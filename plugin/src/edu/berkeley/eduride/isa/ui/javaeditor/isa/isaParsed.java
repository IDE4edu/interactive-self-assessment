package edu.berkeley.eduride.isa.ui.javaeditor.isa;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class isaParsed {
	ArrayList<isaSourceParsed> sources;
	
	public Document buildDoc(File xmlFile){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException in isaParsed");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException e) {
			System.out.println("SAXException in isaParsed");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOException in isaParsed");
			e.printStackTrace();
			System.exit(1);
		}
		return doc;
	}
	
	public isaParsed(File xmlFile) throws BadXmlException{
		Document doc = buildDoc(xmlFile);

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		System.out.println("--------------------");
		NodeList sourceList = doc.getElementsByTagName("source");
		if(sourceList.getLength() < 1) throw new BadXmlException("Bad XML: needs to have at least one source");
		for (int i = 0; i < sourceList.getLength(); i++){
			sources.add(new isaSourceParsed(sourceList.item(i)));
		}
	}

}
