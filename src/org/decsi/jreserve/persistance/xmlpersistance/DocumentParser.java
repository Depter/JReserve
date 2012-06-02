package org.decsi.jreserve.persistance.xmlpersistance;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DocumentParser extends DefaultHandler {
    
    private ElementParser parser;
    
    DocumentParser(ElementParser rootParser) {
        this.parser = rootParser;
    }
    
    void parser(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, this);
        } catch (Exception ex) {
            //TODO log exception.
            ex.printStackTrace();
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parser = parser.startElement(qName, attributes);
    }
    
    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        String str = new String(chars, start, length);
        parser.text(str);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        parser = parser.endElement(qName);
    }
}
