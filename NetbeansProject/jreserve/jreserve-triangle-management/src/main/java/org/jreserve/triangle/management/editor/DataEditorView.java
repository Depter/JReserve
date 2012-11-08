package org.jreserve.triangle.management.editor;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import org.jreserve.data.Data;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.DataStructure;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.GeometrySettingPanel;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidget.TriangleWidgetListener;
import org.jreserve.triangle.widget.WidgetData;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DataEditorView.Title=Geometry"
})
abstract class DataEditorView<T extends DataStructure> extends NavigablePanel {
    
    private final static Color CORRECTION_BG = new Color(235, 204, 204);
    private final static Color VALUE_BG = Color.WHITE;
    private final static Color SMOOTHING_BG = new Color(167, 191, 255);
    
    protected GeometrySettingPanel geometrySetting;
    protected TriangleWidget triangle;
    
    protected ProjectElement<T> element;
    private DataLoader<T> loader;
    
    private boolean userChanging = true;
    private PropertyChangeListener elementListener;
    
    DataEditorView(ProjectElement<T> element, Image img) {
        super(Bundle.LBL_DataEditorView_Title(), img);
        this.element = element;
        userChanging = false;
        initComponents();
        initGeometry();
        initLayers();
        addListeners();
        userChanging = true;
        startLoader();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new GridBagLayout());

        geometrySetting = new GeometrySettingPanel();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.insets = new Insets(0, 0, 10, 0);
        panel.add(geometrySetting, gc);
        
        gc.gridx = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weightx = 1.0;
        Dimension min = new Dimension(0, 0);
        Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        panel.add(new Filler(min, min, max), gc);

        triangle = new TriangleWidget();
        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        triangle.setTriangleGeometry(getElementGeometry());
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx=1d; gc.weighty=1d;
        panel.add(triangle, gc);
        super.setContent(panel);
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
        triangle.setLayerBackground(TriangleCell.VALUE_LAYER, VALUE_BG);
        triangle.setLayerBackground(TriangleCell.SMOOTHING_LAYER, SMOOTHING_BG);
        triangle.setLayerBackground(TriangleCell.CORRECTION_LAYER, CORRECTION_BG);
        triangle.setEditableLayer(TriangleCell.CORRECTION_LAYER);
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
    
    protected abstract List<Smoothing> getSmoothings();
    
    private class LoaderCallback implements DataLoader.Callback<T> {

        @Override
        public void finnished(DataLoader<T> loader) {
            try {
                triangle.setDataValueLayer(TriangleCell.VALUE_LAYER, loader.getData());
                setCorrections();
                setSmoothings();
                triangle.setComments(getComments());
            } catch (RuntimeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    
        private void setCorrections() {
            List<Data<T, Double>> corrections = getCorrectionData();
            triangle.setDataValueLayer(TriangleCell.CORRECTION_LAYER, corrections);
        }
        
        private void setSmoothings() {
            Smoother smoother = new Smoother(triangle, TriangleCell.SMOOTHING_LAYER);
            for(Smoothing smoothing : getSmoothings())
                smoother.smooth(smoothing);
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
            if(layer==TriangleCell.CORRECTION_LAYER && !equals(oldValue, newValue)) {
                List<WidgetData<Double>> values = triangle.getValueLayer(TriangleCell.CORRECTION_LAYER);
                triangle.setValueLayer(TriangleCell.CORRECTION_LAYER, values);
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
        }
    }
}
