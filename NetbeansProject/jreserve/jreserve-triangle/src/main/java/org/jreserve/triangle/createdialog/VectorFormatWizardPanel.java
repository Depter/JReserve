package org.jreserve.triangle.createdialog;

import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.VectorProjectElement;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.guiutil.DataFormatVisualPanel;
import org.jreserve.triangle.guiutil.DataFormatWizardPanel;
import org.jreserve.triangle.guiutil.NameSelectWizardPanel;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.VectorFormatWizardPanel.SaveError=Unable to save vector!",
    "# {0} - vector name",
    "# {1} - db id",
    "# {2} - db name",
    "LOG.VectorFormatWizardPanel.Created=Created vector \"{0}\" for data type {1} - {2}"
})
class VectorFormatWizardPanel extends DataFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(VectorFormatWizardPanel.class.getName());
    
    private final Object lock = new Object();
    private VectorData vectorData;
    
    @Override
    protected DataFormatVisualPanel createPanel() {
        return new VisualPanel();
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
            logCreation();
            clearProperties();
        }
    }
    
    private Vector saveVector() throws WizardValidationException {
        Session session = SessionFactory.beginTransaction();
        try {
            Project project = session.find(Project.class, vectorData.project.getId());
            Vector vector = createVector(project);
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
    
    private Vector createVector(Project project) {
        Vector vector = new Vector(project, vectorData.dataType, vectorData.name);
        vector.setDescription(vectorData.description);
        vector.setGeometry(vectorData.geometry);
        return vector;
    }
    
    private void addProjectElement(final Vector vector) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    VectorProjectElement element = new VectorProjectElement(vector);
                    getContainer().addElement(element);
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
    
    private void logCreation() {
        ProjectDataType dt = vectorData.dataType;
        String msg = Bundle.LOG_VectorFormatWizardPanel_Created(vectorData.name, dt.getDbId(), dt.getName());
        ChangeLogUtil.getDefault().addChange(vectorData.project, ChangeLog.Type.DATA, msg);
        ChangeLogUtil.getDefault().saveValues(vectorData.project);
    }
    
    private class VectorData {
        private ProjectElement element;
        private Project project;
        private ProjectDataType dataType;
        private String name;
        private String description;
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
            description = (String) wizard.getProperty(NameSelectWizardPanel.PROP_DATA_DESCRIPTION);
        }
        
        private void readPanel() {
            TriangleGeometry triangle = panel.getGeometry();
            Date start = triangle.getAccidentStart();
            int periods = triangle.getAccidentPeriods();
            int months = triangle.getMonthInAccident();
            this.geometry = new VectorGeometry(start, periods, months);
        }
        
    }
    
    private static class VisualPanel extends DataFormatVisualPanel {
        
        private Date devFromDate = null;
        
        @Override
        protected void componentsInitialized() {
            geometrySetting.setSymmetricPeriods(false);
            geometrySetting.setSymmetricPeriodsEnabled(false);
            geometrySetting.setSymmetricMonths(false);
            geometrySetting.setSymmetricMonthsEnabled(false);
            geometrySetting.setDevelopmentPeriodCount(1);
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            super.propertyChange(evt);
            if(developmentFromChanged())
                geometrySetting.setDevelopmentMonthsPerStep(getDevelopmentMonthCount());
        }
        
        private boolean developmentFromChanged() {
            Date from = geometrySetting.getDevelopmentStartDate();
            if(devFromDate == null)
                return setNewDevFromDate(from);
            return setDevFromDate(from);
        }
        
        private boolean setNewDevFromDate(Date from) {
            if(from == null)
                return false;
            devFromDate = from;
            return true;
        }
        
        private boolean setDevFromDate(Date from) {
            if(devFromDate.equals(from))
                return false;
            devFromDate = from;
            return true;
        }
        
        private int getDevelopmentMonthCount() {
            Calendar c = Calendar.getInstance();
            c.setTime(devFromDate);
            int count = 1;
            while(c.getTime().before(developmentEnd)) {
                c.add(Calendar.MONTH, 1);
                count++;
            }
            return count;
        }
    }
}