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
package edu.berkeley.eduride.isa.ui.refactoring.actions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;


import org.eclipse.jdt.ui.actions.SelectionDispatchAction;

import edu.berkeley.eduride.isa.corext.refactoring.RefactoringAvailabilityTester;
import edu.berkeley.eduride.isa.corext.refactoring.RefactoringExecutionStarter;
import edu.berkeley.eduride.isa.corext.util.JavaModelUtil;
import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.JavaPlugin;
import edu.berkeley.eduride.isa.ui.actions.ActionUtil;
import edu.berkeley.eduride.isa.ui.actions.SelectionConverter;
import edu.berkeley.eduride.isa.ui.javaeditor.JavaEditor;
import edu.berkeley.eduride.isa.ui.javaeditor.JavaTextSelection;
import edu.berkeley.eduride.isa.ui.refactoring.RefactoringMessages;
import edu.berkeley.eduride.isa.ui.util.ExceptionHandler;

public class MoveStaticMembersAction extends SelectionDispatchAction{

	private JavaEditor fEditor;

	public MoveStaticMembersAction(IWorkbenchSite site) {
		super(site);
		setText(RefactoringMessages.RefactoringGroup_move_label);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.MOVE_ACTION);
	}

	public MoveStaticMembersAction(JavaEditor editor) {
		this(editor.getEditorSite());
		fEditor= editor;
		setEnabled(SelectionConverter.canOperateOn(fEditor));
	}

	public void selectionChanged(IStructuredSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isMoveStaticMembersAvailable(getSelectedMembers(selection)));
		} catch (JavaModelException e) {
			// http://bugs.eclipse.org/bugs/show_bug.cgi?id=19253
			if (JavaModelUtil.isExceptionToBeLogged(e))
				JavaPlugin.log(e);
			setEnabled(false);//no ui
		}
	}

	public void selectionChanged(ITextSelection selection) {
		setEnabled(true);
	}

	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 */
	public void selectionChanged(JavaTextSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isMoveStaticAvailable(selection));
		} catch (JavaModelException e) {
			setEnabled(false);
		}
	}

	public void run(IStructuredSelection selection) {
		try {
			IMember[] members= getSelectedMembers(selection);
			for (int index= 0; index < members.length; index++) {
				if (!ActionUtil.isEditable(getShell(), members[index]))
					return;
			}
			if (RefactoringAvailabilityTester.isMoveStaticMembersAvailable(members))
				RefactoringExecutionStarter.startMoveStaticMembersRefactoring(members, getShell());
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e, RefactoringMessages.OpenRefactoringWizardAction_refactoring, RefactoringMessages.OpenRefactoringWizardAction_exception);
		}
	}

	public void run(ITextSelection selection) {
		try {
			IMember member= getSelectedMemberFromEditor();
			if (!ActionUtil.isEditable(fEditor, getShell(), member))
				return;
			IMember[] array= new IMember[]{member};
			if (member != null && RefactoringAvailabilityTester.isMoveStaticMembersAvailable(array)){
				RefactoringExecutionStarter.startMoveStaticMembersRefactoring(array, getShell());
			} else {
				MessageDialog.openInformation(getShell(), RefactoringMessages.OpenRefactoringWizardAction_unavailable, RefactoringMessages.MoveMembersAction_unavailable);
			}
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e, RefactoringMessages.OpenRefactoringWizardAction_refactoring, RefactoringMessages.OpenRefactoringWizardAction_exception);
		}
	}

	private static IMember[] getSelectedMembers(IStructuredSelection selection){
		if (selection.isEmpty())
			return null;

		for  (final Iterator iterator= selection.iterator(); iterator.hasNext(); ) {
			if (! (iterator.next() instanceof IMember))
				return null;
		}
		Set memberSet= new HashSet();
		memberSet.addAll(Arrays.asList(selection.toArray()));
		return (IMember[]) memberSet.toArray(new IMember[memberSet.size()]);
	}

	private IMember getSelectedMemberFromEditor() throws JavaModelException{
		IJavaElement element= SelectionConverter.getElementAtOffset(fEditor);
		if (element == null || ! (element instanceof IMember))
			return null;
		return (IMember)element;
	}
}
