package org.jreserve.estimates.chainladder;

import java.awt.Image;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class ChainLadderEstimateProjectElement extends ProjectElement<ChainLadderEstimate>{
    
    private final static Image ICON = ImageUtilities.loadImage("resources/estimate.png", false);

    public ChainLadderEstimateProjectElement(ChainLadderEstimate estimate) {
        super(estimate);
        initProperties(estimate);
        initLookup();
    }
    
    private void initProperties(ChainLadderEstimate estimate) {
        super.setProperty(NAME_PROPERTY, estimate.getName());
        super.setProperty(DESCRIPTION_PROPERTY, estimate.getDescription());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "ChainLadderEstimate"));
        //super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        //super.addToLookup(new TriangleUndoRedo());
        //new TriangleSavable();
    }

    @Override
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this, ICON);
    }
    
    @Override
    public int getPosition() {
        return ChainLadderEstimate.POSITION;
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        super.setProperty(property, value);
    }
    
}
