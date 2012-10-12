package org.jreserve.vector;

import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.triangle.entities.Vector;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - vector name",
    "LOG.VectorProjectElement.Deleted=Vector \"{0}\" deleted."
})
public class VectorProjectElement extends ProjectElement<Vector> {

    public final static int POSITION = 200;
    
    public VectorProjectElement(Vector vector) {
        super(vector);
        super.setProperty(NAME_PROPERTY, vector.getName());
        super.addToLookup(new VectorDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new VectorNode(this);
    }
    
    @Override
    public int getPosition() {
        return POSITION;
    }

    
    private class VectorDeletable extends PersistentDeletable {
        
        VectorDeletable() {
            super(VectorProjectElement.this);
        }

        @Override
        protected void cleanUpAfterEntity(Session session) {
            Vector vector = VectorProjectElement.this.getValue();
            String name = vector.getName();
            String msg = Bundle.LOG_VectorProjectElement_Deleted(name);
            logDeletion(vector.getProject(), msg);
        }
        
        private void logDeletion(Project project, String msg) {
            ChangeLogUtil util = ChangeLogUtil.getDefault();
            util.addChange(project, ChangeLog.Type.DATA, msg);
            util.saveValues(project);
        }
    }
}
