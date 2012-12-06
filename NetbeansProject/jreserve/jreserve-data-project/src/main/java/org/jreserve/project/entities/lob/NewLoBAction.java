package org.jreserve.project.entities.lob;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.management.NewElementWizard;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.project.entities.lob.NewLoBAction"
)
@ActionRegistration(
    iconBase = "resources/lob.png",
    displayName = "#CTL_NewLoBAction"
)
@ActionReferences({
    @ActionReference(path=NewElementWizard.DIRECT_ACTION_PATH, position=100)
})
@Messages("CTL_NewLoBAction=LoB")
public final class NewLoBAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NewElementWizard.showWizard(Category.PROJECT, new LoBCreatorWizard());
    }
}
