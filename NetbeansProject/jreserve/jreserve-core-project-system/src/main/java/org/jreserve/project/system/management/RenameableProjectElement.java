package org.jreserve.project.system.management;

import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.RenameableProjectElement.emptyname=Empty name is invalid!",
    "# {0} - the new name",
    "# {1} - the parent's name",
    "MSG.RenameableProjectElement.name.exists=Name \"{0}\" already used within \"{1}\"!"
})
public class RenameableProjectElement implements Renameable {

    private ProjectElement element;
    
    public RenameableProjectElement(ProjectElement element) {
        this.element = element;
    }
    
    @Override
    public void setName(String name) {
        if(checkName(name))
            setNewName(name);
    }
    
    protected boolean checkName(String name) {
        String original = getOriginalName();
        if(original.equals(name))
            return false;
        if(original.equalsIgnoreCase(name))
            return true;
        return checkNotNull(name) && checkNotExists(name);
    }
    
    protected String getOriginalName() {
        return (String) element.getProperty(ProjectElement.NAME_PROPERTY);
    }
    
    private boolean checkNotNull(String name) {
        if(name != null && name.trim().length() > 0)
            return true;
        showError(Bundle.MSG_RenameableProjectElement_emptyname());
        return false;
    }
        
    protected void showError(String msg) {
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
        
    protected boolean checkNotExists(String name) {
        ProjectElement parent = element.getParent();
        if(parent == null)
            return true;
        return checkNotExists(parent.getChildren(element.getValue().getClass()), name);
    }
    
    private boolean checkNotExists(List<ProjectElement> elements, String name) {
        for(ProjectElement child : elements)
            if(isSameName(child, name)) {
                showNameExistsError(child.getParent(), name);
                return false;
            }
        return true;
    }
    
    private boolean isSameName(ProjectElement e, String name) {
        String eName = (String) e.getProperty(ProjectElement.NAME_PROPERTY);
        return eName != null && eName.equalsIgnoreCase(name);
    }
        
    private void showNameExistsError(ProjectElement parent, String name) {
            String pName = (String) parent.getProperty(ProjectElement.NAME_PROPERTY);
            String msg = Bundle.MSG_RenameableProjectElement_name_exists(name, pName);
            showError(msg);
    }
   
    protected void setNewName(String newName) {
        element.setProperty(ProjectElement.NAME_PROPERTY, newName);
    }
}
