package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.widget.actions.DeleteCommentsAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
//@ActionID(
//    category = "JReserve/TriangleWidget/TriangleEditor",
//    id = "org.jreserve.triangle.editor.DeleteTriangleCommentAction"
//)
//@ActionRegistration(
//    displayName="#CTL.DeleteTriangleCommentAction",
//    lazy=true
//)
//@ActionReferences({
//    @ActionReference(path = "JReserve/Popup/TriangleDataEditor", position = 200)
//})
@NbBundle.Messages({
    "CTL.DeleteTriangleCommentAction=Delete comments"
})
public class DeleteTriangleCommentAction extends DeleteCommentsAction {
    
    private Lookup.Result<TriangleProjectElement> tResult;

    private TriangleProjectElement element;
    
    public DeleteTriangleCommentAction() {
        putValue(NAME, NbBundle.getMessage(DeleteTriangleCommentAction.class, "CTL.DeleteTriangleCommentAction"));
    }
    
    public DeleteTriangleCommentAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(DeleteTriangleCommentAction.class, "CTL.DeleteTriangleCommentAction"));
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(TriangleProjectElement.class);
        tResult.addLookupListener(this);
        super.init(lookup);
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

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteTriangleCommentAction(lkp);
    }
}
