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
package edu.berkeley.eduride.isa.ui.refactoring.code;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;


import edu.berkeley.eduride.isa.corext.refactoring.code.InlineMethodRefactoring;
import edu.berkeley.eduride.isa.ui.JavaPlugin;
import edu.berkeley.eduride.isa.ui.refactoring.RefactoringMessages;

public class InlineMethodWizard extends RefactoringWizard {

	/* package */ static final String DIALOG_SETTING_SECTION= "InlineMethodWizard"; //$NON-NLS-1$

	public InlineMethodWizard(InlineMethodRefactoring ref){
		super(ref, DIALOG_BASED_USER_INTERFACE);
		setDefaultPageTitle(RefactoringMessages.InlineMethodWizard_page_title);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
	}

	protected void addUserInputPages(){
		addPage(new InlineMethodInputPage());
	}
}
