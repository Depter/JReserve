package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangularDataModification implements TriangularDataModification {

    protected TriangularData source = TriangularData.EMPTY;
    protected ChangeListener sourceListener;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean detached = false;
    
    protected AbstractTriangularDataModification(TriangularData source) {
        this.source = source==null? TriangularData.EMPTY : source;
        sourceListener = new SourceListener();
        this.source.addChangeListener(sourceListener);
    }
    
    @Override
    public void setSource(TriangularData source) {
        this.source = source==null? TriangularData.EMPTY : source;
    }

    @Override
    public TriangularData getSource() {
        return source;
    }
    
    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        return source.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return source.getDevelopmentName(accident, development);
    }

    @Override
    public double[][] toArray() {
        int accidents = getAccidentCount();
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        int developments = getDevelopmentCount(accident);
        double[] result = new double[developments];
        for(int d=0; d<developments; d++)
            result[d] = getValue(accident, d);
        return result;
    }

    @Override
    public List<TriangleComment> getComments(int accident, int development) {
        return source.getComments(accident, development);
    }
    
    @Override
    public void recalculate() {
        if(source != null)
            source.recalculate();
        recalculateCorrection();
    }

    protected abstract void recalculateCorrection();
    
    @Override
    public void detach() {
        if(!detached) {
            detached = true;
            detachSource();
            listeners.clear();
        }
    }
    
    private void detachSource() {
        if(source != null) {
            source.detach();
            source.removeChangeListener(sourceListener);
        }
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    
    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        if(source != null)
            source.createRTriangle(triangleName, rCode);
        modifyRTriangle(triangleName, rCode);
    }

    /**
     * Appends the modification to the r-code.
     */
    protected abstract void modifyRTriangle(String triangleName, RCode rCode);
    
    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireChange() {
        if(detached) return;
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    protected boolean withinSourceBounds(TriangleCell.Provider cell) {
        return withinSourceBounds(cell.getTriangleCell());
    }
    
    protected boolean withinSourceBounds(TriangleCell cell) {
        int accident = cell.getAccident();
        int development = cell.getDevelopment();
        return withinSourceBounds(accident, development);
    }
    
    protected boolean withinSourceBounds(int accident, int development) {
        return accident >= 0 && 
               accident < source.getAccidentCount() &&
               development >= 0 && 
               development < source.getDevelopmentCount(accident);
    }    

    private class SourceListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            recalculate();
            fireChange();
        }
    }
}
