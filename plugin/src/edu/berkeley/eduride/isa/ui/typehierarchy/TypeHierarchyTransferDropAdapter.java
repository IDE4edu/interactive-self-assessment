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
package edu.berkeley.eduride.isa.ui.typehierarchy;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.jdt.core.IJavaElement;

import edu.berkeley.eduride.isa.ui.dnd.ViewerInputDropAdapter;
import edu.berkeley.eduride.isa.ui.util.OpenTypeHierarchyUtil;
import edu.berkeley.eduride.isa.ui.util.SelectionUtil;

public class TypeHierarchyTransferDropAdapter extends ViewerInputDropAdapter {

	private TypeHierarchyViewPart fTypeHierarchyViewPart;

	public TypeHierarchyTransferDropAdapter(TypeHierarchyViewPart viewPart, AbstractTreeViewer viewer) {
		super(viewer);
		fTypeHierarchyViewPart= viewPart;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doInputView(Object inputElement) {
		fTypeHierarchyViewPart.setInputElement((IJavaElement) inputElement);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getInputElement(ISelection selection) {
		Object single= SelectionUtil.getSingleElement(selection);
		if (single == null)
			return null;

		IJavaElement[] candidates= OpenTypeHierarchyUtil.getCandidates(single);
		if (candidates != null && candidates.length > 0)
			return candidates[0];

		return null;
	}

}
