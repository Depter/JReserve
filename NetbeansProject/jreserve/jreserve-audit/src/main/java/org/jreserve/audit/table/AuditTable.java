package org.jreserve.audit.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputMap;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jreserve.audit.AuditElement;
import org.jreserve.audit.Auditable;
import org.jreserve.audit.Auditor;
import org.jreserve.audit.util.AuditorRegistry;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.persistence.SessionFactory;
import org.openide.util.Exceptions;

public class AuditTable extends javax.swing.JPanel {
    
    private final static Logger logger = Logger.getLogger(AuditTable.class.getName());
    
    private AuditChangeLoader loader = null;
    private Auditable auditable;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private DateFormat df;
    
    public AuditTable() {
        initDateFormat();
        initComponents();
    }
    
    private void initDateFormat() {
        String format = LocaleSettings.getDateFormatString();
        format += " hh:mm:ss";
        Locale locale = LocaleSettings.getLocale();
        df = new SimpleDateFormat(format, locale);
    }
    
    public void setAuditable(Auditable auditable) {
        if(isNewAuditable(auditable)) {
            this.auditable = auditable;
            checkAudit();
        }
    }
    
    private boolean isNewAuditable(Auditable auditable) {
        if(this.auditable == null)
            return auditable != null;
        return this.auditable != auditable;
    }

    private void checkAudit() {
        stopLoader();
        if(auditable != null)
            startLoader(auditable);
        else
            table.setModel(new ChangeTableModel());
    }
    
    public void stopLoader() {
        if(loader != null) {
            loader.cancel(true);
            loader = null;
        }
    }
    
    private void startLoader(Auditable auditable) {
        table.setModel(LoadingTableModel.getInstance());
        loader = new AuditChangeLoader(auditable.getAuditableEntity());
        fireChangeEvent();
        loader.execute();
    }

    public InputMap getTableInputMap() {
        return table.getInputMap();
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    public List<AuditElement> getChanges() {
        TableModel model = table.getModel();
        if(model instanceof ChangeTableModel)
            return ((ChangeTableModel) model).getChanges();
        return Collections.EMPTY_LIST;
    }
    
    public DateFormat getDateFormat() {
        return df;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        table.setModel(new ChangeTableModel());
        table.setDefaultRenderer(Date.class, new DateCellRenderer(df));
        tableScroll.setViewportView(table);

        add(tableScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
    
    private class AuditChangeLoader extends SwingWorker<List<AuditElement>, Void> {

        private final Object value;
        private AuditReader reader;
        private Session session;
        
        private AuditChangeLoader(Object value) {
            this.value = value;
        }
        
        @Override
        protected List<AuditElement> doInBackground() throws Exception {
            try {
                if(!SessionFactory.isConnected())
                    return Collections.EMPTY_LIST;
                openAuditReader();
                return getAuditElements();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to get audits for: {0}!", value);
                Exceptions.printStackTrace(ex);
                throw ex;
            } finally {
                closeSession();
            }
        }
        
        private void openAuditReader() {
            session = SessionFactory.openSession();
            reader = AuditReaderFactory.get(session);
        }
        
        private List<AuditElement> getAuditElements() {
            List<AuditElement> changes = new ArrayList<AuditElement>();
            for(Auditor auditor : AuditorRegistry.getAuditors(value))
                changes.addAll(auditor.getAudits(reader, value));
            return changes;
        }
        
        private void closeSession() {
            if(session != null) {
                session.close();
                session = null;
            }
        }

        @Override
        protected void done() {
            try {
                setChanges(get());
            } catch (Exception ex) {
                setChanges(Collections.EMPTY_LIST);
            }
        }
        
        private void setChanges(List<AuditElement> changes) {
            table.setModel(new ChangeTableModel(changes));
            loader = null;
            fireChangeEvent();
        }
    }

}
