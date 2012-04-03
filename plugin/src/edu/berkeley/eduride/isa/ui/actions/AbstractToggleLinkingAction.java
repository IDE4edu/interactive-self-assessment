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
package edu.berkeley.eduride.isa.ui.actions;

import org.eclipse.jface.action.Action;

import org.eclipse.ui.PlatformUI;

import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.JavaPluginImages;


/**
 * This is an action template for actions that toggle whether
 * it links its selection to the active editor.
 *
 * @since 3.0
 */
public abstract class AbstractToggleLinkingAction extends Action {

	/**
	 * Constructs a new action.
	 */
	public AbstractToggleLinkingAction() {
		super(ActionMessages.ToggleLinkingAction_label);
		setDescription(ActionMessages.ToggleLinkingAction_description);
		setToolTipText(ActionMessages.ToggleLinkingAction_tooltip);
		JavaPluginImages.setLocalImageDescriptors(this, "synced.gif"); //$NON-NLS-1$
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.LINK_EDITOR_ACTION);
		setChecked(false);
	}

	/**
	 * Runs the action.
	 */
	public abstract void run();
}
