package org.jreserve.triangle.dataimport;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.importutil.DataFormatVisualPanel;
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
    "MSG.TriangleFormatWizardPanel.SaveError=Unable to save triangle!"
})
class TriangleFormatWizard extends DataFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(TriangleFormatWizard.class.getName());
    
    private final Object lock = new Object();
    private TriangleData triangleData;

    @Override
    protected DataFormatVisualPanel createPanel() {
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
            Date accidentStart = panel.getAccidentStartDate();
            int accidentPeriods = panel.getAccidentPeriodCount();
            int accidentMonths = panel.getAccidentMonthsPerStep();

            Date developmentStart = panel.getDevelopmentStartDate();
            int developmentPeriods = panel.getDevelopmentPeriodCount();
            int developmentMonths = panel.getDevelopmentMonthsPerStep();
            
            geometry = new TriangleGeometry(accidentStart, accidentPeriods, accidentMonths, 
                    developmentStart, developmentPeriods, developmentMonths);
        }
        
    }

    private static class VisualPanel extends DataFormatVisualPanel {    
    
        
    }
}
