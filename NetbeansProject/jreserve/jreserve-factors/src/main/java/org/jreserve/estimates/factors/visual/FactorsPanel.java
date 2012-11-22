package org.jreserve.estimates.factors.visual;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.DataLoader;
import org.jreserve.triangle.widget.*;
import org.jreserve.triangle.widget.model.TriangleCellFactory;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
public class FactorsPanel extends NavigablePanel implements PropertyChangeListener {
    
    private final static String POPUP_PATH = "JReserve/Popup/FactorsPanel";
    private final static java.awt.Image IMG = ImageUtilities.loadImage("resources/factors.png", false);
    
    private ProjectElement<Triangle> triangle;
    private ProjectElement<? extends PersistentObject> estimate;
    
    private TriangleWidget widget;
    private FactorInput factorInput;
    private DataLoader<Triangle> loader;
    private Lookup lookup;
    
    public FactorsPanel(ProjectElement<Triangle> triangle, ProjectElement<? extends PersistentObject> estimate) {
        super("Factors", IMG);
        this.triangle = triangle;
        this.estimate = estimate;
        this.factorInput = new FactorInput();
        initComponents();
        startLoader();
        initListener();
        lookup = new ProxyLookup(widget.getLookup(), Lookups.fixed(estimate));
    }
    
    private void initComponents() {
        widget = new TriangleWidget();
        widget.setManualEvents(true);
        widget.setViewButtonsVisible(false);
        widget.setCummulateButtonsVisible(false);
        widget.setVisibleDigits(5);
        widget.setEditableLayer(TriangleCell.CORRECTION_LAYER);
        widget.addTriangleWidgetListener(new WidgetListener());
        widget.setPopUpActionPath(POPUP_PATH);
        setContent(widget);
    }
    
    private void initListener() {
        triangle.addPropertyChangeListener(this);
    }
    
    private void startLoader() {
        loader = new DataLoader<Triangle>(triangle.getValue(), new LoaderCallback());
        loader.start();
    }

    public TriangleWidget getTriangleWidget() {
        return widget;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void parentClosed() {
        triangle.removePropertyChangeListener(this);
    }
    
    private TriangleGeometry getTraingleGeometry() {
        return ((TriangleGeometry) triangle.getProperty(Triangle.GEOMETRY_PROPERTY)).copy();
    }
    
    private List<TriangleCorrection> getTriangleCorrections() {
        return (List<TriangleCorrection>) triangle.getProperty(Triangle.CORRECTION_PROPERTY);
    }
    
    private List<Smoothing> getTriangleSmoothings() {
        return (List<Smoothing>) triangle.getProperty(Triangle.SMOOTHING_PROPERTY);
    }
    
    private List<TriangleComment> getFactorComments() {
        return (List<TriangleComment>) estimate.getProperty(FactorSelection.FACTOR_SELECTION_COMMENTS);
    }
    
    private List<TriangleCorrection> getFactorCorrections() {
        return (List<TriangleCorrection>) estimate.getProperty(FactorSelection.FACTOR_SELECTION_CORRECTIONS);
    }
    
    private List<Smoothing> getFactorSmoothings() {
        return (List<Smoothing>) estimate.getProperty(FactorSelection.FACTOR_SELECTION_SMOOTHINGS);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if(Triangle.GEOMETRY_PROPERTY.equals(property)) {
            factorInput.geometry = getTraingleGeometry();
            factorInput.calculateFactors();
        } else if(Triangle.CORRECTION_PROPERTY.equals(property)) {
            factorInput.triangleCorrections = getTriangleCorrections();
            factorInput.calculateFactors();
        } else if(Triangle.GEOMETRY_PROPERTY.equals(property)) {
            factorInput.triangleSmoothings = getTriangleSmoothings();
            factorInput.calculateFactors();
        }
    }
    
    private class WidgetListener extends TriangleWidgetAdapter {

        @Override
        public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
            if(layer==TriangleCell.CORRECTION_LAYER && !equals(oldValue, newValue)) {
                List<WidgetData<Double>> values = widget.getValueLayer(TriangleCell.CORRECTION_LAYER);
                widget.setValueLayer(TriangleCell.CORRECTION_LAYER, values);
                List<TriangleCorrection> datas = getCorrections(values);
                estimate.setProperty(FactorSelection.FACTOR_SELECTION_CORRECTIONS, datas);
            }
        }
        
        private List<TriangleCorrection> getCorrections(List<WidgetData<Double>> values) {
            PersistentObject owner = estimate.getValue();
            return DataUtil.escapeCorrections(owner, values);
        }
    
        private boolean equals(Double d1, Double d2) {
            if(d1 == null) return d2 == null;
            if(d2 == null) return false;
            return d1.equals(d2);
        }
    }
    
    private class LoaderCallback implements DataLoader.Callback<Triangle> {

        @Override
        public void finnished(DataLoader<Triangle> loader) {
            try {
                factorInput.values = loader.getData();
                factorInput.calculateFactors();
                widget.setComments(DataUtil.convertComments(getFactorComments()));
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                widget.setManualEvents(false);
                widget.fireTriangleStructureChanged();
            }
        }
    }
    
    private class FactorInput {
        
        private TriangleGeometry geometry;
        private List<ClaimValue> values;
        private List<TriangleCorrection> triangleCorrections;
        private List<Smoothing> triangleSmoothings;
        
        private List<TriangleCorrection> factorCorrections;
        private List<Smoothing> factorSmoothings;

        FactorInput() {
            geometry = getTraingleGeometry();
            triangleCorrections = getTriangleCorrections();
            triangleSmoothings = getTriangleSmoothings();
            
            factorCorrections = getFactorCorrections();
            factorSmoothings = getFactorSmoothings();
        }
        
        void calculateFactors() {
            if(isComplete()) {
                widget.setManualEvents(true);
                TriangleCell[][] input = createInputCells();
                widget.setTriangleGeometry(createGeoemtry());
                widget.setValueLayer(TriangleCell.VALUE_LAYER, calculateFactors(input));
                widget.setValueLayer(TriangleCell.CORRECTION_LAYER, DataUtil.convertCorrections(factorCorrections));
                applySmoothings(widget.getCellArray(), factorSmoothings);
                widget.setManualEvents(false);
                widget.fireTriangleStructureChanged();
            }
        }
        
        private boolean isComplete() {
            return geometry != null &&
                   triangleCorrections!=null && triangleSmoothings!=null &&
                   factorCorrections!=null && factorSmoothings!=null;
        }
        
        private TriangleCell[][] createInputCells() {
            TriangleCell[][] cells = new TriangleCellFactory(geometry).createCells();
            TriangleCellUtil.setCellValues(cells, DataUtil.convertDatas(values), TriangleCell.VALUE_LAYER);
            TriangleCellUtil.setCellValues(cells, DataUtil.convertCorrections(triangleCorrections), TriangleCell.CORRECTION_LAYER);
            applySmoothings(cells, triangleSmoothings);
            TriangleCellUtil.cummulate(cells);
            return cells;
        }
        
        private void applySmoothings(TriangleCell[][] cells, List<Smoothing> smoothings) {
            Smoother smoother = new Smoother(cells, TriangleCell.SMOOTHING_LAYER);
            sortSmoothings(smoothings);
            for(Smoothing smoothing : smoothings)
                smoother.smooth(smoothing);
        }
        
        private void sortSmoothings(List<Smoothing> smoothings) {
            Collections.sort(smoothings, new Comparator<Smoothing>() {
                @Override
                public int compare(Smoothing o1, Smoothing o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
        }
        
        private TriangleGeometry createGeoemtry() {
            int dPeriods = geometry.getDevelopmentPeriods() - 1;
            int dMonths = geometry.getDevelopmentMonths();
            int aMonths = geometry.getAccidentMonths();
            int maxDevelopments = dPeriods * dMonths;

            int aCount = 0;
            while((maxDevelopments - (aCount+1) * aMonths) > 0)
                aCount++;

            return new TriangleGeometry(geometry.getStartDate(), 
                    aCount+1, aMonths, 
                    dPeriods, dMonths);
        }
        
        private List<WidgetData<Double>> calculateFactors(TriangleCell[][] cells) {
            List<WidgetData<Double>> result = new ArrayList<WidgetData<Double>>();
            for(TriangleCell[] row : cells)
                calculateFactors(row, result);
            return result;
        }

        private void calculateFactors(TriangleCell[] row, List<WidgetData<Double>> result) {
            for(TriangleCell cell : row) {
                WidgetData<Double> factor = createFactor(cell);
                if(factor != null)
                    result.add(factor);
            }
        }
        
        private WidgetData<Double> createFactor(TriangleCell cell) {
            if(cell == null) return null;
            TriangleCell prev = cell.getPreviousCell();
            if(prev == null) return null;
            return createFactor(prev, cell);
        }
        
        private WidgetData<Double> createFactor(TriangleCell c1, TriangleCell c2) {
            Double d1 = c1.getDisplayValue();
            Double d2 = c2.getDisplayValue();
            if(d1==null || d2==null) return null;
            return new WidgetData<Double>(c1.getAccidentBegin(), c1.getDevelopmentBegin(), d2 / d1);
        }
    
    }
}
