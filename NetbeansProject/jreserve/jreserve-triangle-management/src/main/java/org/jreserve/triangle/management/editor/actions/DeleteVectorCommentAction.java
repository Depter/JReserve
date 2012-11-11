package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.VectorComment;
import org.jreserve.triangle.management.VectorProjectElement;
import org.jreserve.triangle.widget.actions.DeleteCommentsAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 */
@NbBundle.Messages({
    "CTL.DeleteVectorCommentAction=Delete comments"
})
public class DeleteVectorCommentAction extends DeleteCommentsAction {
    
    private Lookup.Result<VectorProjectElement> tResult;

    private VectorProjectElement element;
    
    public DeleteVectorCommentAction() {
        putValue(NAME, NbBundle.getMessage(DeleteVectorCommentAction.class, "CTL.DeleteVectorCommentAction"));
    }
    
    public DeleteVectorCommentAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(DeleteTriangleCommentAction.class, "CTL.DeleteVectorCommentAction"));
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(VectorProjectElement.class);
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
        List<VectorComment> vcs = (List<VectorComment>) element.getProperty(VectorProjectElement.COMMENT_PROPERTY);
        vcs.removeAll(comments);
        element.setProperty(VectorProjectElement.COMMENT_PROPERTY, vcs);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteVectorCommentAction(lkp);
    }
}
