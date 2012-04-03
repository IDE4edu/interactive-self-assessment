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
package edu.berkeley.eduride.isa.ui.refactoring.reorg;


import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameCompilationUnitProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameEnumConstProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameFieldProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameJavaProjectProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameLocalVariableProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameNonVirtualMethodProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenamePackageProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameSourceFolderProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameTypeParameterProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameTypeProcessor;
import edu.berkeley.eduride.isa.corext.refactoring.rename.RenameVirtualMethodProcessor;
import edu.berkeley.eduride.isa.ui.refactoring.UserInterfaceManager;

public class RenameUserInterfaceManager extends UserInterfaceManager {
	private static final UserInterfaceManager fgInstance= new RenameUserInterfaceManager();

	public static UserInterfaceManager getDefault() {
		return fgInstance;
	}

	private RenameUserInterfaceManager() {
		put(RenameJavaProjectProcessor.class, RenameUserInterfaceStarter.class, RenameJavaProjectWizard.class);
		put(RenameSourceFolderProcessor.class, RenameUserInterfaceStarter.class, RenameSourceFolderWizard.class);
		put(RenamePackageProcessor.class, RenameUserInterfaceStarter.class, RenamePackageWizard.class);
		put(RenameCompilationUnitProcessor.class, RenameUserInterfaceStarter.class, RenameCuWizard.class);
		put(RenameTypeProcessor.class, RenameUserInterfaceStarter.class, RenameTypeWizard.class);
		put(RenameFieldProcessor.class, RenameUserInterfaceStarter.class, RenameFieldWizard.class);
		put(RenameEnumConstProcessor.class, RenameUserInterfaceStarter.class, RenameEnumConstWizard.class);
		put(RenameTypeParameterProcessor.class, RenameUserInterfaceStarter.class, RenameTypeParameterWizard.class);
		put(RenameNonVirtualMethodProcessor.class, RenameMethodUserInterfaceStarter.class, RenameMethodWizard.class);
		put(RenameVirtualMethodProcessor.class, RenameMethodUserInterfaceStarter.class, RenameMethodWizard.class);
		put(RenameLocalVariableProcessor.class, RenameUserInterfaceStarter.class, RenameLocalVariableWizard.class);
	}
}
