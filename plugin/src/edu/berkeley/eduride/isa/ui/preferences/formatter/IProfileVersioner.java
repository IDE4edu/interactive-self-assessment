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
package edu.berkeley.eduride.isa.ui.preferences.formatter;

import edu.berkeley.eduride.isa.ui.preferences.formatter.ProfileManager.CustomProfile;

public interface IProfileVersioner {

	public int getFirstVersion();

	public int getCurrentVersion();

    public String getProfileKind();

	/**
	 * Update the <code>profile</code> to the
	 * current version number
	 */
	public void update(CustomProfile profile);

}
