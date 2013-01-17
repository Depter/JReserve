package org.jreserve.triangle;

import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ChangeableTriangularData extends TriangularData {
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);

    
    public static interface Provider extends TriangularData.Provider {
        @Override
        public ChangeableTriangularData getTriangularData();
    }
}
