/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.database.explorer;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.jreserve.persistence.databaseexplorer//DatabaseExplorer//EN",
autostore = false)
@TopComponent.Description(preferredID = "DatabaseExplorerTopComponent",
iconBase = "org/jreserve/persistence/database/database.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.jreserve.persistence.databaseexplorer.DatabaseExplorerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_DatabaseExplorerAction",
preferredID = "DatabaseExplorerTopComponent")
@Messages({
    "CTL_DatabaseExplorerAction=Databases",
    "CTL_DatabaseExplorerTopComponent=Database",
    "HINT_DatabaseExplorerTopComponent=Manage databases"
})
public final class DatabaseExplorerTopComponent extends TopComponent implements ExplorerManager.Provider {

    private final static ExplorerManager em = new ExplorerManager();
    
    public DatabaseExplorerTopComponent() {
        initComponents();
        setName(Bundle.CTL_DatabaseExplorerTopComponent());
        setToolTipText(Bundle.HINT_DatabaseExplorerTopComponent());
        em.setRootContext(new AbstractNode(Children.create(new DbExplorerRootChildFactory(), true)));
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        beanTreeView1.setRootVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(beanTreeView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(beanTreeView1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView beanTreeView1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}