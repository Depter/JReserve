package org.jreserve.data.settings;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DTDummyCreator.id=Database id:",
    "LBL.DTDummyCreator.name=Name:",
    "LBL.DTDummyCreator.istriangle=Triangle:"
})
class DTDummyCreator extends JPanel {

    private JTextField idText;
    private JTextField nameText;
    private JCheckBox isTriangleCheck;
    
    
    private DTDummyCreator() {
        
        
    }
}
