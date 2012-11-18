package org.jreserve.estimates.factors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Audited
@Entity
@Table(name="FACTOR_SELECTION", schema="JRESERVE")
public class FactorSelection extends AbstractPersistentObject {

    public final static String FACTOR_SELECTION_CORRECTIONS = "FACTOR_SELECTION_CORRECTIONS";
    public final static String FACTOR_SELECTION_SMOOTHINGS = "FACTOR_SELECTION_SMOOTHINGS";
    public final static String FACTOR_SELECTION_COMMENTS = "FACTOR_SELECTION_COMMENTS";
    public final static String FACTOR_SELECTION_EXCLUSIONS = "FACTOR_SELECTION_EXCLUSIONS";
    
    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_SELECTION_CORRECTIONS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_SELECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="CORRECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<TriangleCorrection> corrections = new HashSet<TriangleCorrection>();

    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_SELECTION_SMOOTHINGS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_SELECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="SMOOTHING_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<Smoothing> smoothings = new HashSet<Smoothing>();

    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_SELECTION_COMMENTS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_SELECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="COMMENT_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<TriangleComment> comments = new HashSet<TriangleComment>();

    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_SELECTION_EXCLUSIONS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_SELECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="EXCLUSION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<FactorExclusion> exclusions = new HashSet<FactorExclusion>();
    
    public List<TriangleCorrection> getCorrections() {
        return new ArrayList<TriangleCorrection>(corrections);
    }
    
    public void setCorrections(List<TriangleCorrection> corrections) {
        this.corrections.clear();
        if(corrections != null)
            this.corrections.addAll(corrections);
    }
    
    public List<Smoothing> getSmoothings() {
        return new ArrayList<Smoothing>(smoothings);
    }
    
    public void setSmoothings(List<Smoothing> smoothings) {
        this.smoothings.clear();
        if(smoothings != null)
            this.smoothings.addAll(smoothings);
    }
    
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }
    
    public void setComments(List<TriangleComment> comments) {
        this.comments.clear();
        if(comments != null)
            this.comments.addAll(comments);
    }
    
    public List<FactorExclusion> getExclusions() {
        return new ArrayList<FactorExclusion>(exclusions);
    }
    
    public void setExclusions(List<FactorExclusion> exclusions) {
        this.exclusions.clear();
        if(exclusions != null)
            this.exclusions.addAll(exclusions);
    }
}