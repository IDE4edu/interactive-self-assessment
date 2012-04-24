package edu.berkeley.eduride.isa.ui.javaeditor;

import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

public final class ISAKeyListener {
	private final ISAEventListener fEventListener= new ISAEventListener();
	
	private final class ISAEventListener implements VerifyKeyListener {

		public void verifyKey(VerifyEvent event) {
			// TODO Auto-generated method stub
			System.err.println("omg");
		}
		
	}
}
