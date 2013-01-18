package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.ChangeableTriangularData;
import org.jreserve.triangle.TriangleModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleAdapter;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.entities.TriangleListener;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleBundle implements ChangeableTriangularData {
    
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
        registerTriangleListener();
        update();
    }
    
    private void registerTriangleListener() {
        updateListener = new UpdateListener();
        weakUpdateListener = WeakListeners.create(TriangleListener.class, updateListener, triangle);
        triangle.addTriangleListener(weakUpdateListener);
    }
    
    private void update() {
        updateGeometry();
        updateModifications();
        updateComments();
        fireChange();
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
    public List<TriangularData> getLayers() {
        return new ArrayList<TriangularData>(mods);
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
    public void createTriangle(String triangleName, RCode rCode) {
        top.createTriangle(triangleName, rCode);
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
    public void close() {
        top.close();
        triangle.removeTriangleListener(weakUpdateListener);
        this.triangle = null;
    }
    
    private class UpdateListener extends TriangleAdapter {

        @Override
        public void commentsChanged(Triangle triangle) {
            updateComments();
            fireChange();
        }

        @Override
        public void geometryChanged(Triangle triangle) {
            update();
        }

        @Override
        public void modificationsChanged(Triangle triangle) {
            updateModifications();
        }
    }
}
