package org.jreserve.project.entities.project.editor;

import org.jreserve.project.entities.project.ProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class ProjectEditor {

    public static TopComponent createTopComponent(ProjectElement element) {
        MultiViewDescription[] desc = {
            new ProjectEditorDescriptor(element),
            new ProjectLogDescriptor(element)
        };
        TopComponent tc = MultiViewFactory.createCloneableMultiView(desc, desc[0]);
        tc.setDisplayName(element.getValue().getName());
        tc.setIcon(ImageUtilities.loadImage("resources/project.png"));
        return tc;
    }
}
