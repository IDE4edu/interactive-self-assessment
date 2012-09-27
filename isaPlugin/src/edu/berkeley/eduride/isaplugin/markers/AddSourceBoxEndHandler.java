package edu.berkeley.eduride.isaplugin.markers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class AddSourceBoxEndHandler extends AbstractAddMarkerHandler {



	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO 
		System.err.println("End handler execute:" + event);
		try {
			MarkerUtilities.createMarker(null,null,"edu.berkeley.eduride.isa.markerBoxEnd");
		} catch (CoreException e) {
			handleCoreException(e);
		}
		return null;
	}



}
