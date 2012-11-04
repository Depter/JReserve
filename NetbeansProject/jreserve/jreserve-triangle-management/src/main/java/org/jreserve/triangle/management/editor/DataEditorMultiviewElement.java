package org.jreserve.triangle.management.editor;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.DataStructure;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.MultiViewFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class DataEditorMultiviewElement<T extends DataStructure> extends DataEditorView<T> implements MultiViewElement {
    
    protected JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callBack;
    
    DataEditorMultiviewElement(ProjectElement<T> element) {
        super(element);
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
    public void componentClosed() {
        super.closed();
    }

    @Override public void componentOpened() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callBack = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return MultiViewFactory.createUnsafeCloseState("not-saved", null, null);
        //return CloseOperationState.STATE_OK;
    }
}
