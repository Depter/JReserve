package org.jreserve.rutil.r;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OperatorToken extends Token {

    
    public OperatorToken(int begin, char c) {
        super(begin, RTokenType.OPERATOR);
    }
    
    @Override
    protected boolean acceptsNextChar(char c) {
        return false;
    }

}
