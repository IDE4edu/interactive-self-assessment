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

import java.util.Map;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;

import edu.berkeley.eduride.isa.ui.fix.AbstractCleanUp;
import edu.berkeley.eduride.isa.ui.fix.MapCleanUpOptions;

public abstract class AbstractCleanUpTabPage extends CleanUpTabPage {

	private AbstractCleanUp[] fPreviewCleanUps;
	private Map fValues;

	public AbstractCleanUpTabPage() {
		super();
	}

	protected abstract AbstractCleanUp[] createPreviewCleanUps(Map values);

	/* 
	 * @see edu.berkeley.eduride.isa.ui.preferences.cleanup.CleanUpTabPage#setWorkingValues(java.util.Map)
	 */
	public void setWorkingValues(Map workingValues) {
		super.setWorkingValues(workingValues);
		fValues= workingValues;
		setOptions(new MapCleanUpOptions(workingValues));
	}

	/* 
	 * @see edu.berkeley.eduride.isa.ui.preferences.cleanup.ICleanUpTabPage#setOptions(edu.berkeley.eduride.isa.ui.fix.CleanUpOptions)
	 */
	public void setOptions(CleanUpOptions options) {
	}

	/* 
	 * @see edu.berkeley.eduride.isa.ui.preferences.cleanup.ICleanUpTabPage#getPreview()
	 */
	public String getPreview() {
		if (fPreviewCleanUps == null) {
			fPreviewCleanUps= createPreviewCleanUps(fValues);
		}
	
		StringBuffer buf= new StringBuffer();
		for (int i= 0; i < fPreviewCleanUps.length; i++) {
			buf.append(fPreviewCleanUps[i].getPreview());
			buf.append("\n"); //$NON-NLS-1$
		}
		return buf.toString();
	}

}
