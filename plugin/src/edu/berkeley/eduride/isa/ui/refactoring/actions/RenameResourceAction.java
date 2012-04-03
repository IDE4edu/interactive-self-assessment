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


import org.eclipse.core.resources.IResource;

import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.ui.IWorkbenchSite;


import org.eclipse.jdt.ui.actions.SelectionDispatchAction;

import edu.berkeley.eduride.isa.corext.refactoring.RefactoringAvailabilityTester;
import edu.berkeley.eduride.isa.corext.refactoring.RefactoringExecutionStarter;

public class RenameResourceAction extends SelectionDispatchAction {

	public RenameResourceAction(IWorkbenchSite site) {
		super(site);
	}

	public void selectionChanged(IStructuredSelection selection) {
		IResource element= getResource(selection);
		if (element == null)
			setEnabled(false);
		else
			setEnabled(RefactoringAvailabilityTester.isRenameAvailable(element));
	}

	public void run(IStructuredSelection selection) {
		IResource resource = getResource(selection);
		if (!RefactoringAvailabilityTester.isRenameAvailable(resource))
			return;
		RefactoringExecutionStarter.startRenameResourceRefactoring(resource, getShell());
	}

	private static IResource getResource(IStructuredSelection selection) {
		if (selection.size() != 1)
			return null;
		Object first= selection.getFirstElement();
		if (! (first instanceof IResource))
			return null;
		return (IResource)first;
	}
}
