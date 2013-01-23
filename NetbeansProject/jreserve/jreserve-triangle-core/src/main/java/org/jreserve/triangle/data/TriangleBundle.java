package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.entities.*;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleBundle implements TriangularData {
    
    private Triangle triangle;
    private TriangleListener updateListener;
    private TriangleListener weakUpdateListener;
    private AsynchronousTriangleInput source;
    private List<TriangularData> mods = new ArrayList<TriangularData>();
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private TriangularData top = null;
    
    public TriangleBundle(Triangle triangle) {
        this.triangle = triangle;
        this.source = new AsynchronousTriangleInput(triangle.getGeometry(), triangle.getDataType());
        registerListeners();
        update();
    }
    
    private void registerListeners() {
        updateListener = new UpdateListener();
        weakUpdateListener = WeakListeners.create(TriangleListener.class, updateListener, triangle);
        triangle.addTriangleListener(weakUpdateListener);
        source.addChangeListener(new SourceChangeListener());
    }
    
    private void update() {
        updateGeometry();
        updateModifications();
        updateComments();
    }
    
    private void updateGeometry() {
        source.setTriangleGeometry(triangle.getGeometry());
    }
    
    private void updateModifications() {
        mods.clear();
        mods.add(source);
        addModifications();
    }
    
    private void addModifications() {
        top = source;
        for(TriangleModification tmod : triangle.getModifications()) {
            TriangularData mod = tmod.createModification(top);
            mods.add(mod);
            top = mod;
        }
    }
    
    private void updateComments() {
        source.setComments(triangle.getComments());
    }

    @Override
    public int getAccidentCount() {
        return top.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return top.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return top.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        return top.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return top.getDevelopmentName(accident, development);
    }

    @Override
    public double getValue(int accident, int development) {
        return top.getValue(accident, development);
    }

    @Override
    public double[][] toArray() {
        return top.toArray();
    }

    @Override
    public List<TriangleComment> getComments(int accident, int development) {
        return source.getComments(accident, development);
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return top.getLayerTypeId(accident, development);
    }

    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        top.createRTriangle(triangleName, rCode);
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

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public void detach() {
        top.detach();
        triangle.removeTriangleListener(weakUpdateListener);
        this.triangle = null;
    }
    
    @Override
    public String toString() {
        String msg = "TriangleBundle [triangle = %s]";
        String name = triangle==null? null : triangle.getName();
        return String.format(msg, name);
    }

    @Override
    public TriangularData getSource() {
        return null;
    }

    @Override
    public void recalculate() {
    }
    
    private class UpdateListener extends TriangleAdapter {

        @Override
        public void commentsChanged(CommentableTriangle triangle) {
            updateComments();
            fireChange();
        }

        @Override
        public void geometryChanged(Triangle triangle) {
            update();
            fireChange();
        }

        @Override
        public void modificationsChanged(ModifiableTriangle triangle) {
            updateModifications();
            fireChange();
        }
    }
    
    private class SourceChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            fireChange();
        }
    }
}
