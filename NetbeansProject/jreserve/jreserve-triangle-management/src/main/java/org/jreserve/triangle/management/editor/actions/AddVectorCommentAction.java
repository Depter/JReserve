package org.jreserve.triangle.management.editor.actions;

import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.VectorComment;
import org.jreserve.triangle.management.VectorProjectElement;
import org.jreserve.triangle.widget.actions.AddCommentAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 */
@NbBundle.Messages({
    "CTL.AddVectorCommentAction=Add comment"
})
public class AddVectorCommentAction extends AddCommentAction {
    
    private Result<VectorProjectElement> tResult;
    private VectorProjectElement element;
    
    public AddVectorCommentAction() {
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.AddVectorCommentAction"));
    }
    
    public AddVectorCommentAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.AddVectorCommentAction"));
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
    protected Comment createComment(Comment comment) {
        VectorComment tc = createVectorComment(comment);
        List<VectorComment> comments = (List<VectorComment>) element.getProperty(VectorProjectElement.COMMENT_PROPERTY);
        comments.add(tc);
        element.setProperty(VectorProjectElement.COMMENT_PROPERTY, comments);
        return tc;
    }
    
    private VectorComment createVectorComment(Comment comment) {
        Date accident = cell.getAccidentBegin();
        String user = comment.getUserName();
        String text = comment.getCommentText();
        return new VectorComment(element.getValue(), accident, user, text);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddVectorCommentAction(lkp);
    }

}
