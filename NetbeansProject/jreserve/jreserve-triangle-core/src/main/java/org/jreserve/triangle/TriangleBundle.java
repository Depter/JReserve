package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleBundle implements TriangularData, ChangeListener {

    private TriangularData data;
    private TriangularData top;
    private TreeSet<ModifiedTriangularData> modifications = new TreeSet<ModifiedTriangularData>();
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean fireEvent = true;
    
    public TriangleBundle(TriangularData data) {
        initData(data);
        initTop();
    }
    
    private void initData(TriangularData data) {
        if(data == null)
            throw new NullPointerException("Data is null!");
        removeChangeListener(this.data);
        this.data = data;
        this.data.addChangeListener(this);
    }
    
    private void removeChangeListener(TriangularData td) {
        if(td != null)
            td.removeChangeListener(this);
    }
    
    private void initTop() {
        if(modifications.isEmpty())
            top = data;
        else
            top = modifications.last();
    }
    
    public void setData(TriangularData data) {
        initData(data);
        setModificationSource();
        initTop();
        fireChangeEvent();
    }
    
    public TriangularData getSourceData() {
        return data;
    }
    
    private void setModificationSource() {
        if(!modifications.isEmpty())
            modifications.first().setSource(data);
    }
    
    public void setModifications(List<ModifiedTriangularData> modifications) {
        fireEvent = false;
        for(ModifiedTriangularData mod : new ArrayList<ModifiedTriangularData>(this.modifications))
            removeModification(mod);
        for(ModifiedTriangularData mod : modifications)
            addModification(mod);
        fireEvent = true;
        fireChangeEvent();
    }
    
    public void addModification(ModifiedTriangularData modification) {
        checkNewModification(modification);
        modifications.add(modification);
        modification.addChangeListener(this);
        linkModifications();
        fireChangeEvent();
    }
    
    private void checkNewModification(ModifiedTriangularData modification) {
        if(modifications.contains(modification))
            throw new IllegalArgumentException("Modification already exists: "+modification);
    }
    
    private void linkModifications() {
        TriangularData prev = data;
        for(ModifiedTriangularData modification : modifications) {
            modification.setSource(prev);
            prev = modification;
        }
        initTop();
    }
    
    public void removeModification(ModifiedTriangularData modification) {
        ModifiedTriangularData contained = getContainedModification(modification);
        if(contained != null) {
            modifications.remove(contained);
            contained.removeChangeListener(this);
            linkModifications();
            fireChangeEvent();
        }
    }
    
    private ModifiedTriangularData getContainedModification(ModifiedTriangularData modification) {
        for(ModifiedTriangularData m : modifications)
            if(m.equals(modification))
                return m;
        return null;
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
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        if(!fireEvent)
            return;
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireChangeEvent();
    }

    @Override
    public double[][] toArray() {
        return top.toArray();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return top.getLayerTypeId(accident, development);
    }

    @Override
    public void createTriangle(String triangleName, RCode rCode) {
        top.createTriangle(triangleName, rCode);
    }
}
