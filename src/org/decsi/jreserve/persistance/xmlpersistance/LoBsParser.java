package org.decsi.jreserve.persistance.xmlpersistance;

import java.util.ArrayList;
import java.util.List;
import org.decsi.jreserve.data.LoB;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBsParser extends CollectionElementParser<LoB> {
    
    private final static String TAG_NAME = "lobs";

    LoBsParser() {
        super(null, TAG_NAME, new LoBParser());
    }

    @Override
    void childEnded(ElementParser parser) throws SAXException {
        LoB lob = childParser.getValue();
        checkNotExists(lob);
        values.add(lob);
    }
    
    private void checkNotExists(LoB lob) throws SAXException {
        for(LoB existing : values) {
            checkNewId(existing.getId(), lob.getId());
            checkNewName(existing.getName(), lob.getName());
            checkNewShortName(existing.getShortName(), lob.getShortName());
        }
    }

    private void checkNewId(int id1, int id2) throws SAXException {
        if(id1 != id2) return;
        String msg = "LoB with id '%d' already exists!";
        throw new SAXException(String.format(msg, id1));
    }

    private void checkNewName(String n1, String n2) throws SAXException {
        if(!n1.equalsIgnoreCase(n2)) return;
        String msg = "LoB with name '%s' already exists!";
        throw new SAXException(String.format(msg, n1));
    }

    private void checkNewShortName(String n1, String n2) throws SAXException {
        if(!n1.equalsIgnoreCase(n2)) return;
        String msg = "LoB with short name '%s' already exists!";
        throw new SAXException(String.format(msg, n1));
    }
}
