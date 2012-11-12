package org.jreserve.rutil.util;

import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import org.jreserve.rutil.RCode;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RCodeTextPane extends JTextPane implements ChangeListener, DocumentListener {

    private RCode code;
    private DefaultStyledDocument document;
    
    public RCodeTextPane() {
        document = new DefaultStyledDocument();
        document.addDocumentListener(this);
        super.setDocument(document);
    }
    
    public RCodeTextPane(RCode code) {
        this();
        if(code != null) {
            this.code = code;
            this.code.addChangeListener(this);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        super.setText(code.toRCode());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            format();
        } catch (BadLocationException ex) {}
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            format();
        } catch (BadLocationException ex) {}
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void format() throws BadLocationException {
        char[] txt = document.getText(0, document.getLength()).trim().toCharArray();
        
        Token first = new Token();
        Token token = first;
        StringBuilder sb = new StringBuilder();
        for(int i=0, size=txt.length; i<size; i++) {
            if(Character.isWhitespace(txt[i])) {
            
            } else {
            }
        }
    }
    
    private static class Token {
        private int begin;
        StringBuilder str = new StringBuilder();
        private Token next;
        private boolean ended = false;
        
        Token addCharacter(char c) {
            if(Character.isWhitespace(c)) {
                ended = true;
                return this;
            } else {
                return ended? createNextToken(c) : processChar(c);
            }
        }
        
        private Token createNextToken(char c) {
            next = new Token().addCharacter(c);
            return next;
        }
        
        private Token processChar(char c) {
        }
        
        private boolean isOperatorChar(char c) {
            return c == ',' ||
                   c == ';' ||
                   c == ':' ||
                   c == '(' || c == ')' ||
                   c == '{' || c == '}' ||
                   c == '[' || c == ']' ||
                   c == '+' || c == '-' ||
                   c == '*' || c == '/' ||
                   c == '%' || c == '^';
        }
    }
}
