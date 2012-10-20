package org.jreserve.project.entities.lob;

import java.util.Collections;
import java.util.List;
import org.jreserve.persistence.EntityAuditor;
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
        super.addToLookup(new PersistentDeletable(this));
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new LobAuditor());
        new LobSavable();
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        super.setProperty(property, value);
    }
    
    private class LobSavable extends PersistentSavable<LoB> {

        public LobSavable() {
            super(LoBElement.this);
        }
        
        @Override
        protected void initOriginalProperties() {
            originalProperties.put(NAME_PROPERTY, element.getValue().getName());
        }
    }
    
    private class LobAuditor implements EntityAuditor<LoB> {

        @Override
        public LoB getEntity() {
            return LoBElement.this.getValue();
        }

        @Override
        public Object getId() {
            return LoBElement.this.getValue().getId();
        }

        @Override
        public String getChange(LoB previous, LoB current) {
            if(previous == null)
                return "Created";
            if(current == null)
                return "Deleted";
            return getNameChange(previous, current);
        }
        
        private String getNameChange(LoB previous, LoB current) {
            String prevName = previous.getName();
            String currName = current.getName();
            return String.format("Name changed \"%s\" => \"%s\".", prevName, currName);
        
        }

        @Override
        public List<Class<?>> getSubEntities() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public String getName() {
            return LoBElement.this.getNamePath();
        }
    }
}
