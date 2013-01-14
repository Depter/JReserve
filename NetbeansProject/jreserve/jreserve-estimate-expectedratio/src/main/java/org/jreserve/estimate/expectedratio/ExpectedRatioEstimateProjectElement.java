package org.jreserve.estimate.expectedratio;

import java.awt.Image;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpectedRatioEstimateProjectElement extends ProjectElement<ExpectedRatioEstimate> {
    
    private final static Image ICON = ImageUtilities.loadImage("resources/estimate.png", false);


    public ExpectedRatioEstimateProjectElement(ExpectedRatioEstimate estimate) {
        super(estimate);
        initProperties(estimate);
        initLookup();
    }
    
    private void initProperties(ExpectedRatioEstimate estimate) {
        super.setProperty(NAME_PROPERTY, estimate.getName());
        super.setProperty(DESCRIPTION_PROPERTY, estimate.getDescription());
    }
    
    private void initLookup() {
//        super.addToLookup(new PersistentObjectDeletable(this, "ChainLadderEstimate"));
//        super.addToLookup(new ChainLadderEstimateOpenable());
//        super.addToLookup(new RenameableProjectElement(this));
//        super.addToLookup(new AuditableProjectElement(this));
//        super.addToLookup(new ProjectElementUndoRedo(this));
//        super.addToLookup(createSmoothable());
//        super.addToLookup(createCommentable());
//        super.addToLookup(createCorrectable());
//        super.addToLookup(createExcludables());
//        new ChainLadderEstimateSavable();
    }
    
    @Override
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this, ICON);
    }
    
    @Override
    public int getPosition() {
        return ExpectedRatioEstimate.POSITION;
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        super.setProperty(property, value);
    }
    
//    private class ChainLadderEstimateOpenable extends PersistentOpenable {
//        @Override
//        protected TopComponent createComponent() {
//            return Editor.createEditor(ChainLadderEstimateProjectElement.this);
//        }
//    }

}
