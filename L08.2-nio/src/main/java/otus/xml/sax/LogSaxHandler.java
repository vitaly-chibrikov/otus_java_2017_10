package otus.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LogSaxHandler extends BuilderHandler {
	private static final String CLASSNAME = "class";
	
	private boolean inElement = false;

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Start document");
	}

    @Override
	public void endDocument() throws SAXException {
		System.out.println("End document ");
	}

    @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.println("Start element: " + qName);
		if(!qName.equals(CLASSNAME))
			inElement = true;
		else
			System.out.println("Class name: " + attributes.getValue(0));
		
	}

    @Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("End element: " + qName);
		inElement = false;
	}

    @Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if(inElement)
			System.out.println("Process : " + new String(ch, start, length));
	}

	@Override
	public Object build() {
		return null;
	}
}
