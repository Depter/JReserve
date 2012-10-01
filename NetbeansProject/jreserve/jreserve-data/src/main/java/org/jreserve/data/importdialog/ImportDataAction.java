package org.jreserve.data.importdialog;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.jreserve.data.DataImportWizard;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 */
@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.data.importdialog.ImportDataAction"
)
@ActionRegistration(displayName = "#CTL_ImportDataAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1250),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-ProjectNode", position = 250)
})
@Messages({
    "CTL_ImportDataAction=Import data",
    "LBL.ImportDataAction.Dialog.Title=Import data"
})
public class ImportDataAction implements ActionListener {
    
    private Project project;
    
    public ImportDataAction(Project project) {
        this.project = project;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DataImportWizardIterator iterator = new DataImportWizardIterator();
        WizardDescriptor descriptor = createDescriptor(iterator);
        showWizard(descriptor);
    }
    
    private WizardDescriptor createDescriptor(DataImportWizardIterator iterator) {
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        descriptor.putProperty(DataImportWizard.PROJECT_PROPERTY, getProjectElement());
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle(Bundle.LBL_ImportDataAction_Dialog_Title());
        iterator.setWizardDescriptor(descriptor);
        return descriptor;
    }
    
    private ProjectElement<Project> getProjectElement() {
        Lookup lkp = Utilities.actionsGlobalContext();
        ProjectElement<Project> element = lkp.lookup(ProjectElement.class);
        return element;
    }

    private void showWizard(WizardDescriptor descriptor) {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
    }
}
