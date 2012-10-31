package org.jreserve.triangle.editor;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.guiutil.TriangleFormatVisualPanel;
import org.jreserve.triangle.widget.TriangleWidget.TriangleWidgetListener;
import org.jreserve.triangle.widget.data.TriangleCell;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleDataEditorView extends TriangleFormatVisualPanel implements MultiViewElement, Serializable, ChangeListener, TriangleWidgetListener, DataLoader.Callback<Triangle> {
    
    private final static int VALUE_LAYER = 0;
    private final static int CORRECTION_LAYER = 1;
    private final static Color CORRECTION_BG = new Color(235, 204, 204);
    
    private JToolBar toolBar = new JToolBar();
    private TriangleProjectElement element;
    private MultiViewElementCallback callBack;
    private DataLoader<Triangle> loader;
    
    public TriangleDataEditorView(TriangleProjectElement element) {
        this.element = element;
        super.addChangeListener(this);
        super.triangle.setEditableLayer(CORRECTION_LAYER);
        super.triangle.addTriangleWidgetListener(this);
        initGeometry();
        initLayers();
        startLoader();
    }
    
    private void initGeometry() {
        TriangleGeometry triangleGeometry = element.getValue().getGeometry();
        initBegin(triangleGeometry);
        initPeriods(triangleGeometry);
        initMonths(triangleGeometry);
    }
    
    private void initBegin(TriangleGeometry geometry) {
        if(isBeginSymmetric(geometry)) {
            geometrySetting.setSymmetricFromDate(true);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
        } else {
            geometrySetting.setSymmetricFromDate(false);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
            geometrySetting.setDevelopmentStartDate(geometry.getDevelopmentStart());
        }
    }
    
    private boolean isBeginSymmetric(TriangleGeometry geometry) {
        Date aBegin = geometry.getAccidentStart();
        Date dBegin = geometry.getDevelopmentStart();
        return aBegin.equals(dBegin);
    }
    
    private void initPeriods(TriangleGeometry geometry) {
        if(isPeriodSymmetric(geometry)) {
            geometrySetting.setSymmetricPeriods(true);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        } else {
            geometrySetting.setSymmetricPeriods(false);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
            geometrySetting.setDevelopmentPeriodCount(geometry.getDevelopmentPeriods());
        }
    }
    
    private boolean isPeriodSymmetric(TriangleGeometry geometry) {
        int aPeriod = geometry.getAccidentPeriods();
        int dPeriod = geometry.getDevelopmentPeriods();
        return aPeriod == dPeriod;
    }
    
    private void initMonths(TriangleGeometry geometry) {
        if(isMonthsSymmetric(geometry)) {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
        } else {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
            geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInDevelopment());
        }
    }
    
    private boolean isMonthsSymmetric(TriangleGeometry geometry) {
        int aMonth = geometry.getMonthInAccident();
        int dMonth = geometry.getMonthInDevelopment();
        return aMonth == dMonth;
    }
    
    private void initLayers() {
        triangle.addValueLayer(new ArrayList<Data<ProjectDataType, Double>>());
        triangle.addValueLayer(new ArrayList<Data<Triangle, Double>>());
        triangle.setLayerBackground(CORRECTION_LAYER, CORRECTION_BG);
    }
    
    private void startLoader() {
        loader = new DataLoader<Triangle>(element.getValue(), this);
        loader.start();
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }
    
    @Override 
    public void componentClosed() {
        if(loader != null) {
            loader.cancel();
            loader = null;
        }
    }

    @Override public void componentOpened() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        return ur!=null? ur : UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callBack = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public void finnished(DataLoader loader) {
        try {
            setData(loader.getData());
            setCorrections(getCorrectionData());
        } catch (RuntimeException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
        
    private List<Data<Triangle, Double>> getCorrectionData() {
        List<Data<Triangle, Double>> datas = new ArrayList<Data<Triangle, Double>>();
        for(TriangleCorrection tc : element.getValue().getCorrections())
            datas.add(tc.toData());
        return datas;
    }
    
    private void setData(List<Data<Triangle, Double>> datas) {
        triangle.setValueLayer(VALUE_LAYER, datas);
    }
    
    private void setCorrections(List<Data<Triangle, Double>> corrections) {
        triangle.setValueLayer(CORRECTION_LAYER, corrections);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TriangleGeometry triangleGeometry = geometrySetting.getGeometry();
        if(triangleGeometry != null)
            element.setProperty(TriangleProjectElement.GEOMETRY_PROPERTY, triangleGeometry);
    }

    @Override
    public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
        if(layer==CORRECTION_LAYER && !equals(oldValue, newValue))
            updateCorrections();
    }
    
    private boolean equals(Double d1, Double d2) {
        if(d1 == null) return d2 == null;
        if(d2 == null) return false;
        return d1.equals(d2);
    }
    
    private void updateCorrections() {
        List<Data<Triangle, Double>> datas = triangle.getLayer(element.getValue(), CORRECTION_LAYER);
        List<TriangleCorrection> corrections = getCorrections(datas);
        element.setProperty(TriangleProjectElement.CORRECTION_PROPERTY, corrections);
    }
    
    private List<TriangleCorrection> getCorrections(List<Data<Triangle, Double>> datas) {
        List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>(datas.size());
        for(Data<Triangle, Double> data : datas)
            corrections.add(getCorrection(data));
        return corrections;
    }
    
    private TriangleCorrection getCorrection(Data<Triangle, Double> data) {
        TriangleCorrection tc = new TriangleCorrection(data.getOwner(), data.getAccidentDate(), data.getDevelopmentDate());
        tc.setCorrection(data.getValue());
        return tc;
    }
}
