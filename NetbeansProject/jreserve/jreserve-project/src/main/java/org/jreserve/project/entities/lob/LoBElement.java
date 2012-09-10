package org.jreserve.project.entities.lob;

import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - the new name",
    "MSG.LoBElement.nameexists=Name \"{0}\" is already exists!"
})
class LoBElement extends ProjectElement<LoB> {
    
    LoBElement(LoB lob) {
        super(lob);
        properties.put(NAME_PROPERTY, lob.getName());
        super.addToLookup(new LoBDeletable());
        super.addToLookup(new RenameableProjectElement(this));
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            setName((String) value);
        super.setProperty(property, value);
        getValue().setName((String) value);
    }
    
    private void setName(String name) {
        getValue().setName(name);
        addToLookup(new PersistentSavable(this));
    }
    
    private class LoBDeletable extends PersistentDeletable {
        
        private LoBDeletable() {
            super(LoBElement.this);
        }
        
        @Override
        protected void cleanUpEntity() {
        }
        
        @Override
        public Node getNode() {
            return LoBElement.this.createNodeDelegate();
        }
    }
}
