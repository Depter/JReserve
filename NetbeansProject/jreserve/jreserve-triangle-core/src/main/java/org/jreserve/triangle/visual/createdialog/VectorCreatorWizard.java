package org.jreserve.triangle.visual.createdialog;

import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ElementCreatorWizard.Registration(
    category=ElementCreatorWizard.Category.DATA, 
    displayName="#LBL.VectorCreatorWizard.Name",
    iconBase="resources/vector.png",
    position=200
)
@Messages({
    "LBL.VectorCreatorWizard.Name=Vector",
    "LBL.VectorCreatorWizard.Description=Create new vector from existing data."
})
public class VectorCreatorWizard extends DataCreatorWizard {

    public VectorCreatorWizard() {
        super(Bundle.LBL_VectorCreatorWizard_Description(), false);
    }
}
