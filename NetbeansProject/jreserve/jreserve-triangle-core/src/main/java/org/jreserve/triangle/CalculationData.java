package org.jreserve.triangle;

import javax.swing.event.ChangeListener;

/**
 * Calculations within JReserve are organized into calculation layers. 
 * A calculation layer can represent input data, corrections, link-ratio
 * calculations etc.
 * 
 * <p>This interface is the core interface for the calculation layers
 * and represents all the common functionality for these laers.</p>
 * 
 * <p>Until the calculation is not detached it should listen for
 * changes on it's source calculation and recalculate itself and
 * fire change events when necessary.</p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface CalculationData {

    /**
     * Returns the data, which is the source for this calculation,
     * or null if there is no source for this calculation.
     */
    public CalculationData getSource();
    
    /**
     * After calling this method the claculation should stop listening
     * to changes from it's source data. 
     * 
     * <p>This means that after calling this method the class should not 
     * generate any change events, thus the implementing class may 
     * release all references held on ChangeListeners and other objects 
     * (i.e. entities).</p>
     * 
     * <p>The implementing class must always be able to recalculate it's
     * state, thus it should not realease the reference to it's source.
     * </p>
     * 
     * <p>A call should be cascaded down to the source data.</p>
     */
    public void detach();
    
    /**
     * Recalculates the values for this data layer.
     * 
     * <p>A call should be cascaded down to the source data.</p>
     */
    public void recalculate();
    
    /**
     * Registers the given change listener on this calculation.
     */
    public void addChangeListener(ChangeListener listener);
    
    /**
     * Unregisters the given change listener from this calculation.
     */
    public void removeChangeListener(ChangeListener listener);
}