/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.project.system.newdialog;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "jreserve",
    id = "org.jreserve.project.system.newdialog.NewProjectElementAction"
)
@ActionRegistration(displayName = "#CTL_NewProjectElementAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1300, separatorAfter = 1350)
})
@Messages("CTL_NewProjectElementAction=New...")
public final class NewProjectElementAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NewProjectElementWizardIterator iterator = new NewProjectElementWizardIterator();
        WizardDescriptor descriptor = new WizardDescriptor(iterator);
        iterator.setWizardDescriptor(descriptor);
        
        descriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        descriptor.setTitle("New Element...");
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.toFront();
        
        boolean cancelled = descriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        
    }
}
