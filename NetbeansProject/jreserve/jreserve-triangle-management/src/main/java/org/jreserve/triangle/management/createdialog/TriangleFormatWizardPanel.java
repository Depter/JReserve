package org.jreserve.triangle.management.createdialog;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.management.guiutil.DataFormatWizardPanel;
import org.jreserve.triangle.management.guiutil.NameSelectWizardPanel;
import org.jreserve.triangle.management.guiutil.TriangleFormatVisualPanel;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TriangleFormatWizardPanel.SaveError=Unable to save triangle!",
    "# {0} - triangle name",
    "# {1} - db id",
    "# {2} - db name",
    "LOG.TriangleFormatWizard.Created=Created triangle \"{0}\" for data type {1} - {2}"
})
class TriangleFormatWizardPanel extends DataFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(TriangleFormatWizardPanel.class.getName());
    
    private final Object lock = new Object();
    private TriangleData triangleData;

    @Override
    protected TriangleFormatVisualPanel createPanel() {
        return new TriangleFormatVisualPanel();
    }

    @Override
    public void prepareValidation() {
        synchronized(lock) {
            triangleData = new TriangleData();
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        synchronized(lock) {
            try {
                Triangle triangle = SessionTask.withOpenSession(new TriangleCreator());
                addProjectElement(triangle);
                clearProperties();
            } catch (Exception ex) {
               logger.log(Level.SEVERE, "Unable to save triangle", ex);
                String msg = Bundle.MSG_TriangleFormatWizardPanel_SaveError();
                throw new WizardValidationException(panel, msg, msg);
            }
        }
    }
    
    private void addProjectElement(final Triangle triangle) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    TriangleProjectElement element = new TriangleProjectElement(triangle);
                    getContainer().addElement(element);
                } catch (RuntimeException ex) {
                    logger.log(Level.SEVERE, "Unable to create element!", ex);
                }
            }
        });
    }
    
    private ProjectDataContainer getContainer() {
        ProjectElement element = triangleData.element;
        Object v = element.getFirstChildValue(ProjectDataContainer.class);
        return (ProjectDataContainer) v;
    }
    
    private class TriangleCreator extends SessionTask.AbstractTask<Triangle> {

        @Override
        public void doWork(Session session) throws Exception {
            Project project = (Project) session.get(Project.class, triangleData.project.getId());
            Triangle triangle = createTriangle(project);
            session.persist(triangle);
            result = triangle;
        }
    
        private Triangle createTriangle(Project project) {
            Triangle triangle = new Triangle(project, triangleData.dataType, triangleData.name);
            triangle.setDescription(triangleData.description);
            triangle.setGeometry(triangleData.geometry);
            return triangle;
        }
    }
    
    private class TriangleData {
        private ProjectElement element;
        private Project project;
        private ProjectDataType dataType;
        private String name;
        private String description;
        private TriangleGeometry geometry;
        
        private TriangleData() {
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
            geometry = panel.getTriangleWidget().getTriangleGeometry();
        }
        
    }
}
