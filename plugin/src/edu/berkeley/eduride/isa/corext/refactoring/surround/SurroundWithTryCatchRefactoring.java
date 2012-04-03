/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.corext.refactoring.surround;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;

import edu.berkeley.eduride.isa.corext.codemanipulation.ContextSensitiveImportRewriteContext;
import edu.berkeley.eduride.isa.corext.codemanipulation.StubUtility;
import edu.berkeley.eduride.isa.corext.dom.ASTNodeFactory;
import edu.berkeley.eduride.isa.corext.dom.CodeScopeBuilder;
import edu.berkeley.eduride.isa.corext.dom.Selection;
import edu.berkeley.eduride.isa.corext.fix.LinkedProposalModel;
import edu.berkeley.eduride.isa.corext.refactoring.Checks;
import edu.berkeley.eduride.isa.corext.refactoring.RefactoringCoreMessages;
import edu.berkeley.eduride.isa.corext.refactoring.util.RefactoringASTParser;
import edu.berkeley.eduride.isa.corext.refactoring.util.ResourceUtil;
import edu.berkeley.eduride.isa.corext.refactoring.util.SelectionAwareSourceRangeComputer;
import edu.berkeley.eduride.isa.corext.util.Strings;

/**
 * Surround a set of statements with a try/catch block.
 *
 * Special case:
 *
 * URL url= file.toURL();
 *
 * In this case the variable declaration statement gets convert into a
 * declaration without initializer. So the body of the try/catch block
 * only consists of new assignments. In this case we can't move the
 * selected nodes (e.g. the declaration) into the try block.
 */
public class SurroundWithTryCatchRefactoring extends Refactoring {

	public static final String GROUP_EXC_TYPE= "exc_type"; //$NON-NLS-1$
	public static final String GROUP_EXC_NAME= "exc_name"; //$NON-NLS-1$

	private Selection fSelection;
	private SurroundWithTryCatchAnalyzer fAnalyzer;
	private boolean fLeaveDirty;

	private ICompilationUnit fCUnit;
	private CompilationUnit fRootNode;
	private ASTRewrite fRewriter;
	private ImportRewrite fImportRewrite;
	private CodeScopeBuilder.Scope fScope;
	private ASTNode[] fSelectedNodes;

	private LinkedProposalModel fLinkedProposalModel;

	private SurroundWithTryCatchRefactoring(ICompilationUnit cu, Selection selection) {
		fCUnit= cu;
		fSelection= selection;
		fLeaveDirty= false;
	}

	public static SurroundWithTryCatchRefactoring create(ICompilationUnit cu, ITextSelection selection) {
		return new SurroundWithTryCatchRefactoring(cu, Selection.createFromStartLength(selection.getOffset(), selection.getLength()));
	}

	public static SurroundWithTryCatchRefactoring create(ICompilationUnit cu, int offset, int length) {
		return new SurroundWithTryCatchRefactoring(cu, Selection.createFromStartLength(offset, length));
	}

	public LinkedProposalModel getLinkedProposalModel() {
		return fLinkedProposalModel;
	}

	public void setLeaveDirty(boolean leaveDirty) {
		fLeaveDirty= leaveDirty;
	}

	public boolean stopExecution() {
		if (fAnalyzer == null)
			return true;
		ITypeBinding[] exceptions= fAnalyzer.getExceptions();
		return exceptions == null || exceptions.length == 0;
	}

	/* non Java-doc
	 * @see IRefactoring#getName()
	 */
	public String getName() {
		return RefactoringCoreMessages.SurroundWithTryCatchRefactoring_name;
	}

	public RefactoringStatus checkActivationBasics(CompilationUnit rootNode) throws CoreException {
		RefactoringStatus result= new RefactoringStatus();
		fRootNode= rootNode;

		fAnalyzer= new SurroundWithTryCatchAnalyzer(fCUnit, fSelection);
		fRootNode.accept(fAnalyzer);
		result.merge(fAnalyzer.getStatus());
		return result;
	}


	/*
	 * @see Refactoring#checkActivation(IProgressMonitor)
	 */
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		CompilationUnit rootNode= new RefactoringASTParser(AST.JLS3).parse(fCUnit, true, pm);
		return checkActivationBasics(rootNode);
	}

	/*
	 * @see Refactoring#checkInput(IProgressMonitor)
	 */
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException {
		return Checks.validateModifiesFiles(
			ResourceUtil.getFiles(new ICompilationUnit[]{fCUnit}),
			getValidationContext());
	}

	/* non Java-doc
	 * @see IRefactoring#createChange(IProgressMonitor)
	 */
	public Change createChange(IProgressMonitor pm) throws CoreException {
		final String NN= ""; //$NON-NLS-1$
		if (pm == null) pm= new NullProgressMonitor();
		pm.beginTask(NN, 2);
		try {
			final CompilationUnitChange result= new CompilationUnitChange(getName(), fCUnit);
			if (fLeaveDirty)
				result.setSaveMode(TextFileChange.LEAVE_DIRTY);
			MultiTextEdit root= new MultiTextEdit();
			result.setEdit(root);
			fRewriter= ASTRewrite.create(fAnalyzer.getEnclosingBodyDeclaration().getAST());
			fRewriter.setTargetSourceRangeComputer(new SelectionAwareSourceRangeComputer(
				fAnalyzer.getSelectedNodes(), fCUnit.getBuffer(), fSelection.getOffset(), fSelection.getLength()));
			fImportRewrite= StubUtility.createImportRewrite(fRootNode, true);

			fLinkedProposalModel= new LinkedProposalModel();

			fScope= CodeScopeBuilder.perform(fAnalyzer.getEnclosingBodyDeclaration(), fSelection).
				findScope(fSelection.getOffset(), fSelection.getLength());
			fScope.setCursor(fSelection.getOffset());

			fSelectedNodes= fAnalyzer.getSelectedNodes();

			createTryCatchStatement(fCUnit.getBuffer(), fCUnit.findRecommendedLineSeparator());

			if (fImportRewrite.hasRecordedChanges()) {
				TextEdit edit= fImportRewrite.rewriteImports(null);
				root.addChild(edit);
				result.addTextEditGroup(new TextEditGroup(NN, new TextEdit[] {edit} ));
			}
			TextEdit change= fRewriter.rewriteAST();
			root.addChild(change);
			result.addTextEditGroup(new TextEditGroup(NN, new TextEdit[] {change} ));
			return result;
		} finally {
			pm.done();
		}
	}

	private AST getAST() {
		return fRootNode.getAST();
	}

	private void createTryCatchStatement(org.eclipse.jdt.core.IBuffer buffer, String lineDelimiter) throws CoreException {
		List result= new ArrayList(1);
		TryStatement tryStatement= getAST().newTryStatement();
		ITypeBinding[] exceptions= fAnalyzer.getExceptions();
		ImportRewriteContext context= new ContextSensitiveImportRewriteContext(fAnalyzer.getEnclosingBodyDeclaration(), fImportRewrite);
		for (int i= 0; i < exceptions.length; i++) {
			ITypeBinding exception= exceptions[i];
			String type= fImportRewrite.addImport(exception, context);
			CatchClause catchClause= getAST().newCatchClause();
			tryStatement.catchClauses().add(catchClause);
			SingleVariableDeclaration decl= getAST().newSingleVariableDeclaration();
			String varName= StubUtility.getExceptionVariableName(fCUnit.getJavaProject());

			String name= fScope.createName(varName, false);
			decl.setName(getAST().newSimpleName(name));
			decl.setType(ASTNodeFactory.newType(getAST(), type));
			catchClause.setException(decl);
			Statement st= getCatchBody(type, name, lineDelimiter);
			if (st != null) {
				catchClause.getBody().statements().add(st);
			}
			fLinkedProposalModel.getPositionGroup(GROUP_EXC_TYPE + i, true).addPosition(fRewriter.track(decl.getType()), i == 0);
			fLinkedProposalModel.getPositionGroup(GROUP_EXC_NAME + i, true).addPosition(fRewriter.track(decl.getName()), false);
		}
		List variableDeclarations= getSpecialVariableDeclarationStatements();
		ListRewrite statements= fRewriter.getListRewrite(tryStatement.getBody(), Block.STATEMENTS_PROPERTY);
		boolean selectedNodeRemoved= false;
		ASTNode expressionStatement= null;
		for (int i= 0; i < fSelectedNodes.length; i++) {
			ASTNode node= fSelectedNodes[i];
			if (node instanceof VariableDeclarationStatement && variableDeclarations.contains(node)) {
				AST ast= getAST();
				VariableDeclarationStatement statement= (VariableDeclarationStatement)node;
				// Create a copy and remove the initializer
				VariableDeclarationStatement copy= (VariableDeclarationStatement)ASTNode.copySubtree(ast, statement);
				List modifiers= copy.modifiers();
				for (Iterator iter= modifiers.iterator(); iter.hasNext();) {
					IExtendedModifier modifier= (IExtendedModifier) iter.next();
					if (modifier.isModifier() && Modifier.isFinal(((Modifier)modifier).getKeyword().toFlagValue())) {
						iter.remove();
					}
				}
				List fragments= copy.fragments();
				for (Iterator iter= fragments.iterator(); iter.hasNext();) {
					VariableDeclarationFragment fragment= (VariableDeclarationFragment)iter.next();
					fragment.setInitializer(null);
				}
				CompilationUnit root= (CompilationUnit)statement.getRoot();
				int extendedStart= root.getExtendedStartPosition(statement);
				// we have a leading comment and the comment is covered by the selection
				if (extendedStart != statement.getStartPosition() && extendedStart >= fSelection.getOffset()) {
					String commentToken= buffer.getText(extendedStart, statement.getStartPosition() - extendedStart);
					commentToken= Strings.trimTrailingTabsAndSpaces(commentToken);
					Type type= statement.getType();
					String typeName= buffer.getText(type.getStartPosition(), type.getLength());
					copy.setType((Type)fRewriter.createStringPlaceholder(commentToken + typeName, type.getNodeType()));
				}
				result.add(copy);
				// convert the fragments into expression statements
				fragments= statement.fragments();
				if (!fragments.isEmpty()) {
					List newExpressionStatements= new ArrayList();
					for (Iterator iter= fragments.iterator(); iter.hasNext();) {
						VariableDeclarationFragment fragment= (VariableDeclarationFragment)iter.next();
						Expression initializer= fragment.getInitializer();
						if (initializer != null) {
							Assignment assignment= ast.newAssignment();
							assignment.setLeftHandSide((Expression)fRewriter.createCopyTarget(fragment.getName()));
							assignment.setRightHandSide((Expression)fRewriter.createCopyTarget(initializer));
							newExpressionStatements.add(ast.newExpressionStatement(assignment));
						}
					}
					if (!newExpressionStatements.isEmpty()) {
						if (fSelectedNodes.length == 1) {
							expressionStatement= fRewriter.createGroupNode((ASTNode[])newExpressionStatements.toArray(new ASTNode[newExpressionStatements.size()]));
						} else {
							fRewriter.replace(
								statement,
								fRewriter.createGroupNode((ASTNode[])newExpressionStatements.toArray(new ASTNode[newExpressionStatements.size()])),
								null);
						}
					} else {
						fRewriter.remove(statement, null);
						selectedNodeRemoved= true;
					}
				} else {
					fRewriter.remove(statement, null);
					selectedNodeRemoved= true;
				}
			}
		}
		result.add(tryStatement);
		ASTNode replacementNode;
		if (result.size() == 1) {
			replacementNode= (ASTNode)result.get(0);
		} else {
			replacementNode= fRewriter.createGroupNode((ASTNode[])result.toArray(new ASTNode[result.size()]));
		}
		if (fSelectedNodes.length == 1) {
			if (expressionStatement != null) {
				statements.insertLast(expressionStatement, null);
			} else {
				if (!selectedNodeRemoved)
					statements.insertLast(fRewriter.createMoveTarget(fSelectedNodes[0]), null);
			}
			fRewriter.replace(fSelectedNodes[0], replacementNode, null);
		} else {
			ListRewrite source= fRewriter.getListRewrite(
				fSelectedNodes[0].getParent(),
				(ChildListPropertyDescriptor)fSelectedNodes[0].getLocationInParent());
			ASTNode toMove= source.createMoveTarget(
				fSelectedNodes[0], fSelectedNodes[fSelectedNodes.length - 1],
				replacementNode, null);
			statements.insertLast(toMove, null);
		}
	}

	private List getSpecialVariableDeclarationStatements() {
		List result= new ArrayList(3);
		VariableDeclaration[] locals= fAnalyzer.getAffectedLocals();
		for (int i= 0; i < locals.length; i++) {
			ASTNode parent= locals[i].getParent();
			if (parent instanceof VariableDeclarationStatement && !result.contains(parent))
				result.add(parent);
		}
		return result;

	}

	private Statement getCatchBody(String type, String name, String lineSeparator) throws CoreException {
		String s= StubUtility.getCatchBodyContent(fCUnit, type, name, fSelectedNodes[0], lineSeparator);
		if (s == null) {
			return null;
		} else {
			return (Statement)fRewriter.createStringPlaceholder(s, ASTNode.RETURN_STATEMENT);
		}
	}
}
