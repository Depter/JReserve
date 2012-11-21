package org.jreserve.estimates.chainladder;

import java.awt.Image;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.estimates.chainladder.visual.Editor;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.estimates.factors.FactorSelection;
import static org.jreserve.estimates.factors.FactorSelection.*;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

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
        initProperties(estimate.getFactorSelection());
    }
    
    private void initProperties(FactorSelection fs) {
        super.setProperty(FACTOR_SELECTION_COMMENTS, fs.getComments());
        super.setProperty(FACTOR_SELECTION_CORRECTIONS, fs.getCorrections());
        super.setProperty(FACTOR_SELECTION_EXCLUSIONS, fs.getExclusions());
        super.setProperty(FACTOR_SELECTION_SMOOTHINGS, fs.getSmoothings());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "ChainLadderEstimate"));
        super.addToLookup(new ChainLadderEstimateOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new ProjectElementUndoRedo(this));
        new ChainLadderEstimateSavable();
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
        else if(FactorSelection.FACTOR_SELECTION_CORRECTIONS.equals(property))
            getValue().getFactorSelection().setCorrections((List<TriangleCorrection>) value);
        else if(FactorSelection.FACTOR_SELECTION_EXCLUSIONS.equals(property))
            getValue().getFactorSelection().setExclusions((List<FactorExclusion>) value);
        else if(FactorSelection.FACTOR_SELECTION_COMMENTS.equals(property))
            getValue().getFactorSelection().setComments((List<TriangleComment>) value);
        else if(FactorSelection.FACTOR_SELECTION_SMOOTHINGS.equals(property))
            getValue().getFactorSelection().setSmoothings((List<Smoothing>) value);
        super.setProperty(property, value);
    }
    
    private class ChainLadderEstimateSavable extends PersistentSavable<ChainLadderEstimate> {

        private ChainLadderEstimateSavable() {
            super(ChainLadderEstimateProjectElement.this);
        }
        
        @Override
        protected void initOriginalProperties() {
            ChainLadderEstimate estimate = element.getValue();
            originalProperties.put(NAME_PROPERTY, estimate.getName());
            originalProperties.put(DESCRIPTION_PROPERTY, estimate.getDescription());
            initOriginalProperties(estimate.getFactorSelection());
        }
    
        private void initOriginalProperties(FactorSelection factors) {
            originalProperties.put(FactorSelection.FACTOR_SELECTION_COMMENTS, factors.getComments());
            originalProperties.put(FactorSelection.FACTOR_SELECTION_CORRECTIONS, factors.getCorrections());
            originalProperties.put(FactorSelection.FACTOR_SELECTION_EXCLUSIONS, factors.getExclusions());
            originalProperties.put(FactorSelection.FACTOR_SELECTION_SMOOTHINGS, factors.getSmoothings());
        }
    }
    
    
    private class ChainLadderEstimateOpenable extends PersistentOpenable {
        @Override
        protected TopComponent createComponent() {
            return Editor.createEditor(ChainLadderEstimateProjectElement.this);
        }
    }
    
}
