package org.jreserve.data.importdialog.clipboardtable;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.DataImportWizard;
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
