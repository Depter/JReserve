package org.jreserve.project.system.newdialog;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.ProxyLookup;

@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.project.system.newdialog.NewProjectElementAction"
)
@ActionRegistration(displayName = "#CTL_NewProjectElementAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1100),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-DefaultNode", position = 100)
})
@Messages({
    "CTL_NewProjectElementAction=New...",
    "LBL_NewProjectElementAction.title=New Element..."
})
public final class NewProjectElementAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NewProjectElementWizardIterator iterator = new NewProjectElementWizardIterator();
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        descriptor.putProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP, getLookup());
        iterator.setWizardDescriptor(descriptor);
        
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle(Bundle.LBL_NewProjectElementAction_title());
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
        
        boolean cancelled = descriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        
    }
    
    private Lookup getLookup() {
        List<ProjectElement> elements = getPath();
        int length = elements.size();
        Lookup[] lookups = new Lookup[length];
        for(int i=0; i<length; i++)
            lookups[i] = elements.get(i).getLookup();
        return new ProxyLookup(lookups);
    }
    
    private List<ProjectElement> getPath() {
        List<ProjectElement> elements = getSelectedElements();
        if(elements.size() != 1)
            return Collections.EMPTY_LIST;
        return getPath(elements);
    }
    
    private List<ProjectElement> getSelectedElements() {
        Collection<? extends ProjectElement> elements = Utilities.actionsGlobalContext().lookupAll(ProjectElement.class);
        return new ArrayList<ProjectElement>(elements);
    }
    
    private List<ProjectElement> getPath(List<ProjectElement> elements) {
        ProjectElement parent = elements.get(0).getParent();
        while(parent != null) {
            elements.add(parent);
            parent = parent.getParent();
        }
        return elements;
    }
}
