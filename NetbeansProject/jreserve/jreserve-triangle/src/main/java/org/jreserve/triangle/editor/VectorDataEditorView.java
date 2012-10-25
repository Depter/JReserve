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
import org.jreserve.triangle.VectorProjectElement;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.guiutil.VectorFormatVisualPanel;
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
 */
public class VectorDataEditorView extends VectorFormatVisualPanel implements MultiViewElement, Serializable, ChangeListener, DataLoader.Callback<Vector> {
    
    private final static int CORRECTION_LAYER = 0;
    private final static int VALUE_LAYER = 1;
    
    private JToolBar toolBar = new JToolBar();
    private VectorProjectElement element;
    private MultiViewElementCallback callBack;
    private DataLoader<Vector> loader;
    
    public VectorDataEditorView(VectorProjectElement element) {
        this.element = element;
        super.addChangeListener(this);
        initGeometry();
        initLayers();
        startLoader();
    }
    
    private void initGeometry() {
        VectorGeometry vectorGeometry = element.getValue().getGeometry();
        initBegin(vectorGeometry);
        initPeriods(vectorGeometry);
        initMonths(vectorGeometry);
    }
    
    private void initBegin(VectorGeometry geometry) {
        geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
    }
    
    private void initPeriods(VectorGeometry geometry) {
        geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
    }
    
    private void initMonths(VectorGeometry geometry) {
        geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
    }
    
    private void initLayers() {
        triangle.addLayer(new DoubleLayer(true, true));
        triangle.addLayer(new DoubleLayer(false, true));
    }
    
    private void startLoader() {
        loader = new DataLoader<Vector>(element.getValue(), this);
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
    
    private void setCorrections(List<Data<Vector, Double>> corrections) {
        triangle.getLayerAt(CORRECTION_LAYER).setData(corrections);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        VectorGeometry triangleGeometry = getGeometry();
        if(triangleGeometry != null)
            element.setProperty(VectorProjectElement.GEOMETRY_PROPERTY, triangleGeometry);
    }
    
    private VectorGeometry getGeometry() {
        TriangleGeometry geometry = geometrySetting.getGeometry();
        if(geometry == null)
            return null;
        return getGeometry(geometry);
    }
    
    private VectorGeometry getGeometry(TriangleGeometry geometry) {
        Date start = geometry.getAccidentStart();
        int periods = geometry.getAccidentPeriods();
        int months = geometry.getMonthInAccident();
        return new VectorGeometry(start, periods, months);
    }
}
