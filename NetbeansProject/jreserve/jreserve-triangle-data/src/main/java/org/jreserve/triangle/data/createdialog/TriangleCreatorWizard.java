package org.jreserve.triangle.data.createdialog;

import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.management.ElementCreatorWizard.Registration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Registration(
    category=Category.DATA, 
    displayName="#LBL.TriangleCreatorWizard.Name",
    iconBase="resources/triangle.png",
    position=100
)
@Messages({
    "LBL.TriangleCreatorWizard.Name=Triangle",
    "LBL.TriangleCreatorWizard.Description=Create new triangle from existing data."
})
public class TriangleCreatorWizard extends DataCreatorWizard {

    public TriangleCreatorWizard() {
        super(Bundle.LBL_TriangleCreatorWizard_Description(), true);
    }
}

