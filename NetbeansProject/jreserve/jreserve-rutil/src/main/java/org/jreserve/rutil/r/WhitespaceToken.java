package org.jreserve.rutil.r;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WhitespaceToken extends Token {
    
    public WhitespaceToken(int begin, char c) {
        super(begin, RTokenType.WHITESPACE);
        super.appendChar(c);
    }

    @Override
    protected boolean acceptsNextChar(char c) {
        return Character.isWhitespace(c);
    }
}
