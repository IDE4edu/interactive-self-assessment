/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.ui.refactoring.actions;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;


import org.eclipse.jdt.ui.actions.SelectionDispatchAction;

import edu.berkeley.eduride.isa.corext.refactoring.RefactoringAvailabilityTester;
import edu.berkeley.eduride.isa.corext.refactoring.RefactoringExecutionStarter;
import edu.berkeley.eduride.isa.corext.refactoring.util.JavaElementUtil;
import edu.berkeley.eduride.isa.corext.refactoring.util.RefactoringASTParser;
import edu.berkeley.eduride.isa.corext.util.JavaModelUtil;
import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.JavaPlugin;
import edu.berkeley.eduride.isa.ui.actions.ActionUtil;
import edu.berkeley.eduride.isa.ui.actions.SelectionConverter;
import edu.berkeley.eduride.isa.ui.javaeditor.JavaEditor;
import edu.berkeley.eduride.isa.ui.javaeditor.JavaTextSelection;
import edu.berkeley.eduride.isa.ui.refactoring.RefactoringMessages;
import edu.berkeley.eduride.isa.ui.util.ExceptionHandler;

/**
 * Inlines a method.
 *
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class InlineMethodAction extends SelectionDispatchAction {

	private JavaEditor fEditor;

	/**
	 * Note: This constructor is for internal use only. Clients should not call this constructor.
	 * @param editor the java editor
	 */
	public InlineMethodAction(JavaEditor editor) {
		this(editor.getEditorSite());
		fEditor= editor;
		setEnabled(SelectionConverter.canOperateOn(fEditor));
	}

	public InlineMethodAction(IWorkbenchSite site) {
		super(site);
		setText(RefactoringMessages.InlineMethodAction_inline_Method);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.INLINE_ACTION);
	}

	//---- structured selection ----------------------------------------------

	/*
	 * @see SelectionDispatchAction#selectionChanged(IStructuredSelection)
	 */
	public void selectionChanged(IStructuredSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
		} catch (JavaModelException e) {
			if (JavaModelUtil.isExceptionToBeLogged(e))
				JavaPlugin.log(e);
		}
	}

	/*
	 * @see SelectionDispatchAction#run(IStructuredSelection)
	 */
	public void run(IStructuredSelection selection) {
		try {
			Assert.isTrue(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
			IMethod method= (IMethod) selection.getFirstElement();
			ISourceRange nameRange= method.getNameRange();
			run(nameRange.getOffset(), nameRange.getLength(), method.getTypeRoot());
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e, getShell(), RefactoringMessages.InlineMethodAction_dialog_title, RefactoringMessages.InlineMethodAction_unexpected_exception);
		}
	}

	/*
	 * @see SelectionDispatchAction#selectionChanged(ITextSelection)
	 */
	public void selectionChanged(ITextSelection selection) {
		setEnabled(true);
	}

	/*
	 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#selectionChanged(edu.berkeley.eduride.isa.ui.javaeditor.JavaTextSelection)
	 */
	public void selectionChanged(JavaTextSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
		} catch (JavaModelException e) {
			setEnabled(false);
		}
	}

	/*
	 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#run(org.eclipse.jface.text.ITextSelection)
	 */
	public void run(ITextSelection selection) {
		ITypeRoot typeRoot= SelectionConverter.getInput(fEditor);
		if (typeRoot == null)
			return;
		if (! JavaElementUtil.isSourceAvailable(typeRoot))
			return;
		run(selection.getOffset(), selection.getLength(), typeRoot);
	}

	private void run(int offset, int length, ITypeRoot typeRoot) {
		if (!ActionUtil.isEditable(fEditor, getShell(), typeRoot))
			return;
		CompilationUnit compilationUnit= RefactoringASTParser.parseWithASTProvider(typeRoot, true, null);
		if (!RefactoringExecutionStarter.startInlineMethodRefactoring(typeRoot, compilationUnit, offset, length, getShell())) {
			MessageDialog.openInformation(getShell(), RefactoringMessages.InlineMethodAction_dialog_title, RefactoringMessages.InlineMethodAction_no_method_invocation_or_declaration_selected);
		}
	}

	public boolean tryInlineMethod(ITypeRoot typeRoot, CompilationUnit node, ITextSelection selection, Shell shell) {
		return RefactoringExecutionStarter.startInlineMethodRefactoring(typeRoot, node, selection.getOffset(), selection.getLength(), shell);
	}
}
