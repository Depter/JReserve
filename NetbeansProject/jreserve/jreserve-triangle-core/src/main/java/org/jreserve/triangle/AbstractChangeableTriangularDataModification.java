package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 */
public abstract class AbstractChangeableTriangularDataModification extends AbstractTriangularDataModification implements ChangeableTriangularData {
    
    protected ChangeListener sourceListener;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    protected AbstractChangeableTriangularDataModification(TriangularData source) {
        super(source);
        sourceListener = new SourceListener();
        this.source = source;
        if(source instanceof ChangeableTriangularData)
            ((ChangeableTriangularData)source).addChangeListener(sourceListener);
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void close() {
        if(source instanceof ChangeableTriangularData)
            ((ChangeableTriangularData)source).removeChangeListener(sourceListener);
        source.close();
        this.source = TriangularData.EMPTY;
    }
    
    protected abstract void sourceChanged();

    protected void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    private class SourceListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            sourceChanged();
            fireChange();
        }
    }
}