package org.jreserve.triangle.visual.createdialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.triangle.util.ProjectDataContainerFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.NameSelectWizardPanel.NoProject=Project not selected!",
    "MSG.NameSelectWizardPanel.NoDataType=Data type not selected!",
    "MSG.NameSelectWizardPanel.NoName=Field 'Name' is empty!",
    "MSG.NameSelectWizardPanel.NameExists=Name already exists!",
    "MSG.NameSelectWizardPanel.LoadError=Unable to load data!",
    "MSG.NameSelectWizardPanel.NoData=There is no avaiable data!"
})
class NameSelectWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private final static Logger logger = Logger.getLogger(NameSelectWizardPanel.class.getName());
    
    public final static String PROP_DATA_NAME = "DATA_NAME_PROPERTY";
    public final static String PROP_DATA_DESCRIPTION = "DATA_DESCRIPTION_PROPERTY";
    public final static String PROP_DATA_TYPE = "DATA_TYPE_PROPERTY";
    public final static String PROP_PROJECT = "PROJECT_PROPERTY";
    public final static String PROP_PROJECT_ELEMENT = "PROJECT_ELEMENT_PROPERTY";
    public final static String PROP_DATA = "DATA_PROPERTY";
    
    private boolean isTrinagle;
    
    private NameSelectVisualPanel component;
    private InputValidator validator;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    
    private volatile DataCriteria criteria;
    
    NameSelectWizardPanel(boolean isTriangle) {
        this.isTrinagle = isTriangle;
    }
    
    @Override
    public Component getComponent() {
        if(component == null)
            createComponent();
        return component;
    }

    private void createComponent() {
        component = new NameSelectVisualPanel(isTrinagle);
        validator = new InputValidator();
        component.addChangeListener(validator);
    }
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        if(setProject(lkp) || setClaimType(lkp) || setLoB(lkp))
            setDataType();
        setDataName();
        setDataDescription();
        validator.validatePanel();
    }
    
    private boolean setProject(Lookup lookup) {
        Project project = lookup.lookup(Project.class);
        if(project != null) {
            component.setProject(project);
            return true;
        }
        return false;
    }
    
    private boolean setClaimType(Lookup lookup) {
        ClaimType ct = lookup.lookup(ClaimType.class);
        if(ct != null) {
            component.setClaimType(ct);
            return true;
        }
        return false;
    }
    
    private boolean setLoB(Lookup lookup) {
        LoB lob = lookup.lookup(LoB.class);
        if(lob != null) {
            component.setLoB(lob);
            return true;
        }
        return false;
    }
    
    private void setDataType() {
        ProjectDataType dataType = (ProjectDataType) wizard.getProperty(PROP_DATA_TYPE);
        if(dataType != null && exists(dataType))
            component.setDataType(dataType);
        else
            component.setDataType(null);
    }
    
    private boolean exists(ProjectDataType dataType) {
        ProjectElement project = component.getProjectElement();
        if(project == null)
            return false;
        return project.getChildValues(ProjectDataType.class).contains(dataType);
    }
    
    private void setDataName() {
        String name = (String) wizard.getProperty(PROP_DATA_NAME);
        component.setDataName(name);
    }
    
    private void setDataDescription() {
        String description = (String) wizard.getProperty(PROP_DATA_DESCRIPTION);
        component.setDescription(description);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        if(!listeners.contains(cl))
            listeners.add(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public void prepareValidation() {
        wizard.putProperty(PROP_DATA_NAME, component.getDataName());
        wizard.putProperty(PROP_DATA_TYPE, component.getDataType());
        wizard.putProperty(PROP_PROJECT, component.getProject());
        wizard.putProperty(PROP_PROJECT_ELEMENT, component.getProjectElement());
        wizard.putProperty(PROP_DATA_DESCRIPTION, component.getDescription());
        criteria = new DataCriteria(component.getDataType());
        component.showProgressBar();
    }

    @Override
    public void validate() throws WizardValidationException {
        List<ClaimValue> datas = loadDatas();
        setDatas(datas);
    }
    
    private List<ClaimValue> loadDatas() throws WizardValidationException {
        DataSource ds = new DataSource();
        try {
            ds.open();
            return ds.getClaimData(criteria);
        } catch (RuntimeException ex) {
            String msg = String.format("Unable to load data for query: %s", criteria);
            logger.log(Level.SEVERE, msg, ex);
            stopProgressBar();
            throw new WizardValidationException(component, msg, Bundle.MSG_NameSelectWizardPanel_LoadError());
        } finally {
            ds.rollBack();
        }
    }
    
    private void stopProgressBar() throws WizardValidationException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.hideProgressBar();
            }
        });
    }
    
    private void setDatas(List<ClaimValue> datas) throws WizardValidationException {
        if(datas.isEmpty()) {
            stopProgressBar();
            String msg = Bundle.MSG_NameSelectWizardPanel_NoData();
            throw new WizardValidationException(component, msg, msg);
        } else {
            synchronized(wizard) {
                wizard.putProperty(PROP_DATA, datas);
            }
            stopProgressBar();
        }
    }
    
    private class InputValidator implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            validatePanel();
            fireChange();
        }

        private void validatePanel() {
            isValid = checkProject() && checkDataType() && checkName();
            if(isValid)
                showError(null);
        }

        private void showError(String msg) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
        }

        private boolean checkProject() {
            Project project = component.getProject();
            if(project == null) {
                showError(Bundle.MSG_NameSelectWizardPanel_NoProject());
                return false;
            }
            return true;
        }

        private boolean checkDataType() {
            ProjectDataType dataType = component.getDataType();
            if(dataType == null) {
                showError(Bundle.MSG_NameSelectWizardPanel_NoDataType());
                return false;
            }
            return true;
        }

        private boolean checkName() {
            String name = component.getDataName();
            return checkNameNotEmpty(name) && checkNewName(name); 
        }

        private boolean checkNameNotEmpty(String name) {
            if(name == null || name.trim().length() == 0) {
                showError(Bundle.MSG_NameSelectWizardPanel_NoName());
                return false;
            }
            return true;
        }

        private boolean checkNewName(String name) {
            ProjectElementContainer container = getDataContainer();
            if(container.containsName(name)) {
                showError(Bundle.MSG_NameSelectWizardPanel_NameExists());
                return false;
            }
            return true;
        }

        private ProjectElementContainer getDataContainer() {
            ProjectElement element = component.getProjectElement();
            element = element.getFirstChild(ProjectDataContainerFactory.POSITION, ProjectElementContainer.class);
            return (ProjectElementContainer) element.getValue();
        }
    }
}
