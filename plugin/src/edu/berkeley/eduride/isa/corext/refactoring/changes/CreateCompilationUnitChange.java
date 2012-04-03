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

import org.eclipse.jdt.core.ICompilationUnit;


import edu.berkeley.eduride.isa.corext.refactoring.RefactoringCoreMessages;
import edu.berkeley.eduride.isa.corext.refactoring.nls.changes.CreateTextFileChange;
import edu.berkeley.eduride.isa.corext.util.Messages;
import edu.berkeley.eduride.isa.ui.viewsupport.BasicElementLabels;

public final class CreateCompilationUnitChange extends CreateTextFileChange {

	private final ICompilationUnit fUnit;

	public CreateCompilationUnitChange(ICompilationUnit unit, String source, String encoding) {
		super(unit.getResource().getFullPath(), source, encoding, "java"); //$NON-NLS-1$
		fUnit= unit;
	}

	public String getName() {
		String cuName= BasicElementLabels.getFileName(fUnit);
		String cuContainerName= BasicElementLabels.getPathLabel(fUnit.getParent().getPath(), false);
		return Messages.format(RefactoringCoreMessages.CompilationUnitChange_label, new String[] { cuName, cuContainerName });
	}
}
