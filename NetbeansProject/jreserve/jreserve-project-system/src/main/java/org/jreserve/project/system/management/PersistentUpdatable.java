package org.jreserve.project.system.management;

import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
 abstract class PersistentUpdatable<T> extends PersistentSavable<T> {

    public PersistentUpdatable(ProjectElement<T> element) {
        super(element);
    }
    
    @Override
    protected void saveEntity() {
        session.update(element.getValue());
    }
}
