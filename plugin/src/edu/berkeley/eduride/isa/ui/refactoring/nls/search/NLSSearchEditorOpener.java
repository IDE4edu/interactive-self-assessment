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

package edu.berkeley.eduride.isa.ui.refactoring.nls.search;

import org.eclipse.search.ui.text.Match;

import org.eclipse.jdt.core.IJavaElement;

import edu.berkeley.eduride.isa.ui.search.JavaSearchEditorOpener;

/**
 */
public class NLSSearchEditorOpener extends JavaSearchEditorOpener {
	protected Object getElementToOpen(Match match) {
		Object element= match.getElement();
		if (element instanceof IJavaElement) {
			return element;
		} else if (element instanceof FileEntry) {
			FileEntry fileEntry= (FileEntry) element;
			return fileEntry.getPropertiesFile();
		} else if (element instanceof CompilationUnitEntry) {
			return ((CompilationUnitEntry)element).getCompilationUnit();
		}
		// this should not happen
		return null;
	}
}
