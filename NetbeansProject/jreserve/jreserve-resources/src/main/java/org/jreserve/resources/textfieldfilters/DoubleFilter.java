package org.jreserve.resources.textfieldfilters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Peter Decsi
 */
public class DoubleFilter extends PlainDocument {

    private char decimal;
    
    public DoubleFilter(char decimal) {
        this.decimal = decimal;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null)
            return;
        
        for(int i = 0; i < str.length(); i++) {
            if(!acceptsChar(offset, i, str.charAt(i)))
                return;
        }
        
        super.insertString(offset, str, attr);
    }
    
    private boolean acceptsChar(int offset, int pos, char ch) {
        switch(ch) {
            case '0':
                return true;
            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            case '9':
                return true;
            case '-':
                return offset == 0 && pos == 0;
            default:
                return decimal==ch && offset>0 && acceptsDecimal();
        }
    } 
    
    private boolean acceptsDecimal() {
        try {
            String str = super.getText(0, super.getLength());
            return (str.indexOf(decimal) < 0);
        } catch (BadLocationException ex) {
            return false;
        }
    }
    
    
}
