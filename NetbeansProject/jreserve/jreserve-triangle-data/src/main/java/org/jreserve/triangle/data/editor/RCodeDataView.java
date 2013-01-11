package org.jreserve.triangle.data.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.rutil.NavigableRCodePanel;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.data.project.TriangleProjectElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RCodeDataView.Title=R - Code"
})
class RCodeDataView extends NavigableRCodePanel {

    private DataListener dataListener = new DataListener();
    private NameListener nameListener = new NameListener();
    private TriangleProjectElement element;
    private RCode rCode;
    
    public RCodeDataView(TriangleProjectElement element) {
        super(Bundle.LBL_RCodeDataView_Title());
        this.element = element;
        this.element.addPropertyChangeListener(nameListener);
        this.element.getTriangularData().addChangeListener(dataListener);
        this.rCode = super.getRCode();
        createCode();
    }
    
    private void createCode() {
        rCode.clear();
        String name = getElementName();
        element.getTriangularData().createTriangle(name, rCode);
        rCode.fireChangeEvent();
    }
    
    private String getElementName() {
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        if(name == null)
            return "";
        return name.replace(' ', '.');
    }

    @Override
    public void parentClosed() {
        super.parentClosed();
        element.removePropertyChangeListener(nameListener);
        element.getTriangularData().removeChangeListener(dataListener);
    }
    
    private class DataListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            createCode();
        }
    }
    
    private class NameListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(ProjectElement.NAME_PROPERTY.equals(evt.getPropertyName()))
                createCode();
        }
    }
}
