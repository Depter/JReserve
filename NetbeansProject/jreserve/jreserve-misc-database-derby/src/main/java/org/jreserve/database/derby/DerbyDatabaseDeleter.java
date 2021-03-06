package org.jreserve.database.derby;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL_deleteDialogTitle=Delete database",
    "# {0} - Name of the database.",
    "CTL_deleteDialogQuestion=Also delete database at:\n{0}",
    "CTL_deleteDialogErrorTitle=Delete database",
    "# {0} - Location of the database.",
    "CTL_deleteDialogErrorMessage=Unable to delete database at:\n{0}"
})
class DerbyDatabaseDeleter {
    
    private final static Logger logger = Logger.getLogger(DerbyDatabaseDeleter.class.getName());
    
    private File dbHome;

    DerbyDatabaseDeleter(String dbHome) {
        this.dbHome = new File(dbHome);
    }
    
    void deleteDatabase() {
        if(dbHome.exists() && shouldDeleteDatabase()) {
            logger.log(Level.INFO, "Deleting database at: \"{0}\"", dbHome.getAbsolutePath());
            if(!deleteFile(dbHome))
                showWarning();
        }
    }
    
    private boolean shouldDeleteDatabase() {
        NotifyDescriptor nd = new NotifyDescriptor(
           Bundle.CTL_deleteDialogQuestion(dbHome), Bundle.CTL_deleteDialogTitle(), 
           NotifyDescriptor.YES_NO_OPTION, NotifyDescriptor.QUESTION_MESSAGE, 
           null, NotifyDescriptor.NO_OPTION);
        return DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.YES_OPTION;
    }
    
    private boolean deleteFile(File file) {
        File[] children = file.listFiles();
        if(children != null && !deleteChildren(children))
            return false;
        return deleteSingleFile(file);
    }
    
    private boolean deleteChildren(File[] children) {
        for(File child : children)
            if(!deleteFile(child))
                return false;
        return true;
    }
    
    private boolean deleteSingleFile(File file) {
        file.delete();
        if(file.exists()) {
            logger.log(Level.WARNING, "Unable to delete file: \"{0}\"", file.getAbsolutePath());
            return false;
        } else {
            logger.log(Level.FINER, "Deleting file: \"{0}\"", file.getAbsolutePath());
            return true;
        }
    }
    
    private void showWarning() {
        NotifyDescriptor nd = new NotifyDescriptor(
           Bundle.CTL_deleteDialogErrorMessage(dbHome), Bundle.CTL_deleteDialogErrorTitle(), 
           NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.WARNING_MESSAGE, 
           new Object[]{NotifyDescriptor.OK_OPTION}, NotifyDescriptor.OK_OPTION);
        DialogDisplayer.getDefault().notify(nd);
    }
}
