package org.jreserve.triangle.editor;

import java.util.List;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.widget.actions.DeleteCommentsAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "JReserve/TriangleWidget/TriangleEditor",
    id = "org.jreserve.triangle.editor.DeleteTriangleCommentAction"
)
@ActionRegistration(
    displayName="#CTL.DeleteTriangleCommentAction"
)
@ActionReferences({
    @ActionReference(path = "JReserve/Popup/TriangleDataEditor", position = 200)
})
@NbBundle.Messages({
    "CTL.DeleteTriangleCommentAction=Delete comments"
})
public class DeleteTriangleCommentAction extends DeleteCommentsAction {
    
    private Lookup.Result<TriangleProjectElement> tResult;

    private TriangleProjectElement element;
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        
        tResult = lookup.lookupResult(TriangleProjectElement.class);
        tResult.addLookupListener(this);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        element = lookupOne(tResult);
        return element != null;
    }

    @Override
    protected void deleteComments(List<Comment> comments) {
        List<TriangleComment> tcs = (List<TriangleComment>) element.getProperty(TriangleProjectElement.COMMENT_PROPERTY);
        tcs.removeAll(comments);
        element.setProperty(TriangleProjectElement.COMMENT_PROPERTY, tcs);
    }
}
