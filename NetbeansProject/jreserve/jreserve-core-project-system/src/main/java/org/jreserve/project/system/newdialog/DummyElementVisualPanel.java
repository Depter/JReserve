/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.project.system.newdialog;

import javax.swing.JPanel;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_dummyPanelName=..."
})
final class DummyElementVisualPanel extends JPanel {

    /**
     * Creates new form NewProjectElementVisualPanel2
     */
    public DummyElementVisualPanel() {
        initComponents();
    }

    @Override
    public String getName() {
        return Bundle.LBL_dummyPanelName();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}