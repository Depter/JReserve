package org.jreserve.database.util;

import java.awt.Dialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.DefaultListSelectionModel;
import org.jreserve.database.AbstractDatabase;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SelectDatabaseDialog extends DialogDescriptor {
    
    private final static boolean MODAL = true;
    
    private Result<AbstractDatabase> result;
    private List<AbstractDatabase> databases = new ArrayList<AbstractDatabase>();
    private Dialog dialog;
    
    public SelectDatabaseDialog(String title, DatabaseChildren children) {
        this(title, children, SelectionMode.MULTIPLE_INTERVAL);
    }
    
    public SelectDatabaseDialog(String title, DatabaseChildren children, SelectionMode selectionMode) {
        super(new SelectDatabasesPanel(children, selectionMode), 
                title, MODAL,  
                NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.CANCEL_OPTION, null );
        ((SelectDatabasesPanel) getMessage()).addDblClickListener(new DblClickListener());
        setValid(false);
    }
    
    
    @Override
    protected void initialize() {
        ExplorerManager.Provider provider = (ExplorerManager.Provider) super.getMessage();
        Lookup lookup = ExplorerUtils.createLookup(provider.getExplorerManager(), new ActionMap());
        result = lookup.lookupResult(AbstractDatabase.class);
        result.addLookupListener(new DatabaseLookupListener());
    }
    
    public List<AbstractDatabase> getDatabases() {
        openDialog();
        if(getValue() != NotifyDescriptor.OK_OPTION)
            databases.clear();
        return databases;
    }
    
    private void openDialog() {
        dialog = DialogDisplayer.getDefault().createDialog(this);
        dialog.setVisible(true);
    }
    
    private class DatabaseLookupListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            databases.clear();
            databases.addAll(result.allInstances());
            setValid(!databases.isEmpty());
        }
    }
    
    private class DblClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(isValid() && e.getClickCount() > 1) {
                setValue(DialogDescriptor.OK_OPTION);
                dialog.setVisible(false);
            }
        }
    }
    
    public static enum SelectionMode {
        
        SINGLE(DefaultListSelectionModel.SINGLE_SELECTION),
        SINGLE_INTERVAL(DefaultListSelectionModel.SINGLE_INTERVAL_SELECTION),
        MULTIPLE_INTERVAL(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        private int swingValue;
        
        private SelectionMode(int swingValue) {
            this.swingValue = swingValue;
        }
        
        int getSwingValue() {
            return swingValue;
        }
    }
}
