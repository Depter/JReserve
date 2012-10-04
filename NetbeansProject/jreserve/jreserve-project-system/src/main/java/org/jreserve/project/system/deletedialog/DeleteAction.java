package org.jreserve.project.system.deletedialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.jreserve.project.system.management.Deletable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.project.system.deletedialog.DeleteAction"
)
@ActionRegistration(
    iconBase = "resources/delete.png",
    displayName = "#CTL_DeleteAction.name"
)
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1400, separatorBefore=1300),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-DefaultNode", 
        position = 300, separatorBefore = 290)
})
@Messages({
    "CTL_DeleteAction.name=Delete"
})
public class DeleteAction implements ActionListener {

    private final List<Deletable> context;
    
    public DeleteAction(List<Deletable> context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DeleteDialog dialog = new DeleteDialog(context);
        dialog.setVisible(true);
    }
}
