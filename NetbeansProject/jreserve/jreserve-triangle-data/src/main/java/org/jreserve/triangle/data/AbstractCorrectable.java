package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;
import static org.jreserve.triangle.data.Correctable.CORRECTION_PROPERTY;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCorrectable<T> implements Correctable {

    private final static String WRONG_OWNER = "Correction '%s' belongs to another owner than '%s'! OwnerId = '%s'.";
    
    protected ProjectElement<T> element;
    protected PersistentObject owner;

    public AbstractCorrectable(ProjectElement<T> element, PersistentObject owner) {
        this.element = element;
        this.owner = owner;
    }

    @Override
    public PersistentObject getOwner() {
        return owner;
    }

    @Override
    public List<TriangleCorrection> getCorrections() {
        return (List<TriangleCorrection>) element.getProperty(CORRECTION_PROPERTY);
    }

    @Override
    public void setCorrections(List<TriangleCorrection> corrections) {
        checkOwnerId(corrections);
        element.setProperty(CORRECTION_PROPERTY, corrections);
    }
    
    private void checkOwnerId(List<TriangleCorrection> corrections) {
        for(TriangleCorrection correction : corrections)
            checkOwnerId(correction);
    }

    private void checkOwnerId(TriangleCorrection correction) {
        if(!correction.getOwnerId().equals(owner.getId())) {
            String msg = String.format(WRONG_OWNER, correction, owner, correction.getOwnerId());
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void addCorrection(TriangleCorrection correction) {
        checkOwnerId(correction);
        List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>(getCorrections());
        if(!corrections.contains(correction))
            corrections.add(correction);
        element.setProperty(CORRECTION_PROPERTY, corrections);
    }

    @Override
    public void removeCorrection(TriangleCorrection correction) {
        List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>(getCorrections());
        corrections.remove(correction);
        element.setProperty(CORRECTION_PROPERTY, corrections);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Correctable)
            return owner.equals(((Correctable)o).getOwner());
        return false;
    }
    
    @Override
    public int hashCode() {
        return owner.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("AbstractCorrection [%s; %s]", element, owner);
    }
}
