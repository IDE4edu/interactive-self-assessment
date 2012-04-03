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
package edu.berkeley.eduride.isa.ui.text.correction;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;


import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;

import edu.berkeley.eduride.isa.corext.fix.CleanUpConstants;
import edu.berkeley.eduride.isa.corext.fix.IProposableFix;
import edu.berkeley.eduride.isa.corext.fix.PotentialProgrammingProblemsFix;
import edu.berkeley.eduride.isa.ui.JavaPluginImages;
import edu.berkeley.eduride.isa.ui.fix.PotentialProgrammingProblemsCleanUp;
import edu.berkeley.eduride.isa.ui.text.correction.proposals.FixCorrectionProposal;

/**
 * Subprocessor for serial version quickfix proposals.
 *
 * @since 3.1
 */
public final class SerialVersionSubProcessor {

	public static final class SerialVersionProposal extends FixCorrectionProposal {
		private boolean fIsDefaultProposal;

		public SerialVersionProposal(IProposableFix fix, int relevance, IInvocationContext context, boolean isDefault) {
			super(fix, createCleanUp(isDefault), relevance, JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_ADD), context);
			fIsDefaultProposal= isDefault;
		}

		private static ICleanUp createCleanUp(boolean isDefault) {
			Map options= new Hashtable();
			options.put(CleanUpConstants.ADD_MISSING_SERIAL_VERSION_ID, CleanUpOptions.TRUE);
			if (isDefault) {
				options.put(CleanUpConstants.ADD_MISSING_SERIAL_VERSION_ID_DEFAULT, CleanUpOptions.TRUE);
			} else {
				options.put(CleanUpConstants.ADD_MISSING_SERIAL_VERSION_ID_GENERATED, CleanUpOptions.TRUE);
			}
			return new PotentialProgrammingProblemsCleanUp(options);
		}

		public boolean isDefaultProposal() {
			return fIsDefaultProposal;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
			if (fIsDefaultProposal) {
				return CorrectionMessages.SerialVersionDefaultProposal_message_default_info;
			} else {
				return CorrectionMessages.SerialVersionHashProposal_message_generated_info;
			}
		}
	}


	/**
	 * Determines the serial version quickfix proposals.
	 *
	 * @param context
	 *        the invocation context
	 * @param location
	 *        the problem location
	 * @param proposals
	 *        the proposal collection to extend
	 */
	public static final void getSerialVersionProposals(final IInvocationContext context, final IProblemLocation location, final Collection proposals) {

		Assert.isNotNull(context);
		Assert.isNotNull(location);
		Assert.isNotNull(proposals);

		IProposableFix[] fixes= PotentialProgrammingProblemsFix.createMissingSerialVersionFixes(context.getASTRoot(), location);
		if (fixes != null) {
			proposals.add(new SerialVersionProposal(fixes[0], 9, context, true));
			proposals.add(new SerialVersionProposal(fixes[1], 9, context, false));
		}
	}
}
