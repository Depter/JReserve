package org.decsi.jreserve.persistance.xmlpersistance;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class ElementParser<T> {

    private ElementParser parent;
    private String tagName;

    ElementParser(ElementParser parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }
    
    ElementParser getParent() {
        return parent;
    }
    
    void setParent(ElementParser parent) {
        this.parent = parent;
    }
    
    String getTagName() {
        return tagName;
    }
    
    boolean isThisTag(String qName) {
        return tagName.equalsIgnoreCase(qName);
    }
    
    SAXException getUnknonChildrenException(String qName) {
        String msg = String.format("Element %s does not allow children '%s'!", getPathToRoot(), qName);
        return new SAXException(msg);
    }
    
    String getPathToRoot() {
        if(parent == null)
            return tagName;
        return parent.getPathToRoot() + " > " + tagName;
    }
    
    abstract ElementParser startElement(String qName, Attributes attributes) throws SAXException;
    
    abstract void text(String text) throws SAXException;
    
    abstract ElementParser endElement(String qName) throws SAXException;
    
    abstract void childEnded(ElementParser parser) throws SAXException;
    
    abstract T getValue();
    
    @Override
    public String toString() {
        return "Element ["+tagName+"]";
    }
    
    String getAttribute(String name, Attributes attributes, boolean allowMissing) throws SAXException {
        String value = attributes.getValue(name);
        if(value!=null || allowMissing)
            return value;
        throw getAttributeMissingException(name);
    }
    
    private SAXException getAttributeMissingException(String name) {
        String msg = "Attribute '%s' is missing from element '%s'!";
        msg = String.format(msg, name, getPathToRoot());
        return new SAXException(msg);
    }
    
    int getIntAttribute(String name, Attributes attributes, boolean allowMissing) throws SAXException {
        String str = getAttribute(name, attributes, allowMissing);
        try {
            return str==null? 0 : Integer.parseInt(str);
        } catch (Exception ex) {
            throw illegalArgumentValueException(name, str);
        }
    }
    
    private SAXException illegalArgumentValueException(String name, String value) {
        String msg = "Value '%s' for attribute '%s' on element '%s' is illegal!";
        msg = String.format(msg, value, name, tagName);
        return new SAXException(msg);
    }
}
