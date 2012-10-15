package org.jreserve.triangle.createdialog;

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
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.importutil.DataFormatVisualPanel2;
import org.jreserve.triangle.importutil.DataFormatWizardPanel;
import org.jreserve.triangle.importutil.NameSelectWizardPanel;
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
class TriangleFormatWizard extends DataFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(TriangleFormatWizard.class.getName());
    
    private final Object lock = new Object();
    private TriangleData triangleData;

    @Override
    protected DataFormatVisualPanel2 createPanel() {
        return new VisualPanel();
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
            Triangle triangle = saveTriangle();
            addProjectElement(triangle);
            logCreation();
        }
    }
    
    private Triangle saveTriangle() throws WizardValidationException {
        Session session = SessionFactory.beginTransaction();
        try {
            Project project = session.find(Project.class, triangleData.project.getId());
            Triangle triangle = new Triangle(project, triangleData.dataType, triangleData.name);
            triangle.setGeometry(triangleData.geometry);
            session.persist(triangle);
            session.comitTransaction();
            return triangle;
        } catch (Exception ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to save triangle", ex);
            String msg = Bundle.MSG_TriangleFormatWizardPanel_SaveError();
            throw new WizardValidationException(panel, msg, msg);
        }
    }
    
    private void addProjectElement(final Triangle triangle) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    getContainer().addTriangle(triangle);
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
    
    private void logCreation() {
        ProjectDataType dt = triangleData.dataType;
        String msg = Bundle.LOG_TriangleFormatWizard_Created(triangleData.name, dt.getDbId(), dt.getName());
        ChangeLogUtil.getDefault().addChange(triangleData.project, ChangeLog.Type.DATA, msg);
        ChangeLogUtil.getDefault().saveValues(triangleData.project);
    }
    
    private class TriangleData {
        private ProjectElement element;
        private Project project;
        private ProjectDataType dataType;
        private String name;
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
        }
        
        private void readPanel() {
            geometry = panel.getGeometry();
        }
        
    }

    private static class VisualPanel extends DataFormatVisualPanel2 {    
    
        
    }
}
