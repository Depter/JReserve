package org.jreserve.project.system.visual;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.netbeans.api.actions.Savable;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class TabNameAdapter implements LookupListener, PropertyChangeListener {

    public static void createAdapter(TopComponent component, ProjectElement element) {
        TabNameAdapter adapter = new TabNameAdapter();
        adapter.setTopComponent(component);
        adapter.setProjectElement(element);
    }
    
    private TopComponent tc;
    private ProjectElement element;
    private Lookup.Result<Savable> savableResult;
    
    private TabNameAdapter() {
    }
    
    private void setTopComponent(TopComponent tc) {
        this.tc = tc;
        TopComponent.getRegistry().addPropertyChangeListener(this);
    }
    
    private void setProjectElement(ProjectElement element) {
        this.element = element;
        savableResult = element.getLookup().lookupResult(Savable.class);
        savableResult.addLookupListener(this);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        boolean changed = !savableResult.allClasses().isEmpty();
        setHtmlDisplayName(changed);
    }

    private void setHtmlDisplayName(boolean isChanged) {
        if (isChanged) {
            tc.setHtmlDisplayName(getChangedHtmlDisplayName());
        } else {
            tc.setHtmlDisplayName(getUnchangedHtmlDisplayName());
        }
    }

    private String getChangedHtmlDisplayName() {
        return getHtmlDisplayName("<b>%s</b>");
    }

    private String getHtmlDisplayName(String format) {
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        format = "<html>" + format + "</html>";
        return String.format(format, name);
    }

    private String getUnchangedHtmlDisplayName() {
        return getHtmlDisplayName("%s");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (tc != null && !tc.isOpened() && isCloseEvent(evt)) {
            tc.removePropertyChangeListener(this);
            tc = null;
        }
    }

    private boolean isCloseEvent(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        return TopComponent.Registry.PROP_TC_CLOSED.equalsIgnoreCase(property);
    }
}
