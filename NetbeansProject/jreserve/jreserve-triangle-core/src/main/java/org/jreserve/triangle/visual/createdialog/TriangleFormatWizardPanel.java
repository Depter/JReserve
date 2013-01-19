package org.jreserve.triangle.visual.createdialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.project.TriangleProjectElement;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
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
    "LOG.TriangleFormatWizard.Created=Created triangle \"{0}\" for data type {1} - {2}",
    "MSG.TriangleFormatWizardPanel.NoData=There is no data selected!"
})
class TriangleFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(TriangleFormatWizardPanel.class.getName());
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private final boolean isTriangle;
    private boolean isValid = false;
    private final Object lock = new Object();
    private TriangleData triangleData;
    private TriangleValidator validator;
    
    protected TriangleFormatVisualPanel panel;
    protected WizardDescriptor wizard;

    TriangleFormatWizardPanel(boolean isTriangle) {
        this.isTriangle = isTriangle;
    }
    
    @Override
    public Component getComponent() {
        if (panel == null)
            createPanel();
        return panel;
    }
    
    private void createPanel() {
        panel = new TriangleFormatVisualPanel(isTriangle);
        validator = new TriangleValidator();
        panel.addChangeListener(validator);
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        List<ClaimValue> datas = (List<ClaimValue>) wizard.getProperty(NameSelectWizardPanel.PROP_DATA);
        initPanel(datas);
        validator.validateState();
    }
    
    private void initPanel(List<ClaimValue> datas) {
        setFirstDate(datas);
        panel.setClaims(datas);
    }
    
    private void setFirstDate(List<ClaimValue> datas) {
        Date start = getFirstDate(datas);
        if(start != null)
            panel.setStartDate(start);
    }
    
    private Date getFirstDate(List<ClaimValue> datas) {
        Date first = null;
        for(ClaimValue data : datas) {
            Date date = data.getAccidentDate();
            if(first==null || date.before(first))
                first = date;
        }
        return first;
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
    
    private ProjectElementContainer getContainer() {
        ProjectElement element = triangleData.element;
        Object v = element.getFirstChildValue(ProjectElementContainer.class);
        return (ProjectElementContainer) v;
    }
    
    protected void clearProperties() {
        synchronized(wizard) {
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_NAME, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_TYPE, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_DESCRIPTION, null);
        }
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
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for (ChangeListener listener : new ArrayList<ChangeListener>(listeners)) {
            listener.stateChanged(evt);
        }
    }
    
    private class TriangleValidator implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            validateState();
            fireChange();
        }

        private void validateState() {
            isValid = validateInput() && validateTable();
            if (isValid) {
                showError(null);
            }
        }

        private boolean validateInput() {
            if (panel.isInputValid()) {
                return true;
            }
            showError(panel.getErrorMsg());
            return false;
        }

        private boolean validateTable() {
            double[][] values = panel.getTriangleValues();
            if (values == null || !validTable(values)) {
                showError(Bundle.MSG_TriangleFormatWizardPanel_NoData());
                return false;
            }
            return true;
        }

        private boolean validTable(double[][] values) {
            for (double[] row : values)
                if (validRow(row))
                    return true;
            return false;
        }

        private boolean validRow(double[] row) {
            for (double value : row)
                if(!Double.isNaN(value))
                    return true;
            return false;
        }

        private void showError(String msg) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
        }
    
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
            Triangle triangle = new Triangle(project, triangleData.dataType, triangleData.name, isTriangle);
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
            geometry = panel.getTriangleGeometry();
        }
        
    }
}
