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

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;


import edu.berkeley.eduride.isa.corext.refactoring.structure.PullUpRefactoringProcessor;
import edu.berkeley.eduride.isa.ui.JavaPluginImages;

/**
 * Refactoring wizard for the pull up refactoring.
 */
public final class PullUpWizard extends RefactoringWizard {

	/** The page name */
	private static final String PAGE_NAME= "PullUpMemberPage"; //$NON-NLS-1$

	private final PullUpRefactoringProcessor fProcessor;

	/**
	 * Creates a new pull up wizard.
	 *
	 * @param processor
	 *           the processor
	 *
	 * @param refactoring
	 *            the pull up refactoring
	 */
	public PullUpWizard(PullUpRefactoringProcessor processor, Refactoring refactoring) {
		super(refactoring, WIZARD_BASED_USER_INTERFACE);
		fProcessor= processor;
		setDefaultPageTitle(RefactoringMessages.PullUpWizard_defaultPageTitle);
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_REFACTOR_PULL_UP);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void addUserInputPages() {
		final PullUpMethodPage page= new PullUpMethodPage(fProcessor);
		addPage(new PullUpMemberPage(PullUpWizard.PAGE_NAME, page, fProcessor));
		addPage(page);
	}
}
