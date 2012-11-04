package org.jreserve.triangle.management.editor;

import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.widget.actions.AddCommentAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.AddTriangleCommentAction=Add comment"
})
public class AddTriangleCommentAction extends AddCommentAction {
    
    private Result<TriangleProjectElement> tResult;
    private TriangleProjectElement element;
    
    public AddTriangleCommentAction() {
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.AddTriangleCommentAction"));
    }
    
    public AddTriangleCommentAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.AddTriangleCommentAction"));
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
    protected Comment createComment(Comment comment) {
        TriangleComment tc = createTriangleComment(comment);
        List<TriangleComment> comments = (List<TriangleComment>) element.getProperty(TriangleProjectElement.COMMENT_PROPERTY);
        comments.add(tc);
        element.setProperty(TriangleProjectElement.COMMENT_PROPERTY, comments);
        return tc;
    }
    
    private TriangleComment createTriangleComment(Comment comment) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        String user = comment.getUserName();
        String text = comment.getCommentText();
        return new TriangleComment(element.getValue(), accident, development, user, text);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddTriangleCommentAction(lkp);
    }

}
