package org.jreserve.resources.textfieldfilters;

import javax.swing.JTextField;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CharacterTextField extends JTextField {

    public final static char EMPTY_CHAR = '\u0000';
    
    public CharacterTextField() {
        super.setDocument(new CharacterDocument());
    }

    public char getChar() {
        String str = super.getText();
        if(str == null || str.length() == 0)
            return EMPTY_CHAR;
        return str.charAt(0);
    }
    
    public boolean isEmpty() {
        String str = super.getText();
        return str == null || str.length() == 0;
    }
    
    public void setChar(char c) {
        if(EMPTY_CHAR == c)
            setText(null);
        else
            setText(String.valueOf(c));
    }
}
