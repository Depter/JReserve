package org.jreserve.estimates.factors.visual;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.*;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.DataLoader;
import org.jreserve.triangle.widget.*;
import org.jreserve.triangle.widget.model.TriangleCellFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
@ActionReferences({
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.widget.actions.ExcludeCellAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/FactorsPanel",
        position=20
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.widget.actions.IncludeCellAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/FactorsPanel",
        position=30
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.smoothing.actions.AddSmoothingAction", category="JReserve/Smoothing"),
        path="JReserve/Popup/FactorsPanel",
        position=50,
        separatorBefore=40
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.smoothing.actions.DeleteSmoothingAction", category="JReserve/Smoothing"),
        path="JReserve/Popup/FactorsPanel",
        position=60
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.widget.actions.AddCommentAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/FactorsPanel",
        position=100,
        separatorBefore=90
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.widget.actions.DeleteCommentsAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/FactorsPanel",
        position=200
    )
})
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
        lookup = new ProxyLookup(widget.getLookup(), estimate.getLookup());
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
        estimate.addPropertyChangeListener(this);
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
        return (List<TriangleCorrection>) triangle.getProperty(Correctable.CORRECTION_PROPERTY);
    }
    
    private List<Smoothing> getTriangleSmoothings() {
        return (List<Smoothing>) triangle.getProperty(Smoothable.SMOOTHING_PROPERTY);
    }
    
    private List<TriangleComment> getFactorComments() {
        return (List<TriangleComment>) estimate.getProperty(Commentable.COMMENT_PROPERTY);
    }
    
    private List<TriangleCorrection> getFactorCorrections() {
        return (List<TriangleCorrection>) estimate.getProperty(Correctable.CORRECTION_PROPERTY);
    }
    
    private List<Smoothing> getFactorSmoothings() {
        return (List<Smoothing>) estimate.getProperty(Smoothable.SMOOTHING_PROPERTY);
    }
    
    private List<TriangleExclusion> getFactorExclusions() {
        return (List<TriangleExclusion>) estimate.getProperty(Excludables.EXCLUSION_PROPERTY);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() == triangle) {
            trianglePropertyChange(evt);
        } else {
            factorPropertyChange(evt);
        }
    }
    
    private void trianglePropertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if(Triangle.GEOMETRY_PROPERTY.equals(property)) {
            factorInput.geometry = getTraingleGeometry();
            factorInput.calculateFactors();
        } else if(Correctable.CORRECTION_PROPERTY.equals(property)) {
            factorInput.triangleCorrections = getTriangleCorrections();
            factorInput.calculateFactors();
        } else if(Smoothable.SMOOTHING_PROPERTY.equals(property)) {
            factorInput.triangleSmoothings = getTriangleSmoothings();
            factorInput.calculateFactors();
        }
    }
    
    private void factorPropertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if(Correctable.CORRECTION_PROPERTY.equals(property)) {
            factorInput.factorCorrections = getFactorCorrections();
            factorInput.calculateFactors();
        } else if(Smoothable.SMOOTHING_PROPERTY.equals(property)) {
            factorInput.factorSmoothings = getFactorSmoothings();
            factorInput.calculateFactors();
        } else if(Commentable.COMMENT_PROPERTY.equals(property)) {
            factorInput.factorComments = getFactorComments();
            factorInput.applyComments();
        } else if(Excludables.EXCLUSION_PROPERTY.equals(property)) {
            factorInput.factorExclusions = getFactorExclusions();
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
                estimate.setProperty(Correctable.CORRECTION_PROPERTY, datas);
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
                factorInput.applyComments();
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
        private List<TriangleExclusion> factorExclusions;
        private List<TriangleComment> factorComments;
        
        FactorInput() {
            geometry = getTraingleGeometry();
            triangleCorrections = getTriangleCorrections();
            triangleSmoothings = getTriangleSmoothings();
            
            factorCorrections = getFactorCorrections();
            factorSmoothings = getFactorSmoothings();
            factorExclusions = getFactorExclusions();
            factorComments = getFactorComments();
        }
        
        void calculateFactors() {
            if(isComplete()) {
                widget.setManualEvents(true);
                TriangleCell[][] input = createInputCells();
                widget.setTriangleGeometry(createGeoemtry());
                widget.setValueLayer(TriangleCell.VALUE_LAYER, calculateFactors(input));
                widget.setValueLayer(TriangleCell.CORRECTION_LAYER, DataUtil.convertCorrections(factorCorrections));
                
                TriangleCell[][] cells = widget.getCellArray();
                applySmoothings(cells, factorSmoothings);
                applyExclusions(cells);
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
        
        private void applyExclusions(TriangleCell[][] cells) {
            for(TriangleExclusion exclusion : factorExclusions)
                applyExclusion(exclusion.getAccidentDate(), exclusion.getDevelopmentDate(), cells);
        }
        
        private void applyExclusion(Date accident, Date development, TriangleCell[][] cells) {
            for(TriangleCell[] row : cells)
                for(TriangleCell cell : row)
                    if(cell!=null && cell.acceptsDates(accident, development))
                        cell.setExcluded(true);
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
        
        private void applyComments() {
            List<WidgetData<Comment>> comments = DataUtil.convertComments(factorComments);
            widget.setComments(comments);
        }
    }
}
