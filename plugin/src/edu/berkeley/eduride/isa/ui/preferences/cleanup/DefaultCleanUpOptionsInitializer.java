/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.ui.preferences.cleanup;


import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer;

import edu.berkeley.eduride.isa.corext.fix.CleanUpConstants;


/**
 * The clean up initializer for clean up mode.
 * 
 * @since 3.5
 */
public class DefaultCleanUpOptionsInitializer implements ICleanUpOptionsInitializer {

	/*
	 * @see org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer#setDefaultOptions(org.eclipse.jdt.ui.cleanup.CleanUpOptions)
	 * @since 3.5
	 */
	public void setDefaultOptions(CleanUpOptions options) {
		CleanUpConstants.setDefaultOptions(CleanUpConstants.DEFAULT_CLEAN_UP_OPTIONS, options);
	}

}
