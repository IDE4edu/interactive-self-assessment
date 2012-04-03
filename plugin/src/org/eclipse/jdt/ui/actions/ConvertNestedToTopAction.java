/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ui.actions;


import java.io.CharConversionException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;


import edu.berkeley.eduride.isa.corext.refactoring.RefactoringAvailabilityTester;
import edu.berkeley.eduride.isa.corext.refactoring.RefactoringExecutionStarter;
import edu.berkeley.eduride.isa.corext.refactoring.util.JavaElementUtil;
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
 * Action to convert a nested class to a top level class.
 *
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @since 2.1
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ConvertNestedToTopAction extends SelectionDispatchAction {

	private JavaEditor fEditor;

	/**
	 * Note: This constructor is for internal use only. Clients should not call
	 * this constructor.
	 * @param editor the java editor
	 *
	 * @noreference This constructor is not intended to be referenced by clients.
	 */
	public ConvertNestedToTopAction(JavaEditor editor) {
		this(editor.getEditorSite());
		fEditor= editor;
		setEnabled(SelectionConverter.canOperateOn(fEditor));
	}

	/**
	 * Creates a new <code>MoveInnerToTopAction</code>. The action requires
	 * that the selection provided by the site's selection provider is of type
	 * <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 *
	 * @param site
	 *            the site providing context information for this action
	 */
	public ConvertNestedToTopAction(IWorkbenchSite site) {
		super(site);
		setText(RefactoringMessages.ConvertNestedToTopAction_Convert);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.MOVE_INNER_TO_TOP_ACTION);
	}

	//---- Structured selection ------------------------------------------------

	/*
	 * @see SelectionDispatchAction#selectionChanged(IStructuredSelection)
	 */
	public void selectionChanged(IStructuredSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isMoveInnerAvailable(selection));
		} catch (JavaModelException e) {
			// http://bugs.eclipse.org/bugs/show_bug.cgi?id=19253
			if (!(e.getException() instanceof CharConversionException) && JavaModelUtil.isExceptionToBeLogged(e))
				JavaPlugin.log(e);
			setEnabled(false);//no UI
		}
	}

	/*
	 * @see SelectionDispatchAction#run(IStructuredSelection)
	 */
	public void run(IStructuredSelection selection) {
		try {
			//we have to call this here - no selection changed event is sent
			// after a refactoring but it may still invalidate enablement
			if (RefactoringAvailabilityTester.isMoveInnerAvailable(selection)) {
				IType singleSelectedType= getSingleSelectedType(selection);
				if (! ActionUtil.isEditable(getShell(), singleSelectedType))
					return;
				RefactoringExecutionStarter.startMoveInnerRefactoring(singleSelectedType, getShell());
			}
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e,
				RefactoringMessages.OpenRefactoringWizardAction_refactoring,
				RefactoringMessages.OpenRefactoringWizardAction_exception);
		}
	}

	private static IType getSingleSelectedType(IStructuredSelection selection) throws JavaModelException {
		if (selection.isEmpty() || selection.size() != 1)
			return null;

		Object first= selection.getFirstElement();
		if (first instanceof IType)
			return (IType)first;
		if (first instanceof ICompilationUnit)
			return JavaElementUtil.getMainType((ICompilationUnit)first);
		return null;
	}

	//---- Text Selection -------------------------------------------------------

	/*
	 * @see SelectionDispatchAction#selectionChanged(ITextSelection)
	 */
	public void selectionChanged(ITextSelection selection) {
		setEnabled(true);
	}

	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 * @param selection the Java text selection (internal type)
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void selectionChanged(JavaTextSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isMoveInnerAvailable(selection));
		} catch (JavaModelException e) {
			setEnabled(false);
		}
	}

	/*
	 * @see SelectionDispatchAction#run(ITextSelection)
	 */
	public void run(ITextSelection selection) {
		try {
			IType type= RefactoringAvailabilityTester.getDeclaringType(SelectionConverter.resolveEnclosingElement(fEditor, selection));
			if (type != null && RefactoringAvailabilityTester.isMoveInnerAvailable(type)) {
				if (! ActionUtil.isEditable(fEditor, getShell(), type))
					return;
				RefactoringExecutionStarter.startMoveInnerRefactoring(type, getShell());
			} else {
				MessageDialog.openInformation(getShell(), RefactoringMessages.OpenRefactoringWizardAction_unavailable, RefactoringMessages.ConvertNestedToTopAction_To_activate);
			}
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e,
				RefactoringMessages.OpenRefactoringWizardAction_refactoring,
				RefactoringMessages.OpenRefactoringWizardAction_exception);
		}
	}
}
