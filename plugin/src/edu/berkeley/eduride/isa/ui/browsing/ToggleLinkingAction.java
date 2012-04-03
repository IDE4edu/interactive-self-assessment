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
package edu.berkeley.eduride.isa.ui.browsing;

import edu.berkeley.eduride.isa.ui.actions.AbstractToggleLinkingAction;


/**
 * This action toggles whether this package explorer links its selection to the active
 * editor.
 *
 * @since 2.1
 */
public class ToggleLinkingAction extends AbstractToggleLinkingAction {

	JavaBrowsingPart fJavaBrowsingPart;

	/**
	 * Constructs a new action.
	 * @param part the browsing part
	 */
	public ToggleLinkingAction(JavaBrowsingPart part) {
		setChecked(part.isLinkingEnabled());
		fJavaBrowsingPart= part;
	}

	/**
	 * Runs the action.
	 */
	public void run() {
		fJavaBrowsingPart.setLinkingEnabled(isChecked());
	}

}
