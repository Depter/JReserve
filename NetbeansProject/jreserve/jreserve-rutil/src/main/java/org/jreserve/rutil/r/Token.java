package org.jreserve.rutil.r;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class Token {

    private RTokenType type;
    private int begin;
    private StringBuilder text = new StringBuilder();
    
    protected Token(int begin, RTokenType type) {
        this.type = type;
        this.begin = begin;
    }
    
    public RTokenType getType() {
        return type;
    }
    
    public int getBegin() {
        return begin;
    }
    
    public int getLength() {
        return text.length();
    }
    
    protected void appendChar(char c) {
        text.append(c);
    }
    
    protected abstract boolean acceptsNextChar(char c);
}
