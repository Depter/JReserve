package org.jreserve.database.oracle.create;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL_dialogTitle=Create Oracle Connection"
})
public class CreateDatabaseWizard {
    
    final static String DATABASE = "db";

    final static String DB_USER_NAME = "db.username";
    final static String DATABASE_CREATED = "db.created";
    
    private final static int STEP_COUNT = 2;
    
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>(STEP_COUNT);
    private String[] steps = new String[STEP_COUNT];
    private WizardDescriptor wiz;
    
    public boolean createDatabase() {
        initWizard();
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION)
            return (Boolean) wiz.getProperty(DATABASE_CREATED);
        return false;
    }
    
    private void initWizard() {
        panels.add(new CreateDatabaseWizardPanel1());
        panels.add(new CreateDatabaseWizardPanel2());
        initDescriptors();
        createWizard();
    }
    
    private void initDescriptors() {
        for(int i=0; i<STEP_COUNT; i++) {
            JComponent component = (JComponent) panels.get(i).getComponent();
            steps[i] = component.getName();
            initDescriptor(component, i);
        }
    }
    
    private void initDescriptor(JComponent component, int index) {
        component.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
        component.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
        component.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
        component.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
        component.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
    }
    
    private void createWizard() {
        wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle(Bundle.CTL_dialogTitle());
    }

}
