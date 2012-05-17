package org.decsi.jreserve.data;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleCell {
    
    public int getAccidentPeriod();
    public int getDevelopmentPeriod();
    public double getOriginalValue();
    public double getValue();
    public List<Comment> getComments();
}
