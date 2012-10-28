package org.jreserve.resources.textfieldfilters;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TextRenderer<T> {

    public String toString(T value);
    
    public T parse(String str);
}
