package org.jreserve.rutil.r;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RTokenizer {

    private final static String[] KEYWORDS = {
        "NA", "Inf", "NaN", "NULL", "TRUE", "FALSE", 
        "NA_integer_", "NA_real_", "NA_complex_", "NA_character_",
        "if", "else", "repeat", "while", "function", 
        "for", "in", "next", "break"
    };
    
    private int position;
    private int begin;
    private char[] txt;
    private int size;
    private StringBuilder str = new StringBuilder();
    private Token token = null;
    
    public RTokenizer(String str) {
        txt = str.toCharArray();
        size = txt.length;
        position = 0;
    }
    
    public boolean hasNext() {
        return position < size;
    }
    
    public int getBegin() {
        return begin;
    }
    
    public Token next() {
        begin = position;
        str.setLength(0);
        readToken(txt[position]);
        return token;
    }
    
    private void readToken(char c) {
        Token result;
        if(Character.isWhitespace(c))
            result = readWhitespaceToken();
        else if(c == '#')
            result = readCommentToken();
        else if(c == '"')
            result = readString();
        else if(isDoubleOperator())
            result = readDoubleOperator();
        else if(isOperator(c))
            result = readOperator();
        else
            result = readSymbol();
        
        result.setPrevious(token);
        token = result;
    }
    
    private Token readWhitespaceToken() {
        while(position < size && Character.isWhitespace(txt[position]))
            str.append(txt[position++]);
        return new Token(begin, RTokenType.WHITESPACE, str.toString());
    }

    private Token readCommentToken() {
        while(position < size && !isNewLine(txt[position]))
            str.append(txt[position++]);
        return new Token(begin, RTokenType.COMMENT, str.toString());
    }
    
    private boolean isNewLine(char c) {
        return c == '\n' || c == '\r';
    }

    private Token readString() {
        int count = 0;
        while(position < size && count < 2) {
            char c = txt[position];
            str.append(c);
            if(c == '"') 
                count++;
        }
        return new Token(begin, RTokenType.STRING, str.toString());
    }
    
    private boolean isDoubleOperator() {
        if((position+1) >= size)
            return false;
        return isDoubleOperator(txt[position], txt[position+1]);
    }
    
    private boolean isDoubleOperator(char c0, char c1) {
        return (c0=='=' && c1=='=') ||
               (c0=='>' && c1=='=') ||
               (c0=='<' && c1=='=') ||
               (c0=='!' && c1=='=') ||
               (c0=='&' && c1=='&') ||
               (c0=='<' && c1=='-');
    }
    
    private Token readDoubleOperator() {
        str.append(txt[position++]).append(txt[position++]);
        return new Token(begin, RTokenType.DOUBLE_OPERATOR, str.toString());
    }
    
    private boolean isOperator(char c) {
        boolean operator = (c=='(' || c==')' || c=='{' || c=='}' || c=='[' || c==']' || 
               c=='+' || c=='-' || c=='*' || c=='/' || c=='%' || c=='^' || 
               c=='=' || c=='!' || c==':' || c=='<' || c=='>' || c=='!' ||
               c==',');
        return operator;
    }
    
    private Token readOperator() {
        str.append(txt[position++]);
        return new Token(begin, RTokenType.OPERATOR, str.toString());
    }

    private Token readSymbol() {
        while(position < size && !Character.isWhitespace(txt[position]) && !isOperator(txt[position]))
            str.append(txt[position++]);
        String text = str.toString();
        RTokenType type = getTokenType(text);
        return new Token(begin, type, text);
    }
    
    private RTokenType getTokenType(String str) {
        if(isKeyword(str))
            return RTokenType.KEYWORD; 
        if(isNumber(str))
            return RTokenType.NUMBER;
        return RTokenType.SYMBOL;
    }
    
    private boolean isKeyword(String str) {
        for(String word : KEYWORDS)
            if(word.equals(str))
                return true;
        return false;
    }
    
    private boolean isNumber(String str) {
        int decimalPointCount = 0;
        for(char c : str.toCharArray()) {
            if(c == '.') {
                if(++decimalPointCount>1)
                    return false;
            } else if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}