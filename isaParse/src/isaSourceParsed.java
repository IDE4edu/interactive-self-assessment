import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.*;

/**
 * This is the object that is used to generate messages the XML based messages 
 * for communication between clients and servers. Data is stored in a 
 * marshalled String format in this object.
 */
public class isaSourceParsed {
	/** dtd stuff:
	 * 	many sources, have to have something to parse isa files
	 *  + means 1 or more occurrences
	 *  * means 0 or more occurrences
	 *  ? means 0 or 1 occurrence
	 */

	public String getFileName(){
		return fileName;
	}
	
	public ArrayList<hidden> getHiddenList(){
		return hiddenList;
	}
	public hidden nextHidden(){
		if (hiddenIterator.hasNext()){
			return hiddenIterator.next();
		}
		System.out.println("no more hidden fields!");
		return null;
	}

	
	public ArrayList<textArea> getTextAreaList(){
		return textAreaList;
	}
	public textArea nextTextArea(){
		if (textAreaIterator.hasNext()){
			return textAreaIterator.next();
		}
		System.out.println("no more text areas!");
		return null;
	}
	
	public ArrayList<textBox> getTextBoxList(){
		return textBoxList;
	}
	public textBox nextTextBox(){
		if (textBoxIterator.hasNext()){
			return textBoxIterator.next();
		}
		System.out.println("no more text boxes!");
		return null;
	}
	
	public ArrayList<assertion> getAssertionList(){
		return assertionList;
	}
	public assertion nextAssertion(){
		if (assertionIterator.hasNext()){
			return assertionIterator.next();
		}
		System.out.println("no more assertions!");
		return null;
	}
	
	
	public ArrayList<test> getTestList(){
		return testList;
	}
	public test nextTest(){
		if (testIterator.hasNext()){
			return testIterator.next();
		}
		System.out.println("no more tests!");
		return null;
	}
	
	
	
	String fileName;
	ArrayList<hidden> hiddenList = new ArrayList<hidden>();
	ArrayList<textArea> textAreaList= new ArrayList<textArea>();
	ArrayList<textBox> textBoxList = new ArrayList<textBox>();
	ArrayList<assertion> assertionList = new ArrayList<assertion>();
	ArrayList<test> testList = new ArrayList<test>();
	
	Iterator<hidden> hiddenIterator = hiddenList.iterator();
	Iterator<textArea> textAreaIterator = textAreaList.iterator();
	Iterator<textBox> textBoxIterator = textBoxList.iterator();
	Iterator<assertion> assertionIterator = assertionList.iterator();
	Iterator<test> testIterator = testList.iterator();

	public isaSourceParsed(Node sourceNode){
		try {
			System.out.println("	"  + sourceNode.getNodeName());
			System.out.println("--------------------");
			if (sourceNode.getNodeType() != Node.ELEMENT_NODE){
				System.out.println("Bad XML file: Source is not an Element");
				System.exit(1);
			}

			Element source = (Element) sourceNode;

			parsefileName(source);
			parseHidden(source);
			parseTextArea(source);
			parseTextBox(source);
			parseAssertion(source);
			parseTest(source);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parsefileName(Element source) throws BadXmlException{
		NodeList NfileNameList = source.getElementsByTagName("filename");

		// check to make sure there's only 1 filename
		if (NfileNameList.getLength() != 1) throw new BadXmlException("Bad XML file: incorrect number of filenames");

		Node fileNameNode = NfileNameList.item(0);
		
		fileName = fileNameNode.getNodeValue();
	}
	
	private void parseHidden(Element source) throws BadXmlException{
		String name = null;
		String tag = null;
		NodeList NhiddenList = source.getElementsByTagName("hidden");
		
		for (int i = 0; i < NhiddenList.getLength(); i++) {
			Node HiddenNode = NhiddenList.item(i);
			int temp = i + 1;
			
			if (HiddenNode.getNodeType() != Node.ELEMENT_NODE) throw new BadXmlException("Bad XML file: hidden " + temp + " is not an Element");

			Element hidden = (Element) HiddenNode;
			
			if("".equals(hidden.getAttribute("name"))) name = null;
			else name = hidden.getAttribute("name");
			
			tag = findSingleData(hidden, "tag", "hidden");

			hiddenList.add(new hidden(name,tag));
		}
	}
	
	private void parseTextArea(Element source) throws BadXmlException{
		String name = null;
		String header = null;
		int size = 0;
		NodeList NtextAreaList = source.getElementsByTagName("textarea");

		for (int i = 0; i < NtextAreaList.getLength(); i++) {
			Node TextAreaNode = NtextAreaList.item(i);
			int temp = i + 1;

			if (TextAreaNode.getNodeType() != Node.ELEMENT_NODE) throw new BadXmlException("Bad XML file: textarea " + temp + " is not an Element");

			Element textarea = (Element) TextAreaNode;

			if("".equals(textarea.getAttribute("name"))) throw new BadXmlException("Bad XML file: textarea " + temp + " has no name");
			else name = textarea.getAttribute("name");

			header = findSingleData(textarea, "header", "textarea");
			size = Integer.parseInt(findSingleData(textarea, "size", "textarea"));
			textAreaList.add(new textArea(name, header, size));
		}
	}
	
	private void parseTextBox(Element source) throws BadXmlException{
		String name = null;
		String header = null;
		String start = null;
		String end = null;
		NodeList NtextBoxList = source.getElementsByTagName("textbox");

		for (int i = 0; i < NtextBoxList.getLength(); i++) {
			Node TextBoxNode = NtextBoxList.item(i);
			int temp = i+1;
			if (TextBoxNode.getNodeType() != Node.ELEMENT_NODE) throw new BadXmlException("Bad XML file: textbox " + temp + " is not an Element");
			
			Element textbox = (Element) TextBoxNode;
			
			if("".equals(textbox.getAttribute("name"))) throw new BadXmlException("Bad XML file: textbox " + temp + " has no name");
			else name = textbox.getAttribute("name");

			header = findSingleData(textbox, "header", "textbox");
			start = findSingleData(textbox, "start", "textbox");
			end = findSingleData(textbox, "end", "textbox");
			
			textBoxList.add(new textBox(name, header, start, end));
		}
	}
	
	private void parseAssertion (Element source) throws BadXmlException{
		String name = null;
		String method = null;
		String target = null;
		String params = null;
		String msg = null;

		NodeList NAssertionList = source.getElementsByTagName("assertion");

		for (int i = 0; i < NAssertionList.getLength(); i++) {
			Node AssertionNode = NAssertionList.item(i);
			int temp = i+1;
			if (AssertionNode.getNodeType() != Node.ELEMENT_NODE){
				throw new BadXmlException("Bad XML file: assertion " + temp + " is not an Element");
			}

			Element assertion = (Element) AssertionNode;
			
			if ("".equals(assertion.getAttribute("name"))) name = null;
			else name = assertion.getAttribute("name");
			
			method = findSingleData(assertion, "method", "assertion");
			target = findSingleData(assertion, "target", "assertion");
			params = findQuestionMarkData(assertion, "params", "assertion");
			msg = findQuestionMarkData(assertion, "msg", "assertion");
			
			assertionList.add(new assertion(name, method, target, params, msg));
		}
	}

	private void parseTest (Element source) throws BadXmlException{
		String name = null;
		String target = null;
		ArrayList<jUnit> jUnit = null;

		NodeList NTestList = source.getElementsByTagName("test");

		for (int i = 0; i < NTestList.getLength(); i++) {
			Node TestNode = NTestList.item(i);
			int temp = i+1;
			if (TestNode.getNodeType() != Node.ELEMENT_NODE){
				throw new BadXmlException("Bad XML file: test " + temp + " is not an Element");
			}

			Element test = (Element) TestNode;
			
			if ("".equals(test.getAttribute("name"))) name = null;
			else name = test.getAttribute("name");
			
			target = findSingleData(test, "target", "test");
			jUnit = findjUnitData(test, "jUnit", "test");
			
			testList.add(new test(name, jUnit, target));
		}
	}

	public String getFileContents(){
		// TODO implement
		// look for in /, look for in /default, throw an exception if its not there, etc
		return null;
	}

	public static String findSingleData(Element eElement, String tagName, String debug) throws BadXmlException{
		NodeList list = eElement.getElementsByTagName(tagName);
		
		if (list.getLength() != 1) throw new BadXmlException("Bad XML file: incorrect number of "+debug+"."+tagName);
		
		Node node = list.item(0);
		
		return node.getNodeValue();
	}

	public static String findQuestionMarkData(Element eElement, String tagName, String debug) throws BadXmlException{
		NodeList list = eElement.getElementsByTagName(tagName);
		
		if (list.getLength() == 0) return null;
			
		if (list.getLength() != 1) return list.item(0).getNodeValue();
		
		// else
		throw new BadXmlException("Bad XML file: incorrect number of "+debug+"."+tagName);
		
	}

	public static ArrayList<jUnit> findjUnitData(Element eElement, String tagName, String debug) throws BadXmlException{
		NodeList list = eElement.getElementsByTagName(tagName);
		ArrayList<jUnit> rtn = new ArrayList<jUnit>();
		
		for (int i = 0; i < list.getLength(); i++){
			Node node = list.item(i);
			rtn.add(new jUnit(node.getNodeValue()));
		}
		return rtn;
	}
}