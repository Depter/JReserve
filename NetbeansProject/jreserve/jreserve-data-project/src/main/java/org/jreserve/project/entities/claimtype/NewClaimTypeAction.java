package org.jreserve.project.entities.claimtype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.management.NewElementWizard;
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
    id = "org.jreserve.project.entities.claimtype.NewClaimTypeAction"
)
@ActionRegistration(
    iconBase = "resources/claim_type.png",
    displayName = "#CTL_NewClaimTypeAction"
)
@ActionReferences({
    @ActionReference(path=NewElementWizard.DIRECT_ACTION_PATH, position=200)
})
@Messages("CTL_NewClaimTypeAction=ClaimType")
public class NewClaimTypeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NewElementWizard.showWizard(Category.PROJECT, new ClaimTypeCreatorWizard());
    }
}
