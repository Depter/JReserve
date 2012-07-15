package org.jreserve.branding.msg;

import javax.swing.Icon;
import org.jreserve.branding.img.Images;
import org.openide.NotifyDescriptor;

/**
 * Taken from Quentin Beuke's {@link http://qbeukes.blogspot.nl/2009/11/netbeans-platform-notifications.html blog}.
 * 
 * @author Quintin Beuke
 * @version 1.0
 */
public enum MessageType {
    
    PLAIN   (NotifyDescriptor.PLAIN_MESSAGE,       Images.BLANK),
    INFO    (NotifyDescriptor.INFORMATION_MESSAGE, Images.INFO),
    QUESTION(NotifyDescriptor.QUESTION_MESSAGE,    Images.QUESTION),
    ERROR   (NotifyDescriptor.ERROR_MESSAGE,       Images.ERROR),
    WARNING (NotifyDescriptor.WARNING_MESSAGE,     Images.WARNING);
  
    private int notifyDescriptorType;
    private Icon icon;

    private MessageType(int notifyDescriptorType, Icon icon) {
        this.notifyDescriptorType = notifyDescriptorType;
        this.icon = icon;
    }

    int getNotifyDescriptorType() {
        return notifyDescriptorType;
    }

    Icon getIcon() {
        return icon;
    }
}