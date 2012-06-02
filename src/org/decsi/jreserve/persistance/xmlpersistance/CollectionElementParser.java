package org.decsi.jreserve.persistance.xmlpersistance;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CollectionElementParser<T> extends ElementParser<List<T>> {
    
    protected List<T> values = new ArrayList<T>();
    protected ElementParser<T> childParser;
    
    CollectionElementParser(ElementParser parent, String tagName, ElementParser<T> childParser) {
        super(parent, tagName);
        this.childParser = childParser;
        childParser.setParent(this);
    }
    
    @Override
    ElementParser startElement(String qName, Attributes attributes) throws SAXException {
        if(isThisTag(qName))
            return this;
        else if(childParser.isThisTag(qName))
            return childParser.startElement(qName, attributes);
        else
            throw getUnknonChildrenException(qName);
    }

    @Override
    void text(String text) throws SAXException {
    }

    @Override
    ElementParser endElement(String qName) throws SAXException {
        if(getParent() != null)
            getParent().childEnded(this);
        return getParent();
    }

    @Override
    void childEnded(ElementParser parser) throws SAXException {
        values.add(childParser.getValue());
    }

    @Override
    List<T> getValue() {
        return values;
    }
}
