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
package edu.berkeley.eduride.isa.ui.preferences;


import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.PlatformUI;

import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.preferences.formatter.CodeFormatterConfigurationBlock;
import edu.berkeley.eduride.isa.ui.preferences.formatter.ProfileConfigurationBlock;

/*
 * The page to configure the code formatter options.
 */
public class CodeFormatterPreferencePage extends ProfilePreferencePage {

	public static final String PREF_ID= "org.eclipse.jdt.ui.preferences.CodeFormatterPreferencePage"; //$NON-NLS-1$
	public static final String PROP_ID= "org.eclipse.jdt.ui.propertyPages.CodeFormatterPreferencePage"; //$NON-NLS-1$

	public CodeFormatterPreferencePage() {
		// only used when page is shown programatically
		setTitle(PreferencesMessages.CodeFormatterPreferencePage_title);
	}

	/* (non-Javadoc)
	 * @see edu.berkeley.eduride.isa.ui.preferences.ProfilePreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
	    super.createControl(parent);
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.CODEFORMATTER_PREFERENCE_PAGE);
	}

	protected ProfileConfigurationBlock createConfigurationBlock(PreferencesAccess access) {
	    return new CodeFormatterConfigurationBlock(getProject(), access);
    }

	/* (non-Javadoc)
	 * @see edu.berkeley.eduride.isa.ui.preferences.PropertyAndPreferencePage#getPreferencePageID()
	 */
	protected String getPreferencePageID() {
		return PREF_ID;
	}

	/* (non-Javadoc)
	 * @see edu.berkeley.eduride.isa.ui.preferences.PropertyAndPreferencePage#getPropertyPageID()
	 */
	protected String getPropertyPageID() {
		return PROP_ID;
	}

}
