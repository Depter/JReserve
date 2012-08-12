package org.jreserve.project.system.util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.jreserve.project.system.management.Deletable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@ActionID(
    category = "jreserve",
    id = "org.jreserve.project.system.util.DeleteAction"
)
@ActionRegistration(
    iconBase = "resources/delete.png",
    displayName = "#CTL_DeleteAction.name"
)
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1400)
})
@Messages({
    "CTL_DeleteAction.name=Delete"
})
public class DeleteAction extends AbstractAction {
    
    
    private static List<DeleteAction> instances = new ArrayList<DeleteAction>();
    private static Result<Deletable> result;
    
    static {
        result = Deletable.REGISTRY.lookupResult(Deletable.class);
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                boolean enabled = !result.allInstances().isEmpty();
                setActionsEnabled(enabled);
            }
        });
    }
    
    private static void setActionsEnabled(boolean enabled) {
        for(DeleteAction action : instances)
            action.setEnabled(enabled);
    }
    
    public DeleteAction() {
        instances.add(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
