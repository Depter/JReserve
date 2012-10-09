package org.jreserve.triangle.entities.vectorimport;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.data.model.DataRow;
import org.jreserve.data.model.DataTable;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.importutil.NameSelectWizardPanel;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.VectorFormatWizardPanel.NoData=There is no data selected!",
    "MSG.VectorFormatWizardPanel.SaveError=Unable to save vector!"
})
public class VectorFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor>, ChangeListener {
    
    private final static Logger logger = Logger.getLogger(VectorFormatWizardPanel.class.getName());
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private VectorFormatVisualPanel panel;
    private boolean isValid = false;
    private WizardDescriptor wizard;
    
    private final Object lock = new Object();
    private VectorData vectorData;
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new VectorFormatVisualPanel();
            panel.addChangeListener(this);
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        this.wizard = data;
        panel.readSettings(data);
        validatePanel();
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        validatePanel();
        fireChange();
    }
    
    private void validatePanel() {
        DataTable table = panel.getTable();
        isValid = checkTable(table);
        if(isValid)
            showError(null);
    }
    
    private boolean checkTable(DataTable table) {
        if(table == null || !validTable(table)) {
            showError(Bundle.LBL_VectorFormatWizardPanel_NoData());
            return false;
        }
        return true;
    }
    
    private boolean validTable(DataTable table) {
        for(int r=0, count=table.getRowCount(); r<count; r++)
            if(validRow(table.getRow(r)))
                return true;
        return false;
    }
    
    private boolean validRow(DataRow row) {
        for(int c=0, count=row.getCellCount(); c<count; c++)
            if(!Double.isNaN(row.getCell(c).getValue()))
                return true;
        return false;
    }
    
    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    @Override
    public void prepareValidation() { 
        synchronized(lock) {
            vectorData = new VectorData();
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        synchronized(lock) {
            Vector vector = saveVector();
            addProjectElement(vector);
        }
    }
    
    private Vector saveVector() throws WizardValidationException {
        Session session = SessionFactory.beginTransaction();
        try {
            Project project = session.find(Project.class, vectorData.project.getId());
            Vector vector = new Vector(project, vectorData.dataType, vectorData.name);
            vector.setGeometry(vectorData.geometry);
            session.persist(vector);
            session.comitTransaction();
            return vector;
        } catch (Exception ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to save vector", ex);
            String msg = Bundle.MSG_VectorFormatWizardPanel_SaveError();
            throw new WizardValidationException(panel, msg, msg);
        }
    }
    
    private void addProjectElement(final Vector vector) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    getContainer().addVector(vector);
                } catch (RuntimeException ex) {
                    logger.log(Level.SEVERE, "Unable to create element!", ex);
                }
            }
        });
    }
    
    private ProjectDataContainer getContainer() {
        ProjectElement element = vectorData.element;
        Object v = element.getFirstChildValue(ProjectDataContainer.class);
        return (ProjectDataContainer) v;
    }
    
    private class VectorData {
        private ProjectElement element;
        private Project project;
        private ProjectDataType dataType;
        private String name;
        private VectorGeometry geometry;
        
        private VectorData() {
            readWizard();
            readPanel();
        }
        
        private void readWizard() {
            element = (ProjectElement) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT);
            project = (Project) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT);
            dataType = (ProjectDataType) wizard.getProperty(NameSelectWizardPanel.PROP_DATA_TYPE);
            name = (String) wizard.getProperty(NameSelectWizardPanel.PROP_DATA_NAME);
        }
        
        private void readPanel() {
            Date start = panel.getStartDate();
            int periods = panel.getPeriodCount();
            int months = panel.getMonthsPerStep();
            geometry = new VectorGeometry(start, periods, months);
        }
        
    }
}
