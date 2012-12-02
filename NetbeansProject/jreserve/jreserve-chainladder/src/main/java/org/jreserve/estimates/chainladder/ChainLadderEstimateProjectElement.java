package org.jreserve.estimates.chainladder;

import java.awt.Image;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.estimates.chainladder.visual.Editor;
import org.jreserve.estimates.factors.AbstractExcludables;
import org.jreserve.estimates.factors.Excludables;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.persistence.DeleteUtil;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.smoothing.core.AbstractSmoothable;
import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.*;
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
        super.setProperty(Commentable.COMMENT_PROPERTY, fs.getComments());
        super.setProperty(Correctable.CORRECTION_PROPERTY, fs.getCorrections());
        super.setProperty(Excludables.EXCLUSION_PROPERTY, fs.getExclusions());
        super.setProperty(Smoothable.SMOOTHING_PROPERTY, fs.getSmoothings());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "ChainLadderEstimate"));
        super.addToLookup(new ChainLadderEstimateOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new ProjectElementUndoRedo(this));
        super.addToLookup(createSmoothable());
        super.addToLookup(createCommentable());
        super.addToLookup(createCorrectable());
        super.addToLookup(createExcludables());
        new ChainLadderEstimateSavable();
    }
    
    private Smoothable createSmoothable() {
        FactorSelection owner = getValue().getFactorSelection();
        return new AbstractSmoothable(this, owner);
    }
    
    private Commentable createCommentable() {
        FactorSelection owner = getValue().getFactorSelection();
        return new AbstractCommentable(this, owner);
    }

    private Correctable createCorrectable() {
        FactorSelection owner = getValue().getFactorSelection();
        return new AbstractCorrectable(this, owner);
    }

    private Excludables createExcludables() {
        FactorSelection owner = getValue().getFactorSelection();
        return new AbstractExcludables(this, owner);
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
        else if(Correctable.CORRECTION_PROPERTY.equals(property))
            getValue().getFactorSelection().setCorrections((List<TriangleCorrection>) value);
        else if(Excludables.EXCLUSION_PROPERTY.equals(property))
            getValue().getFactorSelection().setExclusions((List<FactorExclusion>) value);
        else if(Commentable.COMMENT_PROPERTY.equals(property))
            getValue().getFactorSelection().setComments((List<TriangleComment>) value);
        else if(Smoothable.SMOOTHING_PROPERTY.equals(property))
            getValue().getFactorSelection().setSmoothings((List<Smoothing>) value);
        super.setProperty(property, value);
    }
    
    private class ChainLadderEstimateOpenable extends PersistentOpenable {
        @Override
        protected TopComponent createComponent() {
            return Editor.createEditor(ChainLadderEstimateProjectElement.this);
        }
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
            originalProperties.put(Commentable.COMMENT_PROPERTY, factors.getComments());
            originalProperties.put(Correctable.CORRECTION_PROPERTY, factors.getCorrections());
            originalProperties.put(Excludables.EXCLUSION_PROPERTY, factors.getExclusions());
            originalProperties.put(Smoothable.SMOOTHING_PROPERTY, factors.getSmoothings());
        }
 
        @Override
        protected void saveEntity() {
            super.saveEntity();
            DeleteUtil deleter = new DeleteUtil();
            addEntities(deleter, Smoothing.class, Smoothable.SMOOTHING_PROPERTY);
            addEntities(deleter, TriangleComment.class, Commentable.COMMENT_PROPERTY);
            addEntities(deleter, TriangleCorrection.class, Correctable.CORRECTION_PROPERTY);
            addEntities(deleter, TriangleCorrection.class, Excludables.EXCLUSION_PROPERTY);
            deleter.delete(session);
        }
    }   
}
