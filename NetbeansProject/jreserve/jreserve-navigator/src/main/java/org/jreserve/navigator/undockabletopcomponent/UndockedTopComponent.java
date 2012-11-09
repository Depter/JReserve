package org.jreserve.navigator.undockabletopcomponent;

import java.awt.BorderLayout;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
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
        open(tc);
        //undock(tc);
        tc.dock(component);
        tc.opening = false;
        return tc;
    }
    
    private static void open(UndockedTopComponent tc) {
        tc.open();
        tc.requestVisible();
        tc.requestActive();
    }
    
    private static void undock(UndockedTopComponent tc) {
        if(MODE != null)
            MODE.dockInto(tc);
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
        //setActionMap(new ActionMap());
        initLookup();
    }
    
    private void initComponents() {
        add(contentPane, BorderLayout.CENTER);
    }
    
    private void dock(JComponent component) {
        this.component = component;
        contentPane.add(component, BorderLayout.CENTER);
        contentPane.revalidate();
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
