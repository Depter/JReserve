package org.jreserve.audit.context;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultEditorKit;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jreserve.audit.AuditElement;
import org.jreserve.audit.Auditable;
import org.jreserve.audit.Auditor;
import org.jreserve.audit.util.AuditorRegistry;
import org.jreserve.persistence.SessionFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.actions.CopyAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.util.*;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.audit.context//ContextAuditor//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "ContextAuditorTopComponent",
    iconBase = "resources/audit.png",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED
)
@TopComponent.Registration(
    mode = "auditor", 
    openAtStartup = false
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.audit.context.ContextAuditorTopComponent"
)
@ActionReference(
    path = "Menu/Window",
    position = 450
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_ContextAuditorAction",
    preferredID = "ContextAuditorTopComponent"
)
@Messages({
    "CTL_ContextAuditorAction=Context Auditor",
    "CTL_ContextAuditorTopComponent=Context Auditor Window"
})
public final class ContextAuditorTopComponent extends TopComponent implements LookupListener, Lookup.Provider {

    private final static String NO_PATH = "-";
    
    private ChangeTableModel tableModel = new ChangeTableModel();
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    
    private Lookup.Result<Auditable> result;
    private AuditChangeLoader loader = null;

    public ContextAuditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_ContextAuditorTopComponent());
        initAuditChecking();
        registerCopyAction();
    }

    private void initAuditChecking() {
        result = Utilities.actionsGlobalContext().lookupResult(Auditable.class);
        result.addLookupListener(this);
        checkAudit();
    }
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke("control C");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        table.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyDataAction());
    }
    
    @Override
    public void resultChanged(LookupEvent le) {
        checkAudit();
    }

    private void checkAudit() {
        Auditable auditor = getAuditable();
        setNameText(auditor);
        setLookupContent(auditor);
        loadChanges(auditor);
    }
    
    private void stopLoader() {
        if(loader != null) {
            loader.cancel(true);
            loader = null;
        }
    }
    
    private Auditable getAuditable() {
        List<Auditable> auditors = new ArrayList<Auditable>(result.allInstances());
        if(auditors.isEmpty())
            return null;
        return auditors.get(0);
    }
    
    private void setNameText(Auditable auditor) {
        String path = auditor==null? NO_PATH : auditor.getProjectElement().getNamePath();
        pathText.setText(path);
    }
    
    private void setLookupContent(Auditable auditable) {
        clearLookup();
        addToMyLookup(auditable);
    }
    
    private void clearLookup() {
        Collection c = lookup.lookupAll(Object.class);
        for(Object o : c)
            ic.remove(o);
    }
    
    private void addToMyLookup(Auditable auditable) {
        if(auditable != null)
            ic.add(auditable);
    }
    
    private void loadChanges(Auditable auditable) {
        stopLoader();
        if(auditable != null)
            startLoader(auditable);
        else
            table.setModel(new ChangeTableModel());
    }
    
    private void startLoader(Auditable auditable) {
        copyButton.setEnabled(false);
        table.setModel(LoadingTableModel.getInstance());
        loader = new AuditChangeLoader(auditable.getProjectElement().getValue());
        loader.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        northPanel = new javax.swing.JPanel();
        pathLabel = new javax.swing.JLabel();
        pathText = new javax.swing.JLabel();
        copyButton = new org.jreserve.resources.ToolBarButton(SystemAction.get(CopyAction.class));
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        northPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        northPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(ContextAuditorTopComponent.class, "LBL.ContextAuditorTopComponent.Path")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        northPanel.add(pathLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(pathText, NO_PATH);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        northPanel.add(pathText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(copyButton, null);
        copyButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        northPanel.add(copyButton, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.NORTH);

        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setDefaultRenderer(java.util.Date.class, new DateCellRenderer());
        tableScroll.setViewportView(table);

        add(tableScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jreserve.resources.ToolBarButton copyButton;
    private javax.swing.JPanel northPanel;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JLabel pathText;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        stopLoader();
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
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
                Exceptions.printStackTrace(ex);
            }
        }
        
        private void setChanges(List<AuditElement> changes) {
            copyButton.setEnabled(!changes.isEmpty());
            table.setModel(new ChangeTableModel(changes));
            loader = null;
        }
    }

    
    private class CopyDataAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

}
