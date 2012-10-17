package org.jreserve.triangle.editor;

import org.jreserve.triangle.TriangleProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Editor {

    public static TopComponent createTopComponent(TriangleProjectElement element) {
        MultiViewDescription[] desc = {
            new TriangleEditorDescriptor(element)
        };
        String name = element.getValue().getName();
        return createTopComponent(desc, name);
    }
    
    private static TopComponent createTopComponent(MultiViewDescription[] desc, String name) {
        TopComponent tc = MultiViewFactory.createMultiView(desc, desc[0]);
        tc.setHtmlDisplayName("<html>"+name+"</html>");
        return tc;
    }
}
