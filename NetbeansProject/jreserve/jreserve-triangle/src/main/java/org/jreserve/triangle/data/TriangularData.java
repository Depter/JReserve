package org.jreserve.triangle.data;

import java.util.Date;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangularData {
    
    public int getAccidentCount();
    
    public int getDevelopmentCount();
    
    public int getDevelopmentCount(int accident);
    
    public Date getAccidentName(int accident);
    
    public Date getDevelopmentName(int accident, int development);
    
    public double getValue(int accident, int development);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);
}
