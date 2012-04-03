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

import org.eclipse.swt.custom.BusyIndicator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;

import org.eclipse.ui.PlatformUI;

import org.eclipse.jdt.ui.JavaElementComparator;

import edu.berkeley.eduride.isa.ui.IJavaHelpContextIds;
import edu.berkeley.eduride.isa.ui.JavaPlugin;
import edu.berkeley.eduride.isa.ui.JavaPluginImages;
import edu.berkeley.eduride.isa.ui.browsing.JavaBrowsingMessages;
import edu.berkeley.eduride.isa.ui.dnd.JdtViewerDropSupport;
import edu.berkeley.eduride.isa.ui.viewsupport.SourcePositionComparator;

/*
 * XXX: This class should become part of the MemberFilterActionGroup
 *      which should be renamed to MemberActionsGroup
 */
public class LexicalSortingAction extends Action {
	private JavaElementComparator fComparator= new JavaElementComparator();
	private SourcePositionComparator fSourcePositonComparator= new SourcePositionComparator();
	private StructuredViewer fViewer;
	private String fPreferenceKey;
	private final JdtViewerDropSupport fDropSupport;

	public LexicalSortingAction(StructuredViewer viewer, String id, JdtViewerDropSupport dropSupport) {
		super();
		fViewer= viewer;
		fDropSupport= dropSupport;
		fPreferenceKey= "LexicalSortingAction." + id + ".isChecked"; //$NON-NLS-1$ //$NON-NLS-2$
		setText(JavaBrowsingMessages.LexicalSortingAction_label);
		JavaPluginImages.setLocalImageDescriptors(this, "alphab_sort_co.gif"); //$NON-NLS-1$
		setToolTipText(JavaBrowsingMessages.LexicalSortingAction_tooltip);
		setDescription(JavaBrowsingMessages.LexicalSortingAction_description);
		boolean checked= JavaPlugin.getDefault().getPreferenceStore().getBoolean(fPreferenceKey);
		valueChanged(checked, false);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.LEXICAL_SORTING_BROWSING_ACTION);
	}

	public void run() {
		valueChanged(isChecked(), true);
	}

	private void valueChanged(final boolean on, boolean store) {
		setChecked(on);
		BusyIndicator.showWhile(fViewer.getControl().getDisplay(), new Runnable() {
			public void run() {
				if (on) {
					fViewer.setComparator(fComparator);
					fDropSupport.setFeedbackEnabled(false);
				} else {
					fViewer.setComparator(fSourcePositonComparator);
					fDropSupport.setFeedbackEnabled(true);
				}
			}
		});

		if (store)
			JavaPlugin.getDefault().getPreferenceStore().setValue(fPreferenceKey, on);
	}
}
