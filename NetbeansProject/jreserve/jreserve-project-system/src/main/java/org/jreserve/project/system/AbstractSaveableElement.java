package org.jreserve.project.system;

import java.io.IOException;
import org.netbeans.spi.actions.AbstractSavable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSaveableElement extends AbstractSavable {
    
    private ProjectElement element;
    
    protected AbstractSaveableElement(ProjectElement element) {
        this.element = element;
        super.register();
    }
    
    @Override
    protected String findDisplayName() {
        return element.getValue().toString();
    }

    @Override
    protected void handleSave() throws IOException {
        persist();
        super.unregister();
    }

    protected abstract void persist() throws IOException;
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractSaveableElement)
            return equals((AbstractSaveableElement) o);
        return false;
    }
    
    private boolean equals(AbstractSaveableElement ase) {
        Object value = element.getValue();
        Object aseValue = ase.element.getValue();
        return value==null? (aseValue==null) : value.equals(aseValue);
    }

    @Override
    public int hashCode() {
        Object value = element.getValue();
        return value==null? 0 : value.hashCode();
    }
}
