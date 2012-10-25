package org.jreserve.triangle.editor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.guiutil.TriangleFormatVisualPanel;
import org.jreserve.triangle.mvc.layer.DoubleLayer;
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
public class TriangleDataEditorView extends TriangleFormatVisualPanel implements MultiViewElement, Serializable, ChangeListener, DataLoader.Callback<Triangle> {
    
    private final static int CORRECTION_LAYER = 0;
    private final static int VALUE_LAYER = 1;
    
    private JToolBar toolBar = new JToolBar();
    private TriangleProjectElement element;
    private MultiViewElementCallback callBack;
    private DataLoader<Triangle> loader;
    
    public TriangleDataEditorView(TriangleProjectElement element) {
        this.element = element;
        super.addChangeListener(this);
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
        triangle.addLayer(new DoubleLayer(true, true));
        triangle.addLayer(new DoubleLayer(false, true));
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
        return UndoRedo.NONE;
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
            setCorrections(loader.getCorrections());
        } catch (RuntimeException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void setData(List<Data<ProjectDataType, Double>> datas) {
        triangle.getLayerAt(VALUE_LAYER).setData(datas);
    }
    
    private void setCorrections(List<Data<Triangle, Double>> corrections) {
        triangle.getLayerAt(CORRECTION_LAYER).setData(corrections);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TriangleGeometry triangleGeometry = geometrySetting.getGeometry();
        if(triangleGeometry != null)
            element.setProperty(TriangleProjectElement.GEOMETRY_PROPERTY, triangleGeometry);
    }
}
