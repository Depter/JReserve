package org.jreserve.project.entities.project.editor;

import org.jreserve.project.entities.project.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementCloseHandler;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class ProjectEditor {

    public static TopComponent createTopComponent(ProjectElement element) {
        MultiViewDescription[] desc = {
            new ProjectEditorDescriptor(element)
        };
        ProjectElementCloseHandler handler = new ProjectElementCloseHandler(element);
        TopComponent tc = MultiViewFactory.createMultiView(desc, desc[0], handler);
        String name = element.getValue().getName();
        tc.setHtmlDisplayName("<html>"+name+"</html>");
        return tc;
    }
}
