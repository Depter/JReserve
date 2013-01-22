package org.jreserve.factor.core.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.factor.core.data.CummulatedTriangularData;
import org.jreserve.factor.core.data.SimpleTriangularFactors;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.ChangeableTriangularData;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.comment.TriangleCommentListener;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.entities.TriangleModification;
import org.jreserve.triangle.entities.TriangleModificationListener;
import org.jreserve.triangle.util.TriangleUtil;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SingleFactorSelectionBundle implements ChangeableTriangularData {
    
    private ModificationCommentListener updateListener;
    private TriangleModificationListener weakModificationListener;
    private TriangleCommentListener weakCommentListener;
    
    private SingleFactorSelection factorSelection;
    private ChangeableTriangularData source;
    private List<TriangularData> mods = new ArrayList<TriangularData>();
    private TriangularData top = null;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    private List<TriangleComment> comments = new ArrayList<TriangleComment>();
    
    SingleFactorSelectionBundle(SingleFactorSelection factorSelection) {
        this.factorSelection = factorSelection;
        initSource();
        registerListeners();
        update();
    }
    
    private void initSource() {
        source = factorSelection.getTriangle().getTriangularData();
        source = new CummulatedTriangularData(source);
        source = new SimpleTriangularFactors(source);
    }
    
    private void registerListeners() {
        updateListener = new ModificationCommentListener();
        registerWeakListeners();
        source.addChangeListener(new SourceChangeListener());
    }
    
    private void registerWeakListeners() {
        weakModificationListener = WeakListeners.create(TriangleModificationListener.class, updateListener, factorSelection);
        factorSelection.addTriangleModificationListener(weakModificationListener);
        weakCommentListener = WeakListeners.create(TriangleCommentListener.class, updateListener, factorSelection);
        factorSelection.addTriangleCommentListener(weakCommentListener);
    }
    
    private void update() {
        updateModifications();
        updateComments();
    }
    
    private void updateModifications() {
        mods.clear();
        mods.add(source);
        addModifications();
    }
    
    private void addModifications() {
        top = source;
        for(TriangleModification tmod : factorSelection.getModifications()) {
            TriangularData mod = tmod.createModification(top);
            mods.add(mod);
            top = mod;
        }
    }
    
    private void updateComments() {
        this.comments = factorSelection.getComments();
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
        return TriangleUtil.filterValues(comments, accident, development);
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
    public void close() {
        top.close();
        factorSelection.removeTriangleModificationListener(weakModificationListener);
        factorSelection.removeTriangleCommentListener(weakCommentListener);
        this.factorSelection = null;
    }
    
    @Override
    public String toString() {
        String msg = "SingleFactorSelectionBundle [triangle = %s]";
        String name = factorSelection==null? null : factorSelection.getTriangle().getName();
        return String.format(msg, name);
    }

    private class ModificationCommentListener implements TriangleModificationListener, TriangleCommentListener {

        @Override
        public void modificationsChanged(ModifiableTriangle triangle) {
            updateComments();
            fireChange();
        }

        @Override
        public void commentsChanged(CommentableTriangle commentable) {
            updateComments();
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
