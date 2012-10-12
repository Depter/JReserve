package org.jreserve.triangle;

import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.triangle.entities.Triangle;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - triangle name",
    "LOG.TriangleProjectElement.Deleted=Triangle \"{0}\" deleted."
})
public class TriangleProjectElement extends ProjectElement<Triangle> {

    public final static int POSITION = 100;
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.addToLookup(new TriangleDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new TriangleNode(this);
    }
    
    @Override
    public int getPosition() {
        return POSITION;
    }
    
    private class TriangleDeletable extends PersistentDeletable {
        
        TriangleDeletable() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected void cleanUpAfterEntity(Session session) {
            Triangle triangle = TriangleProjectElement.this.getValue();
            String name = triangle.getName();
            String msg = Bundle.LOG_TriangleProjectElement_Deleted(name);
            logDeletion(triangle.getProject(), msg);
        }
        
        private void logDeletion(Project project, String msg) {
            ChangeLogUtil util = ChangeLogUtil.getDefault();
            util.addChange(project, ChangeLog.Type.DATA, msg);
            util.saveValues(project);
        }
    }
}
