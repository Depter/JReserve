package org.jreserve.rutil.r;

import java.util.regex.Pattern;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Tokenizer {
//#this is a comment
//smoothing.geometric <- function(triangle, rows, columns, smooth) {
//  length = length(rows);
//  average = 1;
//  for(i in 1:length) average = average * triangle[rows[i], columns[i]];
//  average = average ^ (1/length);
//
//  for(i in 1:length)  #This is a line end comment
//    if(smooth[i])
//      triangle[rows[i], columns[i]] = average;
//
//  triangle;
//}
//
//
//REGEX - groups
//Whitespace
//  \s+
//  java: "\\s+"
//
//Separators
// ,|;|:
// java: 
//
//Assignment
// =|<-
// java: 
//
//Operators
// \+|-|\*|/|%|\^|==|!=
// java: 
//
//Brackets
// \[|\]|\{|\}|\(|\)
// java: 
//
//Numbers
// \d+(\.\d+)?
//  java: "\\d+(\\.\\d+)?"
//
//Comment
// #.*+$
//
//Words
// [\w\.\$]+

    
    private final static String REGEX = "\\s+|,|;|:|=|<-|\\+|-|\\*|/|%|\\^|\\[|\\]|\\{|\\}|\\(|\\)\\\\d+(\\.\\d+)?|[\\w\\.\\$]+";
    private final static Pattern PATTERN = Pattern.compile(REGEX);
    
    public Token getFirstToken(String str) {
        if(str == null) return null;
        str = str.trim();
        return str.length()==0? null : getFirstToken(str.toCharArray());
    }
    
    private Token getFirstToken(char[] text) {
        char c = '\u000a';
        return null;
    }
    
//    private Token getTokenForFirstChar(char c) {
//        if(Character.isWhitespace(c))
//            return new WhitespaceToken(c);
//        return null;
//    }
//    
//    private static boolean isTokenSeparator(char c) {
//        return Character.isWhitespace(c);
//        Character.isw
//    }
}
