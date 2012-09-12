package org.jreserve.project.system.management;

import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistentUpdatable extends PersistentSavable {

    public PersistentUpdatable(ProjectElement element) {
        super(element);
    }
    
    @Override
    protected void saveEntity() {
        session.update(element.getValue());
    }
}
