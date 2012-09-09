/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.project.system.management;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.netbeans.api.actions.Openable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "jreserve",
    id = "org.jreserve.project.system.management.EditAction"
)
@ActionRegistration(displayName = "#CTL_EditAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1325)
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
