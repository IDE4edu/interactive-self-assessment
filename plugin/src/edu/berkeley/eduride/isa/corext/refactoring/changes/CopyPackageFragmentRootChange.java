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
package edu.berkeley.eduride.isa.corext.refactoring.changes;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.core.resources.IProject;

import org.eclipse.ltk.core.refactoring.Change;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;


import org.eclipse.jdt.ui.JavaElementLabels;

import edu.berkeley.eduride.isa.corext.refactoring.RefactoringCoreMessages;
import edu.berkeley.eduride.isa.corext.refactoring.reorg.INewNameQuery;
import edu.berkeley.eduride.isa.corext.refactoring.reorg.IPackageFragmentRootManipulationQuery;
import edu.berkeley.eduride.isa.corext.util.Messages;
import edu.berkeley.eduride.isa.ui.viewsupport.BasicElementLabels;

public class CopyPackageFragmentRootChange extends PackageFragmentRootReorgChange {

	public CopyPackageFragmentRootChange(IPackageFragmentRoot root, IProject destination, INewNameQuery newNameQuery, IPackageFragmentRootManipulationQuery updateClasspathQuery) {
		super(root, destination, newNameQuery, updateClasspathQuery);
	}

	protected Change doPerformReorg(IPath destinationPath, IProgressMonitor pm) throws JavaModelException {
		getRoot().copy(destinationPath, getResourceUpdateFlags(), getUpdateModelFlags(true), null, pm);
		return null;
	}

	public String getName() {
		String rootName= JavaElementLabels.getElementLabel(getRoot(), JavaElementLabels.ALL_DEFAULT);
		String destinationName= BasicElementLabels.getResourceName(getDestination());
		return Messages.format(RefactoringCoreMessages.CopyPackageFragmentRootChange_copy, new String[] {rootName, destinationName});
	}
}
