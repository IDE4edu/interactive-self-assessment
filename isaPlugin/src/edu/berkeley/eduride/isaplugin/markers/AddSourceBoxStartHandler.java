package edu.berkeley.eduride.isaplugin.markers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


public class AddSourceBoxStartHandler extends AbstractAddMarkerHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//TODO
		System.err.println("Start handler execute:" + event);
		return null;
	}



}
