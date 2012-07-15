package org.jreserve.branding.msg;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * Taken from Quentin Beuke's {@link http://qbeukes.blogspot.nl/2009/11/netbeans-platform-notifications.html blog}.
 * 
 * @author Quintin Beuke
 * @version 1.0
 */
@Messages({
    "CTL_errorTitle=Error"
})
public class MessageUtil {

    private MessageUtil() {}

    /**
     * @return The dialog displayer used to show message boxes
     */
    private static DialogDisplayer getDialogDisplayer() {
        return DialogDisplayer.getDefault();
    }

    /**
     * Show a message of the specified type
     *
     * @param message
     * @param messageType As in {@link NotifyDescription} message type constants.
     */
    public static void show(String message, MessageType messageType) {
        getDialogDisplayer().notify(new NotifyDescriptor.Message(message, messageType.getNotifyDescriptorType()));
    }

    /**
     * Show an exception message dialog
     *
     * @param message
     * @param exception
     */
    private static void showException(String message, Throwable exception) {
        NotifyDescriptor nd = new NotifyDescriptor(
                message, Bundle.CTL_errorTitle(), 
                NotifyDescriptor.ERROR_MESSAGE, NotifyDescriptor.ERROR_MESSAGE, 
                null, null);
        getDialogDisplayer().notify(nd);
    }

    /**
     * Show an information dialog
     * @param message
     */
    public static void info(String message) {
        show(message, MessageType.INFO);
    }

    /**
     * Show an error dialog
     * @param message
     */
    public static void error(String message) {
        show(message, MessageType.ERROR);
    }

    /**
     * Show an error dialog for an exception
     * @param message
     * @param exception
     */
    public static void error(String message, Throwable exception) {
        Exceptions.printStackTrace(exception);
        //showException(message, exception);
    }

    /**
     * Show an question dialog
     * @param message
     */
    public static void question(String message) {
        show(message, MessageType.QUESTION);
    }

    /**
     * Show an warning dialog
     * @param message
     */
    public static void warn(String message) {
        show(message, MessageType.WARNING);
    }

    /**
     * Show an plain dialog
     * @param message
     */
    public static void plain(String message) {
        show(message, MessageType.PLAIN);
    }
}
