package org.jreserve.estimates.chainladder.creator;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.jreserve.estimates.EstimateContainerFactory;
import org.jreserve.estimates.chainladder.ChainLadderEstimate;
import org.jreserve.estimates.chainladder.ChainLadderEstimateProjectElement;
import org.jreserve.estimates.visual.NameSelectWizardPanel;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.triangle.entities.Triangle;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TriangleSelectWizardPanel.Triangle.Empty=Triangle not selected!"
})
public class TriangleSelectWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, ChangeListener  {
    
    private final static Logger logger = Logger.getLogger(TriangleSelectWizardPanel.class.getName());
    
    final static String PROP_TRIANGLE = "SELECTED TRIANGLE";
    
    private TriangleSelectVisualPanel component;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;

    @Override
    public Component getComponent() {
        if(component == null) {
            component = new TriangleSelectVisualPanel();
            component.addChangeListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        this.wizard = data;
        setProject();
        validatePanel();
    }
    
    private void setProject() {
        ProjectElement project = (ProjectElement) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT);
        component.setProject(project);
        setTriangle();
    }
    
    private void setTriangle() {
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        Project project = (Project) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT);
        Triangle triangle = lkp.lookup(Triangle.class);
        if(triangle != null && triangle.getProject().equals(project))
            component.setTriangle(triangle);
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

    @Override
    public void stateChanged(ChangeEvent e) {
        validatePanel();
        fireChange();
    }
    
    private void validatePanel() {
        isValid = checkTriangle();
        if(isValid)
            showError(null);
    }
    
    private boolean checkTriangle() {
        Triangle triangle = component.getTriangle();
        if(triangle == null) {
            showError(Bundle.MSG_TriangleSelectWizardPanel_Triangle_Empty());
            return false;
        }
        return true;
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
    public void validate() throws WizardValidationException {
        try {
            ChainLadderEstimate estimate = SessionTask.withOpenSession(createFactory());
            addProjectElement(estimate);
            clearProperties();    
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to create ChainLadderEstimate!", ex);
            Exceptions.printStackTrace(ex);
        }
    }    
    
    private Factory createFactory() {
        Triangle triangle = component.getTriangle();
        String name = (String) wizard.getProperty(NameSelectWizardPanel.PROP_NAME);
        String description = (String) wizard.getProperty(NameSelectWizardPanel.PROP_DESCRIPTION);
        return new Factory(triangle.getId(), name, description);
    }
    
    private void addProjectElement(ChainLadderEstimate estimate) {
        ChainLadderEstimateProjectElement element = new ChainLadderEstimateProjectElement(estimate);
        getContainer().addElement(element);
    }
    
    private ProjectElementContainer getContainer() {
        ProjectElement element = (ProjectElement) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT);
        Object v = element.getFirstChildValue(EstimateContainerFactory.POSITION, ProjectElementContainer.class);
        return (ProjectElementContainer) v;
    }
    
    private void clearProperties() {
        wizard.putProperty(NameSelectWizardPanel.PROP_NAME, null);
        wizard.putProperty(NameSelectWizardPanel.PROP_DESCRIPTION, null);
        wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT, null);
        wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT, null);
        wizard.putProperty(PROP_TRIANGLE, null);
    }
    
    private class Factory extends SessionTask.AbstractTask<ChainLadderEstimate> {

        private final String triangleId;
        private final String name;
        private final String description;
        
        Factory(String triangleId, String name, String description) {
            this.triangleId = triangleId;
            this.name = name;
            this.description = description;
        }
        
        @Override
        public void doWork(Session session) throws Exception {
            Triangle triangle = (Triangle) session.get(Triangle.class, triangleId);
            triangle.getProject().getId();
            ChainLadderEstimate estimate = createEstimate(triangle);
            session.persist(estimate);
            result = estimate;
        }
    
        private ChainLadderEstimate createEstimate(Triangle triangle) {
            ChainLadderEstimate estimate = new ChainLadderEstimate(triangle, name);
            estimate.setDescription(description);
            return estimate;
        }
    }
    
}
