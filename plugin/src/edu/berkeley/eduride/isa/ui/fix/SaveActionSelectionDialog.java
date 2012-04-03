/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.ui.fix;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


import edu.berkeley.eduride.isa.corext.fix.CleanUpConstants;
import edu.berkeley.eduride.isa.corext.fix.CleanUpRegistry.CleanUpTabPageDescriptor;
import edu.berkeley.eduride.isa.corext.util.Messages;
import edu.berkeley.eduride.isa.ui.JavaPlugin;
import edu.berkeley.eduride.isa.ui.preferences.cleanup.CleanUpTabPage;

public class SaveActionSelectionDialog extends CleanUpSelectionDialog {

	private static final String PREFERENCE_KEY= "clean_up_save_particpant_modify_dialog"; //$NON-NLS-1$

	public SaveActionSelectionDialog(Shell parentShell, Map settings) {
		super(parentShell, settings, SaveParticipantMessages.CleanUpSaveParticipantPreferenceConfiguration_CleanUpSaveParticipantConfiguration_Title);
	}

	protected NamedCleanUpTabPage[] createTabPages(Map workingValues) {
		CleanUpTabPageDescriptor[] descriptors= JavaPlugin.getDefault().getCleanUpRegistry().getCleanUpTabPageDescriptors(CleanUpConstants.DEFAULT_SAVE_ACTION_OPTIONS);

		NamedCleanUpTabPage[] result= new NamedCleanUpTabPage[descriptors.length];

		for (int i= 0; i < descriptors.length; i++) {
			String name= descriptors[i].getName();
			CleanUpTabPage page= descriptors[i].createTabPage();

			page.setOptionsKind(CleanUpConstants.DEFAULT_SAVE_ACTION_OPTIONS);
			page.setModifyListener(this);
			page.setWorkingValues(workingValues);

			result[i]= new NamedCleanUpTabPage(name, page);
		}

		return result;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout= (GridLayout)parent.getLayout();
		layout.numColumns++;
		layout.makeColumnsEqualWidth= false;
		Label label= new Label(parent, SWT.NONE);
		GridData data= new GridData();
		data.widthHint= layout.horizontalSpacing;
		label.setLayoutData(data);
		super.createButtonsForButtonBar(parent);
	}

	protected String getEmptySelectionMessage() {
		return SaveParticipantMessages.CleanUpSaveParticipantConfigurationModifyDialog_SelectAnAction_Error;
	}

	protected String getSelectionCountMessage(int selectionCount, int size) {
		return Messages.format(SaveParticipantMessages.CleanUpSaveParticipantConfigurationModifyDialog_XofYSelected_Label, new Object[] {new Integer(selectionCount), new Integer(size)});
	}

	protected String getPreferenceKeyPrefix() {
		return PREFERENCE_KEY;
	}
}
