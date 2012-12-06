package org.jreserve.project.system.management;

import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.newdialog.NewProjectElementWizardIterator;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.NewElementWizard.Title=New element..."
})
public class NewElementWizard {

    public final static String ELEMENT_CREATOR_WIZARD = "ELEMENT_CREATOR_WIZARD";
    public final static String LAST_CATEGORY = "LAST_ELEMENT_CATEGORY";
    public final static String LAST_ELEMENT_CREATOR = "LAST_ELEMENT_CREATOR";
    public final static String DIRECT_ACTION_PATH = "JReserve/NewWizard/DirectActions";
    
    public static void showWizard(Category category, ElementCreatorWizard elementCreator) {
        NewProjectElementWizardIterator iterator = new NewProjectElementWizardIterator();
        WizardDescriptor descriptor = createDescriptor(iterator);
        iterator.setWizardDescriptor(descriptor);
        
        descriptor.putProperty(ELEMENT_CREATOR_WIZARD, elementCreator);
        descriptor.putProperty(LAST_CATEGORY, category);
        descriptor.putProperty(LAST_ELEMENT_CREATOR, elementCreator);
        iterator.nextPanel();
        
        showDialog(descriptor);
    }
    
    public static void showWizard() {
        NewProjectElementWizardIterator iterator = new NewProjectElementWizardIterator();
        WizardDescriptor descriptor = createDescriptor(iterator);
        iterator.setWizardDescriptor(descriptor);
        showDialog(descriptor);
    }
    
    private static WizardDescriptor createDescriptor(WizardDescriptor.Iterator iterator) {
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        descriptor.putProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP, getLookup());
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle(Bundle.LBL_NewElementWizard_Title());
        return descriptor;
    }
    
    private static void showDialog(WizardDescriptor descriptor) {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
    }
    
    private static Lookup getLookup() {
        List<ProjectElement> elements = getPath();
        int length = elements.size();
        Lookup[] lookups = new Lookup[length];
        for(int i=0; i<length; i++)
            lookups[i] = elements.get(i).getLookup();
        return new ProxyLookup(lookups);
    }
    
    private static List<ProjectElement> getPath() {
        List<ProjectElement> elements = getSelectedElements();
        if(elements.size() != 1)
            return Collections.EMPTY_LIST;
        return getPath(elements);
    }
    
    private static List<ProjectElement> getSelectedElements() {
        Collection<? extends ProjectElement> elements = Utilities.actionsGlobalContext().lookupAll(ProjectElement.class);
        return new ArrayList<ProjectElement>(elements);
    }
    
    private static List<ProjectElement> getPath(List<ProjectElement> elements) {
        ProjectElement parent = elements.get(0).getParent();
        while(parent != null) {
            elements.add(parent);
            parent = parent.getParent();
        }
        return elements;
    }
    
    private NewElementWizard() {
    }
}
