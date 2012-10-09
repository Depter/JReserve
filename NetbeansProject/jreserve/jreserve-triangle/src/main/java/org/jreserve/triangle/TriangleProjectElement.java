package org.jreserve.triangle;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.triangle.entities.Triangle;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleProjectElement extends ProjectElement<Triangle> {

    public final static int POSITION = 100;
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.addToLookup(new PersistentDeletable(this));
    }

    @Override
    public Node createNodeDelegate() {
        return new TriangleNode(this);
    }
    
    @Override
    public int getPosition() {
        return POSITION;
    }
}
