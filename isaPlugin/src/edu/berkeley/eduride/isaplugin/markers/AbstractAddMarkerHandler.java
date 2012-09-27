package edu.berkeley.eduride.isaplugin.markers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import edu.berkeley.eduride.isaplugin.Activator;

public abstract class AbstractAddMarkerHandler extends AbstractHandler {
	// might consider extending AbstractHandlerWithState ?
	
	

	
	
	
	
	
	@Override
	public void setEnabled(Object evaluationContext) {
		// TODO hide when not an ISA -- call  setBaseEnabled(boolean)
	}

	
	
	
	// execute method in child classes
	
	
	
	/// AddMarker helper methods
	
	protected void handleCoreException(CoreException e) {
		// pulled from CoreException javadoc
		IStatus result = new Status(e.getStatus().getSeverity(), Activator.PLUGIN_ID, "Blew a fuse making a marker, yo", e);
		Activator.getDefault().getLog().log(result);
	}

}
