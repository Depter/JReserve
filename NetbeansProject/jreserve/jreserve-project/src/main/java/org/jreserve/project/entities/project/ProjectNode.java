package org.jreserve.project.entities.project;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectNode extends DefaultProjectNode {
    
    private final static int MAX_TOOLTIP_LENGTH = 30;
    private final static ImageIcon PROJECT_ICON = ImageUtilities.loadImageIcon("resources/project.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-ProjectNode";
    
    ProjectNode(ProjectElement element) {
        super(element);
        super.addActionPath(ACTION_PATH);
        initToolTip(element.getValue().getDescription());
    }
    
    private void initToolTip(String description) {
        String tooltip = getToolTippText(description);
        super.setShortDescription(tooltip);
    }
    
    private String getToolTippText(String description) {
        String str = getFirstSentence(description);
        if(str != null && str.length() > MAX_TOOLTIP_LENGTH)
            str = str.substring(0, MAX_TOOLTIP_LENGTH-3) + "...";
        return str;
    }
    
    private String getFirstSentence(String description) {
        if(description == null || description.trim().length()==0)
            return null;
        int index = description.indexOf('.');
        return index < 0? description : description.substring(0, index+1);
    }
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(ProjectElement.DESCRIPTION_PROPERTY.equals(evt.getPropertyName()))
            initToolTip((String) evt.getNewValue());
        else
            super.propertyChange(evt);
    }
    
    @Override
    public Image getIcon(int type) {
        return PROJECT_ICON.getImage();
    }
    
}
