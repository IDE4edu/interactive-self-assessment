package edu.berkeley.eduride.isa.ui.javaeditor.isa;
public class BadXmlException extends Exception{
	private static final long serialVersionUID = 1L;
	private String message;

	public BadXmlException(String message) {
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}

}
