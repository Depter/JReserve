package org.jreserve.data.importdialog;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.jreserve.data.DataImportWizard;
import org.jreserve.project.entities.Project;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

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
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        descriptor.putProperty(DataImportWizard.PROJECT_PROPERTY, project);
        iterator.setWizardDescriptor(descriptor);
        
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle(Bundle.LBL_ImportDataAction_Dialog_Title());
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
    }

}
