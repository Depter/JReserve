package org.jreserve.resources.textfieldfilters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MaxLengthDocument extends PlainDocument {

    private final int maxLength;
    
    public MaxLengthDocument(int length) {
        if(length < 1)
            throw new IllegalArgumentException("Length must be at least 1! "+length);
        this.maxLength = length;
    }
    
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null)
            return;
        
        int oldLength = super.getLength();
        int insertLength = str.length();
        if(oldLength + insertLength <= maxLength) {
            super.insertString(offset, str, attr);
        } else {
            str = getNewContent(oldLength, insertLength, str);
            super.replace(0, oldLength, str, attr);
        }
    }
    
    private String getNewContent(int oldLength, int insertLength, String str) throws BadLocationException {
        if(insertLength >= maxLength)
            return str.substring(insertLength - maxLength);
        int bit = maxLength - insertLength;
        int offset = oldLength - bit;
        return super.getText(offset, bit) + str;
    }
}
