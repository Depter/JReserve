package org.decsi.jreserve.persistance.xmlpersistance;

import org.decsi.jreserve.data.ClaimType;
import org.decsi.jreserve.data.ClaimTypeBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeParser extends ElementParser<ClaimType> {
    
    private final static String TAG_NAME = "claim-type";
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String SHORT_NAME = "short-name";
    
    private StringElementParser nameParser = new StringElementParser(this, NAME);
    private StringElementParser shortNameParser = new StringElementParser(this, SHORT_NAME);
    
    private ClaimTypeBuilder builder = new ClaimTypeBuilder();
    private ClaimType claimType;
    
    ClaimTypeParser() {
        super(null, TAG_NAME);
    }
    
    ClaimTypeParser(ElementParser parent) {
        super(parent, TAG_NAME);
    }
    
    @Override
    ElementParser startElement(String qName, Attributes attributes) throws SAXException {
        if(isThisTag(qName)) {
            setId(attributes);
            return this;
        } else if(nameParser.isThisTag(qName))
            return nameParser.startElement(qName, attributes);
        else if(shortNameParser.isThisTag(qName))
            return shortNameParser.startElement(qName, attributes);
        else
            throw getUnknonChildrenException(qName);
    }
    
    private void setId(Attributes attributes) throws SAXException {
        int id = getIntAttribute(ID, attributes, false);
        builder.setId(id);
    }
    
    @Override
    void text(String text) throws SAXException {
    }

    @Override
    void childEnded(ElementParser parser) {
        if(nameParser == parser)
            builder.setName(nameParser.getValue());
        else if(shortNameParser == parser)
            builder.setShortName(shortNameParser.getValue());
    }

    @Override
    ElementParser endElement(String qName) throws SAXException {
        claimType = builder.build();
        if(getParent() != null)
            getParent().childEnded(this);
        return getParent();
    }
    
    @Override
    ClaimType getValue() {
        return claimType;
    }
}
