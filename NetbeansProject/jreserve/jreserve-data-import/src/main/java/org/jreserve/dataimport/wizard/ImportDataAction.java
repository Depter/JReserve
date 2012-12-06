package org.jreserve.dataimport.wizard;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.jreserve.dataimport.DataImportWizard;
import org.jreserve.project.entities.ClaimType;
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
    category = "JReserve/Data",
    id = "org.jreserve.dataimport.wizard.ImportDataAction"
)
@ActionRegistration(displayName = "#CTL_ImportDataAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1250),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-ClaimTypeNode", position = 250)
})
@Messages({
    "CTL_ImportDataAction=Import data",
    "LBL.ImportDataAction.Dialog.Title=Import data"
})
public class ImportDataAction implements ActionListener {
    
    private ClaimType claimType;
    
    public ImportDataAction(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DataImportWizardIterator iterator = new DataImportWizardIterator();
        WizardDescriptor descriptor = createDescriptor(iterator);
        showWizard(descriptor);
    }
    
    private WizardDescriptor createDescriptor(DataImportWizardIterator iterator) {
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        descriptor.putProperty(DataImportWizard.CLAIM_TYPE_PROPERTY, getClaimTypeElement());
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle(Bundle.LBL_ImportDataAction_Dialog_Title());
        iterator.setWizardDescriptor(descriptor);
        return descriptor;
    }
    
    private ProjectElement<ClaimType> getClaimTypeElement() {
        Lookup lkp = Utilities.actionsGlobalContext();
        ProjectElement<ClaimType> element = lkp.lookup(ProjectElement.class);
        return element;
    }

    private void showWizard(WizardDescriptor descriptor) {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
    }
}
