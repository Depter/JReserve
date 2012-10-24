package org.jreserve.triangle.editor;

import java.io.Serializable;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.triangle.VectorProjectElement;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.guiutil.VectorFormatVisualPanel;
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
    
    private JToolBar toolBar = new JToolBar();
    private VectorProjectElement element;
    private MultiViewElementCallback callBack;
    private DataLoader<Vector> loader;
    
    public VectorDataEditorView(VectorProjectElement element) {
        this.element = element;
        super.addChangeListener(this);
        initGeometry();
        startLoader();
    }
    
    private void initGeometry() {
        VectorGeometry vectorGeometry = element.getValue().getGeometry();
        initBegin(vectorGeometry);
        initPeriods(vectorGeometry);
        initMonths(vectorGeometry);
    }
    
    private void initBegin(VectorGeometry geometry) {
        //geometrySetting.setSymmetricFromDate(true);
        //geometrySetting.setSymmetricFromDateEnabled(false);
        geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
    }
    
    private void initPeriods(VectorGeometry geometry) {
        //geometrySetting.setSymmetricPeriods(false);
        //geometrySetting.setSymmetricPeriodsEnabled(false);
        geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        //geometrySetting.setDevelopmentPeriodCount(1);
    }
    
    private void initMonths(VectorGeometry geometry) {
        //geometrySetting.setSymmetricMonths(false);
        //geometrySetting.setSymmetricMonthsEnabled(false);
        geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
        //geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInAccident());
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
        super.setDatas(datas);
    }
    
    private void setCorrections(List<Data<Vector, Double>> corrections) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        VectorGeometry triangleGeometry = super.getGeometry();
        if(triangleGeometry != null)
            element.setProperty(VectorProjectElement.GEOMETRY_PROPERTY, triangleGeometry);
    }
}
