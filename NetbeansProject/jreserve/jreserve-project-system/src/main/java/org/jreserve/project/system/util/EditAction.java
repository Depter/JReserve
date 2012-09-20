package org.jreserve.project.system.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.netbeans.api.actions.Openable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.project.system.util.EditAction"
)
@ActionRegistration(
    displayName = "#CTL_EditAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1200),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-DefaultNode", position = 200)
})
@Messages("CTL_EditAction=Edit")
public final class EditAction implements ActionListener {

    private final List<Openable> context;

    public EditAction(List<Openable> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (Openable openable : context)
            openable.open();
    }
}
