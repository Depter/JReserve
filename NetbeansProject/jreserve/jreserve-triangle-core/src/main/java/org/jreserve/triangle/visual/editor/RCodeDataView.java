package org.jreserve.triangle.visual.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.rutil.NavigableRCodePanel;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.ChangeableTriangularData;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.project.TriangleProjectElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RCodeDataView.Title=R - Code"
})
public class RCodeDataView extends NavigableRCodePanel {

    private DataListener dataListener = new DataListener();
    private NameListener nameListener = new NameListener();
    private TriangleProjectElement element;
    private TriangularData data;
    private RCode rCode;
    
    public RCodeDataView(TriangleProjectElement element) {
        super(Bundle.LBL_RCodeDataView_Title());
        this.element = element;
        this.element.addPropertyChangeListener(nameListener);
        
        data = element.getValue().getTriangularData();
        if(data instanceof ChangeableTriangularData)
            ((ChangeableTriangularData)data).addChangeListener(dataListener);
        this.rCode = super.getRCode();
        createCode();
    }
    
    private void createCode() {
        rCode.clear();
        String name = getElementName();
        data.createRTriangle(name, rCode);
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
        if(data instanceof ChangeableTriangularData)
            ((ChangeableTriangularData)data).removeChangeListener(dataListener);
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
