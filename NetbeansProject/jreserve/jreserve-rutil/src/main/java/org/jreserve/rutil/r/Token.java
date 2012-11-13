package org.jreserve.rutil.r;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Token {

    private RTokenType type;
    private int begin;
    private String str;
    
    private Token previous;
    private Token next;
    
    protected Token(int begin, RTokenType type, String str) {
        this.type = type;
        this.begin = begin;
        this.str = str;
    }
    
    public RTokenType getType() {
        return type;
    }
    
    public int getBegin() {
        return begin;
    }
    
    public int getLength() {
        return str.length();
    }
    
    public String getStr() {
        return str;
    }
    
    void setPrevious(Token token) {
        this.previous = token;
        if(token != null)
            token.next = this;
    }
}