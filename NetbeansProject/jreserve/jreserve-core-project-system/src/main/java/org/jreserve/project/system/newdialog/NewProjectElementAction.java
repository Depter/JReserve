package org.jreserve.project.system.newdialog;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.project.system.management.NewElementWizard;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.project.system.newdialog.NewProjectElementAction"
)
@ActionRegistration(displayName = "#CTL_NewProjectElementAction", lazy=false)
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1100),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-DefaultNode", position = 100)
})
@Messages({
    "CTL_NewProjectElementAction=New",
    "CTL_NewProjectElementAction.Other=Other",
    "LBL_NewProjectElementAction.title=New Element..."
})
public final class NewProjectElementAction extends AbstractAction implements Presenter.Menu, Presenter.Popup {
    
    private JMenu menuPresenter = null;
    private JMenu popUpPresenter = null;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        NewElementWizard.showWizard();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        if(menuPresenter == null) {
            menuPresenter = new JMenu(Bundle.CTL_NewProjectElementAction());
            createMenu(menuPresenter);
        }
        return menuPresenter;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        if(popUpPresenter == null) {
            popUpPresenter = new JMenu(Bundle.CTL_NewProjectElementAction());
            createMenu(popUpPresenter);
        }
        return popUpPresenter;
    }
    
    private void createMenu(JMenu menu) {
        addDirectActions(menu);
        menu.addSeparator();
        menu.add(getMyItem());
    }
    
    private void addDirectActions(JMenu menu) {
        for(Action action : Utilities.actionsForPath(NewElementWizard.DIRECT_ACTION_PATH))
            menu.add(action);
    }
    
    private JMenuItem getMyItem() {
        JMenuItem item = new JMenuItem(Bundle.CTL_NewProjectElementAction_Other());
        item.addActionListener(this);
        return item;
    }
}
