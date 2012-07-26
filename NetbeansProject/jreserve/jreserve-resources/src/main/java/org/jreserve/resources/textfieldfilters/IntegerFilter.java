package org.jreserve.resources.textfieldfilters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IntegerFilter extends PlainDocument {

    public static final String NUMERIC = "0123456789";

    protected String acceptedChars = null;

    protected boolean negativeAccepted = false;

    public IntegerFilter() {
        this(NUMERIC);
    }

    IntegerFilter(String acceptedchars) {
        acceptedChars = acceptedchars;
    }

    public void setNegativeAccepted(boolean negativeaccepted) {
        if(acceptedChars.equals(NUMERIC)) {
            negativeAccepted = negativeaccepted;
            acceptedChars += "-";
        }
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null)
            return;
        
        for(int i = 0; i < str.length(); i++)
            if(acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1)
                return;

        if(negativeAccepted && str.indexOf("-") != -1) 
            if(str.indexOf("-") != 0 || offset != 0) 
                return;
        
        super.insertString(offset, str, attr);
    }
}
