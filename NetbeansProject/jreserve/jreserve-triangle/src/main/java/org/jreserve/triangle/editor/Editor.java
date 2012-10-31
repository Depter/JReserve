package org.jreserve.triangle.editor;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.CloseConfirmDialog;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.VectorProjectElement;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationHandler;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Editor {
    
    public static TopComponent createTopComponent(TriangleProjectElement element) {
        MultiViewDescription[] desc = {
            TriangleEditorDescriptor.getTriangle(element),
            new TriangleDataEditorDescriptor(element)
        };
        String name = element.getValue().getName();
        return createTopComponent(desc, name);
    }
    
    private static TopComponent createTopComponent(MultiViewDescription[] desc, String name) {
        CloseHandler handler = new CloseHandler();
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
        return createTopComponent(desc, name);
    }
    
    private static class CloseHandler implements CloseOperationHandler {

        private ProjectElement element;
        
        @Override
        public boolean resolveCloseOperation(CloseOperationState[] coss) {
            return componentsCanBeClosed(coss) &&
                   elementsSaved();
        }
        
        private boolean componentsCanBeClosed(CloseOperationState[] coss) {
            for(CloseOperationState state : coss)
                if(CloseOperationState.STATE_OK != state)
                    return false;
            return true;
        }
        
        private boolean elementsSaved() {
            Lookup lkp = element.getLookup();
            if(lkp.lookup(Savable.class) == null)
                return true;
            return CloseConfirmDialog.canClose(lkp);
        }
    }
}
