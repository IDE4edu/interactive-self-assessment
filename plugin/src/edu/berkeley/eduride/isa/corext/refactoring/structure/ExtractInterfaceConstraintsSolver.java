/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.corext.refactoring.structure;

import org.eclipse.core.runtime.Assert;

import edu.berkeley.eduride.isa.corext.refactoring.structure.constraints.SuperTypeConstraintsModel;
import edu.berkeley.eduride.isa.corext.refactoring.structure.constraints.SuperTypeConstraintsSolver;
import edu.berkeley.eduride.isa.corext.refactoring.structure.constraints.SuperTypeSet;
import edu.berkeley.eduride.isa.corext.refactoring.typeconstraints.types.TType;
import edu.berkeley.eduride.isa.corext.refactoring.typeconstraints2.ConstraintVariable2;
import edu.berkeley.eduride.isa.corext.refactoring.typeconstraints2.ITypeSet;
import edu.berkeley.eduride.isa.corext.refactoring.typeconstraints2.ImmutableTypeVariable2;

/**
 * Type constraint solver to solve the extract interface problem.
 */
public final class ExtractInterfaceConstraintsSolver extends SuperTypeConstraintsSolver {

	/** The extracted type name, without any qualification or type parameters */
	private final String fName;

	/**
	 * Creates a new extract interface constraints solver.
	 *
	 * @param model the model to solve
	 * @param name the name of the extracted type
	 */
	public ExtractInterfaceConstraintsSolver(final SuperTypeConstraintsModel model, final String name) {
		super(model);
		Assert.isNotNull(name);
		fName= name;
	}

	/*
	 * @see edu.berkeley.eduride.isa.corext.refactoring.structure.constraints.SuperTypeConstraintsSolver#computeTypeEstimate(edu.berkeley.eduride.isa.corext.refactoring.typeconstraints2.ConstraintVariable2)
	 */
	protected final ITypeSet computeTypeEstimate(final ConstraintVariable2 variable) {
		final TType type= variable.getType();
		if (variable instanceof ImmutableTypeVariable2 || !type.getErasure().equals(fModel.getSubType().getErasure()))
			return SuperTypeSet.createTypeSet(type);
		final TType[] types= type.getInterfaces();
		for (int index= 0; index < types.length; index++) {
			if (types[index].getName().startsWith(fName) && types[index].getErasure().equals(fModel.getSuperType().getErasure()))
				return SuperTypeSet.createTypeSet(type, types[index]);
		}
		return SuperTypeSet.createTypeSet(type);
	}
}
