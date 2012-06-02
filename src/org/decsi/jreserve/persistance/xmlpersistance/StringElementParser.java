package org.decsi.jreserve.persistance.xmlpersistance;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class StringElementParser extends ElementParser {
    
    private String value;

    StringElementParser(ElementParser parent, String tagName) {
        super(parent, tagName);
    }
    
    @Override
    ElementParser startElement(String qName, Attributes attributes) throws SAXException {
        if(isThisTag(qName))
            return this;
        throw getUnknonChildrenException(qName);
    }

    @Override
    void text(String text) throws SAXException {
        value = text;
    }

    @Override
    ElementParser endElement(String qName) throws SAXException {
        if(getParent() != null)
            getParent().childEnded(this);
        return getParent();
    }

    @Override
    String getValue() {
        return value;
    }

    @Override
    void childEnded(ElementParser parser) {
    }
}
