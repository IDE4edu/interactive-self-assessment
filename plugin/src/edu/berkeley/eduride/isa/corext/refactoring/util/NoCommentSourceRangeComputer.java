/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.berkeley.eduride.isa.corext.refactoring.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer;

public class NoCommentSourceRangeComputer extends TargetSourceRangeComputer {
	public SourceRange computeSourceRange(ASTNode node) {
		return new SourceRange(node.getStartPosition(), node.getLength());
	}
}
