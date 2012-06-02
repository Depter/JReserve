package org.decsi.jreserve.persistance.xmlpersistance;

import org.decsi.jreserve.data.ClaimType;
import org.decsi.jreserve.data.LoB;
import org.decsi.jreserve.data.LobBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBParser extends ElementParser<LoB> {

    private final static String TAG_NAME = "lob";
    private final static String CLAIM_TYPES = "claim-types";
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String SHORT_NAME = "short-name";
    
    private StringElementParser nameParser = new StringElementParser(this, NAME);
    private StringElementParser shortNameParser = new StringElementParser(this, SHORT_NAME);
    private CollectionElementParser<ClaimType> claimTypesParser = 
            new CollectionElementParser<ClaimType>(this, CLAIM_TYPES, new ClaimTypeParser());

    private LobBuilder builder = new LobBuilder();
    private LoB lob;
    
    LoBParser(ElementParser parent) {
        super(parent, TAG_NAME);
    }
    
    LoBParser() {
        super(null, TAG_NAME);
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
        else if(claimTypesParser.isThisTag(qName))
            return claimTypesParser.startElement(qName, attributes);
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
    ElementParser endElement(String qName) throws SAXException {
        lob = builder.build();
        if(getParent() != null)
            getParent().childEnded(this);
        return getParent();
    }

    @Override
    void childEnded(ElementParser parser) {
        if(nameParser == parser)
            builder.setName(nameParser.getValue());
        else if(shortNameParser == parser)
            builder.setShortName(shortNameParser.getValue());
        else if(claimTypesParser == parser)
            addClaimTypes();
    }

    private void addClaimTypes() {
        for(ClaimType claimType : claimTypesParser.getValue())
            builder.addClaimType(claimType);
    }
    
    @Override
    LoB getValue() {
        return lob;
    }
}
