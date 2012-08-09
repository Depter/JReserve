package org.jreserve.project.system.newdialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NewProjectElementWizardIterator implements WizardDescriptor.Iterator {

    private WizardDescriptor descriptor;
    private List<WizardDescriptor.Panel> basicPanels;
    private List<WizardDescriptor.Panel> currentPanels;
    private int index;
    
    void setWizardDescriptor(WizardDescriptor descriptor) {
        this.descriptor = descriptor;
    }
    
    private void initializePanels() {
        if(basicPanels == null)
            createBasicPanels();
        currentPanels.addAll(basicPanels);
        int length = basicPanels.size();
        String[] steps = new String[length];
        
        for (int i = 0; i < length; i++) {
            Component c = currentPanels.get(i).getComponent();
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                jc.putClientProperty("WizardPanel_contentData", steps);
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }
    }
    //wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_DATA, contentData);
    
    private void createBasicPanels() {
        basicPanels = new ArrayList<WizardDescriptor.Panel>();
        basicPanels.add(new ElementSelectWizardPanel());
        basicPanels.add(new DummyElementWizardPanel());
        currentPanels = new ArrayList<WizardDescriptor.Panel>(basicPanels);
    }
    
    @Override
    public WizardDescriptor.Panel current() {
        initializePanels();
        return currentPanels.get(index);
    }

    @Override
    public String name() {
        if(index == 0)
            return "1 of ...";
        return (index+1) + " of "+currentPanels.size();
    }

    @Override
    public boolean hasNext() {
        initializePanels();
        return index + 1 < currentPanels.size();
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if(!hasNext())
            throw new NoSuchElementException();
        index++;
        descriptor.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    @Override
    public void previousPanel() {
        if(!hasPrevious())
            throw new NoSuchElementException();
        index--;
        descriptor.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }
}
