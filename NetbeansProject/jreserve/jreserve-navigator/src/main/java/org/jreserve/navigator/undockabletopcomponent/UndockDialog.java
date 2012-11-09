package org.jreserve.navigator.undockabletopcomponent;

import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 */
public class UndockDialog extends JFrame {

    public static Dialog createDialog(String title, JComponent component, DockTarget dockTarget) {
        Dialog dialog = createDialog(title, component);
        dialog.setAlwaysOnTop(false);
        dialog.addWindowListener(new Redocker(component, dockTarget));
        dialog.setResizable(true);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        return dialog;
    }
    
    private static Dialog createDialog(String title, JComponent component) {
        DialogDescriptor dd = new DialogDescriptor(component, title);
        dd.setModal(false);
        dd.setOptions(new Object[0]);
        return DialogDisplayer.getDefault().createDialog(dd);
    }
    
    private static class Redocker extends WindowAdapter {

        private DockTarget target;
        private JComponent component;
        
        private Redocker(JComponent component, DockTarget target) {
            this.component = component;
            this.target = target;
        }
        
        @Override
        public void windowClosed(WindowEvent e) {
            target.dock(component);
            this.component = null;
            this.target = null;
        }
    }
}
