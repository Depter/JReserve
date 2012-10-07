package org.jreserve.triangle.entities.vectorimport;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import org.jreserve.triangle.importutil.AxisGeometryPanel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.VectorFormatVisualPanel.PanelName=Geometry",
    "LBL.VectorFormatVisualPanel.Geometry=Geometry"
})
class VectorFormatVisualPanel extends JPanel {

    private AxisGeometryPanel geometry;
    private JTable table;
    
    VectorFormatVisualPanel() {
        setName(Bundle.LBL_VectorFormatVisualPanel_PanelName());
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        
        geometry = new AxisGeometryPanel(Bundle.LBL_VectorFormatVisualPanel_Geometry());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx=0d; gc.weighty=0d;
        gc.insets = new Insets(0, 0, 5, 0);
        add(geometry, gc);
        
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 1; gc.gridy = 0;
        gc.weightx=0d; gc.weighty=1d;
        gc.insets = new Insets(0, 0, 5, 0);
        add(Box.createHorizontalGlue(), gc);
        
        table = new JTable();
        table.setPreferredSize(new Dimension(250, 250));
        table.setFillsViewportHeight(true);
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth=2;
        gc.weightx=1d; gc.weighty=1d;
        gc.fill=GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 0, 0);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(250, 250));
        add(scroll, gc);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
