/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.corext;

import edu.berkeley.eduride.isa.ui.JavaPlugin;

/**
 * Facade for JavaPlugin to not contaminate corext classes.
 */
public class Corext {

	public static String getPluginId() {
		return JavaPlugin.getPluginId();
	}
}
