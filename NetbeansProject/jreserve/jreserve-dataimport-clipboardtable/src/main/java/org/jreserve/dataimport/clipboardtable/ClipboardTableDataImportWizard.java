package org.jreserve.dataimport.clipboardtable;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.dataimport.DataImportWizard;
import org.jreserve.dataimport.DataImportWizard.Registration;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ClipboardTableDataImportWizard.description=Import data "+
        "from the clipboard in a table format",
    "LBL.ClipboardTableDataImportWizard.displayName=Clipboard table"
})
@Registration(
    displayName="#LBL.ClipboardTableDataImportWizard.displayName",
    iconBase="resources/table_add.png",
    position=10
)
public class ClipboardTableDataImportWizard implements DataImportWizard {

    private List<WizardDescriptor.Panel> panels = null;
    
    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel>(1);
            panels.add(new WizardPanel());
        }
        return panels;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ClipboardTableDataImportWizard_description();
    }
}
