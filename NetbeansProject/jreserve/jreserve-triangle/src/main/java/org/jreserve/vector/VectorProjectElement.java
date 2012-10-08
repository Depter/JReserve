package org.jreserve.vector;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.triangle.entities.Vector;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
public class VectorProjectElement extends ProjectElement<Vector> {

    public final static int POSITION = 100;
    
    public VectorProjectElement(Vector vector) {
        super(vector);
        super.setProperty(NAME_PROPERTY, vector.getName());
        super.addToLookup(new PersistentDeletable(this));
    }

    @Override
    public Node createNodeDelegate() {
        return new VectorNode(this);
    }
    
    @Override
    public int getPosition() {
        return POSITION;
    }
}
