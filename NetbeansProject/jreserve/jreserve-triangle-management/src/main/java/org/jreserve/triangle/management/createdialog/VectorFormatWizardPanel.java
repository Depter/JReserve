package org.jreserve.triangle.management.createdialog;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.management.VectorProjectElement;
import org.jreserve.triangle.management.guiutil.DataFormatWizardPanel;
import org.jreserve.triangle.management.guiutil.NameSelectWizardPanel;
import org.jreserve.triangle.management.guiutil.TriangleFormatVisualPanel;
import org.jreserve.triangle.management.guiutil.VectorFormatVisualPanel;
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
    protected TriangleFormatVisualPanel createPanel() {
        return new VectorFormatVisualPanel();
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
            try {
                Vector vector = SessionTask.withOpenSession(new VectorCreator());
                addProjectElement(vector);
                clearProperties();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to save vector", ex);
                String msg = Bundle.MSG_VectorFormatWizardPanel_SaveError();
                throw new WizardValidationException(panel, msg, msg);
            }
        }
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
    
    private class VectorCreator extends SessionTask.AbstractTask<Vector> {

        @Override
        public void doWork(Session session) throws Exception {
            Project project = (Project) session.get(Project.class, vectorData.project.getId());
            Vector vector = createVector(project);
            session.persist(vector);
            result = vector;
        }
    
        private Vector createVector(Project project) {
            Vector vector = new Vector(project, vectorData.dataType, vectorData.name);
            vector.setDescription(vectorData.description);
            vector.setGeometry(vectorData.geometry);
            return vector;
        }
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
            TriangleGeometry triangle = panel.getTriangleWidget().getTriangleGeometry();
            Date start = triangle.getAccidentStart();
            int periods = triangle.getAccidentPeriods();
            int months = triangle.getMonthInAccident();
            this.geometry = new VectorGeometry(start, periods, months);
        }
        
    }
}