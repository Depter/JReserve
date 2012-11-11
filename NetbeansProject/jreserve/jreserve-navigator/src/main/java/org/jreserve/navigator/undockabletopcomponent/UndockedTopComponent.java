package org.jreserve.navigator.undockabletopcomponent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.openide.filesystems.FileUtil;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 */
public class UndockedTopComponent extends TopComponent{

    private final static Mode MODE = WindowManager.getDefault().findMode("navigableUndocked");
    
    public static UndockedTopComponent create(String title, JComponent component, DockTarget target) {
        UndockedTopComponent tc = new UndockedTopComponent(title, target);
        tc.dock(component);
        open(tc);
        undock(tc);
        tc.opening = false;
        return tc;
    }
    
    private static void open(UndockedTopComponent tc) {
        tc.open();
        tc.requestVisible();
        tc.requestActive();
    }
    
    private static void undock(UndockedTopComponent tc) {
        Action action = FileUtil.getConfigObject("Actions/Window/org-netbeans-core-windows-actions-UndockWindowAction.instance", Action.class);
        if(action != null && WindowManager.getDefault().getRegistry().getActivated() == tc) {
            action.actionPerformed(new ActionEvent(tc, ActionEvent.ACTION_FIRST, "open-tc"));
        }
    }
    
    private JPanel contentPane = new JPanel(new BorderLayout());
    private JComponent component;
    private DockTarget source;
    private Lookup lookup;
    private boolean opening = true;
    
    private UndockedTopComponent(String title, DockTarget source) {
        setDisplayName(title);
        this.source = source;
        initComponents();
        setActionMap(new ActionMap());
    }
    
    private void initComponents() {
        super.setLayout(new BorderLayout());
        super.add(new JScrollPane(contentPane), BorderLayout.CENTER);
    }
    
    private void dock(JComponent component) {
        this.component = component;
        contentPane.add(component, BorderLayout.CENTER);
        contentPane.revalidate();
        initLookup();
    }
    
    private void initLookup() {
        if(component instanceof Lookup.Provider)
            lookup = ((Lookup.Provider) component).getLookup();
        else
            lookup = Lookup.EMPTY;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }    
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        if(!opening && component!=null)
            redockComponent();
    }
    
    private void redockComponent() {
        contentPane.remove(component);
        source.dock(component);
        component = null;
        source = null;
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }
}
