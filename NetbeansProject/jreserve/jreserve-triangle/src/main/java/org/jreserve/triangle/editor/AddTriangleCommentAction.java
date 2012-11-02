package org.jreserve.triangle.editor;

import java.util.Date;
import java.util.List;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.widget.actions.AddCommentAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */

@ActionID(
    category = "JReserve/TriangleWidget/TriangleEditor",
    id = "org.jreserve.triangle.editor.AddTriangleCommentAction"
)
@ActionRegistration(
    displayName="#CTL.AddTriangleCommentAction"
)
@ActionReferences({
    @ActionReference(path = "JReserve/Popup/TriangleDataEditor", position = 100)
})
@Messages({
    "CTL.AddTriangleCommentAction=Add comment"
})
public class AddTriangleCommentAction extends AddCommentAction {
    
    private Result<TriangleProjectElement> tResult;

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

}
