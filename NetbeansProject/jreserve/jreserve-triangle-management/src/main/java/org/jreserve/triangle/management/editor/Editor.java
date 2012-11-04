package org.jreserve.triangle.management.editor;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementCloseHandler;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.management.VectorProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "LBL.Editor.Save.Title=Save \"{0}\"?"
})
public class Editor {
    
    public static TopComponent createTopComponent(TriangleProjectElement element) {
        MultiViewDescription[] desc = {
            TriangleEditorDescriptor.getTriangle(element),
            new TriangleDataEditorDescriptor(element)
        };
        String name = element.getValue().getName();
        return createTopComponent(desc, name, element);
    }
    
    private static TopComponent createTopComponent(MultiViewDescription[] desc, String name, ProjectElement element) {
        ProjectElementCloseHandler handler = new ProjectElementCloseHandler(element);
        TopComponent tc = MultiViewFactory.createMultiView(desc, desc[0], handler);
        tc.setHtmlDisplayName("<html>"+name+"</html>");
        return tc;
    }
    
    public static TopComponent createTopComponent(VectorProjectElement element) {
        MultiViewDescription[] desc = {
            TriangleEditorDescriptor.getVector(element),
            new VectorDataEditorDescriptor(element)
        };
        String name = element.getValue().getName();
        return createTopComponent(desc, name, element);
    }
}
