package org.jreserve.rutil.visual;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.r.RTokenType;
import org.jreserve.rutil.r.RTokenizer;
import org.jreserve.rutil.r.Token;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RCodeTextPane extends JTextPane implements ChangeListener {

    private RCode code;
    private DefaultStyledDocument document;
    private Map<RTokenType, Style> styles = new EnumMap<RTokenType, Style>(RTokenType.class);
    
    public RCodeTextPane(RCode code) {
        document = new DefaultStyledDocument();
        initStyles();
        super.setDocument(document);
        this.code = code;
        this.code.addChangeListener(this);
        stateChanged(null);
    }
    
    private void initStyles() {
        Style base = document.addStyle("base", null);
        //WHITESPACE
        styles.put(RTokenType.WHITESPACE, base);
        
        //KEYWORD
        Style keyword = document.addStyle("keyword", base);
        StyleConstants.setForeground(keyword, Color.BLUE);
        StyleConstants.setBold(keyword, true);
        styles.put(RTokenType.KEYWORD, keyword);
        
        //COMMENT
        Style comment = document.addStyle("comment", base);
        StyleConstants.setForeground(comment, Color.DARK_GRAY);
        styles.put(RTokenType.COMMENT, comment);
        
        //STRING,
        Style string = document.addStyle("string", base);
        StyleConstants.setForeground(string, Color.ORANGE);
        styles.put(RTokenType.STRING, string);
        
        //DOUBLE_OPERATOR, OPERATOR
        Style operator = document.addStyle("operator", base);
        StyleConstants.setForeground(operator, Color.BLACK);
        styles.put(RTokenType.OPERATOR, operator);
        styles.put(RTokenType.DOUBLE_OPERATOR, operator);
        
        //SYMBOL,
        Style symbol = document.addStyle("symbol", base);
        StyleConstants.setForeground(symbol, Color.BLACK);
        StyleConstants.setBold(symbol, true);
        styles.put(RTokenType.SYMBOL, symbol);
        
        //NUMBER
        Style number = document.addStyle("number", base);
        StyleConstants.setForeground(number, new Color(00, 96, 00));
        StyleConstants.setBold(number, true);
        styles.put(RTokenType.NUMBER, number);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        super.setText(code.toRCode());
        try {
            format();
        } catch (BadLocationException ex) {}
    }
    
    private void format() throws BadLocationException {
        String str = document.getText(0, document.getLength());
        RTokenizer tokenizer = new RTokenizer(str);
        while(tokenizer.hasNext())
            formatToken(tokenizer.next());
    }
    
    private void formatToken(Token token) {
        Style style = styles.get(token.getType());
        int offset = token.getBegin();
        int length = token.getLength();
        document.setCharacterAttributes(offset, length, style, true);
    }
}
