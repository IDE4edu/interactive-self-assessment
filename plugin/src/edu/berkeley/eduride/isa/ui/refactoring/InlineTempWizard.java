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
package edu.berkeley.eduride.isa.ui.refactoring;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.PlatformUI;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;


import edu.berkeley.eduride.isa.corext.refactoring.code.InlineTempRefactoring;
import edu.berkeley.eduride.isa.corext.util.Messages;
import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.viewsupport.BasicElementLabels;

public class InlineTempWizard extends RefactoringWizard {

	public InlineTempWizard(InlineTempRefactoring ref) {
		super(ref, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE | NO_BACK_BUTTON_ON_STATUS_DIALOG);
		setDefaultPageTitle(RefactoringMessages.InlineTempWizard_defaultPageTitle);
	}

	protected void addUserInputPages() {
		addPage(new InlineTempInputPage());
	}

	public int getMessageLineWidthInChars() {
		return 0;
	}

	private static class InlineTempInputPage extends MessageWizardPage {

		public static final String PAGE_NAME= "InlineTempInputPage"; //$NON-NLS-1$

		public InlineTempInputPage() {
			super(PAGE_NAME, true, MessageWizardPage.STYLE_QUESTION);
		}

		public void createControl(Composite parent) {
			super.createControl(parent);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.INLINE_TEMP_WIZARD_PAGE);
		}

		protected String getMessageString() {
			InlineTempRefactoring refactoring= (InlineTempRefactoring) getRefactoring();
			int occurrences= refactoring.getReferences().length;
			final String identifier= BasicElementLabels.getJavaElementName(refactoring.getVariableDeclaration().getName().getIdentifier());
			switch (occurrences) {
				case 0:
					return Messages.format(
							RefactoringMessages.InlineTempInputPage_message_zero,
							identifier);

				case 1:
					return Messages.format(RefactoringMessages.InlineTempInputPage_message_one, identifier);

				default:
					return Messages.format(RefactoringMessages.InlineTempInputPage_message_multi, new Object[] {
							new Integer(occurrences), identifier });
			}
		}
	}
}
