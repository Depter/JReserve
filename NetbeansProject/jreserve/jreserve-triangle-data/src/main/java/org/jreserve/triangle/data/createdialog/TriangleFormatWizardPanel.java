package org.jreserve.triangle.data.createdialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.openide.WizardDescriptor;
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
    "LOG.TriangleFormatWizard.Created=Created triangle \"{0}\" for data type {1} - {2}"
})
class TriangleFormatWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(TriangleFormatWizardPanel.class.getName());
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    private final Object lock = new Object();
    private TriangleData triangleData;
    
    protected TriangleFormatVisualPanel panel;
    protected WizardDescriptor wizard;

    @Override
    public Component getComponent() {
        if (panel == null) {
            panel = new TriangleFormatVisualPanel();
            panel.addChangeListener(this);
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        List<ClaimValue> datas = (List<ClaimValue>) wizard.getProperty(NameSelectWizardPanel.PROP_DATA);
        ProjectDataType dt = (ProjectDataType) wizard.getProperty(NameSelectWizardPanel.PROP_DATA_TYPE);
        setFirstDate(datas);
        panel.triangle.addValueLayer(getWidgetData(datas));
        validate();
    }
    
    private void setFirstDate(List<ClaimValue> datas) {
        Date start = getFirstDate(datas);
        if(start != null)
            panel.geometrySetting.setStartDate(start);
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
    
    private List<WidgetData<Double>> getWidgetData(List<ClaimValue> datas) {
        List<ClaimValue> escaped = new ArrayList<ClaimValue>(datas);
        return DataUtil.convertDatas(escaped);
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