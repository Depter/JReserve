package org.jreserve.triangle.editor;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.DataStructure;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.GeometrySettingPanel;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidget.TriangleWidgetListener;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.data.TriangleCell;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class DataEditorView<T extends DataStructure> extends JPanel implements Lookup.Provider, UndoRedo.Provider {
    
    private final static int VALUE_LAYER = 0;
    private final static int CORRECTION_LAYER = 1;
    private final static Color CORRECTION_BG = new Color(235, 204, 204);
    
    protected GeometrySettingPanel geometrySetting;
    protected TriangleWidget triangle;
    
    protected ProjectElement<T> element;
    private DataLoader<T> loader;
    
    private boolean userChanging = true;
    private PropertyChangeListener elementListener;
    private Lookup lookup;
    
    DataEditorView(ProjectElement<T> element) {
        this.element = element;
        userChanging = false;
        initComponents();
        initGeometry();
        initLayers();
        addListeners();
        userChanging = true;
        startLoader();
        lookup = new ProxyLookup(element.getLookup(), triangle.getLookup());
    }
    
    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new GridBagLayout());

        geometrySetting = new GeometrySettingPanel();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.insets = new Insets(0, 0, 10, 0);
        add(geometrySetting, gc);
        
        gc.gridx = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weightx = 1.0;
        Dimension min = new Dimension(0, 0);
        Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        add(new Filler(min, min, max), gc);

        triangle = new TriangleWidget();
        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        triangle.setTriangleGeometry(getElementGeometry());
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx=1d; gc.weighty=1d;
        add(triangle, gc);
    }
    
    protected abstract void initGeometry();
    
    protected void setGeometry(TriangleGeometry geometry) {
        userChanging = false;
        setAccidentGeometry(geometry);
        setDevelopmentGeometry(geometry);
        userChanging = true;
    }
    
    private void setAccidentGeometry(TriangleGeometry geometry) {
        geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
        geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
    }
    
    private void setDevelopmentGeometry(TriangleGeometry geometry) {
        geometrySetting.setDevelopmentStartDate(geometry.getDevelopmentStart());
        geometrySetting.setDevelopmentPeriodCount(geometry.getDevelopmentPeriods());
        geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInDevelopment());
    }
    
    private void initLayers() {
        triangle.addValueLayer(new ArrayList<WidgetData<Double>>());
        triangle.addValueLayer(new ArrayList<WidgetData<Double>>());
        triangle.setLayerBackground(CORRECTION_LAYER, CORRECTION_BG);
        triangle.setEditableLayer(CORRECTION_LAYER);
    }
    
    private void addListeners() {
        geometrySetting.addPropertyChangeListener(new GeometryListener());
        triangle.addTriangleWidgetListener(new TriangleListener());
        
        elementListener = new ElementListener();
        element.addPropertyChangeListener(elementListener);
    }
    
    private void startLoader() {
        T value = element.getValue();
        loader = new DataLoader<T>(value, new LoaderCallback());
        loader.start();
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    @Override
    public UndoRedo getUndoRedo() {
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        return ur!=null? ur : UndoRedo.NONE;
    }
    
    public void closed() {
        element.removePropertyChangeListener(elementListener);
        if(loader != null) {
            loader.cancel();
            loader = null;
        }
    }
    
    protected abstract List<Data<T, Double>> getCorrectionData();
    
    protected abstract void setElementGeometry(TriangleGeometry geometry);
    
    protected abstract TriangleGeometry getElementGeometry();
    
    protected abstract boolean isGeometryChanged(PropertyChangeEvent evt);
    
    protected abstract void updateCorrections(List<Data<T, Double>> datas);
    
    protected abstract List<WidgetData<Comment>> getComments();
    
    protected abstract void updateComments(List<WidgetData<Comment>> comments);
    
    private class LoaderCallback implements DataLoader.Callback<T> {

        @Override
        public void finnished(DataLoader<T> loader) {
            try {
                triangle.setDataValueLayer(VALUE_LAYER, loader.getData());
                setCorrections();
                triangle.setComments(getComments());
            } catch (RuntimeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    
        private void setCorrections() {
            List<Data<T, Double>> corrections = getCorrectionData();
            triangle.setDataValueLayer(CORRECTION_LAYER, corrections);
        }
    }
    
    private class GeometryListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(isGeometryChange(evt))
                changeTriangleGeometry(evt);
        }
        
        private boolean isGeometryChange(PropertyChangeEvent evt) {
            return userChanging &&
                   isGeometryProperty(evt) &&
                   isChangedGeometry(evt);
        }
        
        private boolean isGeometryProperty(PropertyChangeEvent evt) {
            String found = evt.getPropertyName();
            return GeometrySettingPanel.PROPERTY_TRIANGLE_GEOMETRY
                    .equals(found);
        }
        
        private boolean isChangedGeometry(PropertyChangeEvent evt) {
            TriangleGeometry g1 = (TriangleGeometry) evt.getOldValue();
            TriangleGeometry g2 = (TriangleGeometry) evt.getNewValue();
            if(g1 == null) return g2 != null;
            return !g1.isEqualGeometry(g2);
        }
        
        private void changeTriangleGeometry(PropertyChangeEvent evt) {
            TriangleGeometry geomety = (TriangleGeometry) evt.getNewValue();
            setElementGeometry(geomety);
        }
    }
    
    private class ElementListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(isGeometryChanged(evt)) {
                TriangleGeometry geoemtry = getElementGeometry();
                triangle.setTriangleGeometry(geoemtry);
            }
        }
    }
    
    private class TriangleListener implements TriangleWidgetListener {

        @Override
        public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
            if(layer==CORRECTION_LAYER && !equals(oldValue, newValue)) {
                List<WidgetData<Double>> values = triangle.getValueLayer(CORRECTION_LAYER);
                triangle.setValueLayer(CORRECTION_LAYER, values);
                List<Data<T, Double>> datas = getCorrections(element.getValue(), values);
                updateCorrections(datas);
            }
        }
        
        private List<Data<T, Double>> getCorrections(T owner, List<WidgetData<Double>> values) {
            List<Data<T, Double>> datas = new ArrayList<Data<T, Double>>(values.size());
            for(WidgetData<Double> value : values)
                datas.add(new Data<T, Double>(owner, value.getAccident(), value.getDevelopment(), value.getValue()));
            return datas;
        }
    
        private boolean equals(Double d1, Double d2) {
            if(d1 == null) return d2 == null;
            if(d2 == null) return false;
            return d1.equals(d2);
        }

        @Override
        public void commentsChanged() {
            List<WidgetData<Comment>> comments = triangle.getComments();
            updateComments(comments);
        }
    }
    
}
