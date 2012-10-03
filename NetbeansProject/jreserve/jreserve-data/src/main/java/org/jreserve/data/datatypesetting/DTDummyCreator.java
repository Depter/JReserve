package org.jreserve.data.datatypesetting;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.resources.textfieldfilters.IntegerFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DTDummyCreator.id=Database id:",
    "LBL.DTDummyCreator.name=Name:",
    "LBL.DTDummyCreator.istriangle=Triangle:",
    "CTL.DTDummyCreator.add=Add",
    "CTL.DTDummyCreator.cancel=Cancel",
    "MSG.DTDummyCreator.id.empty=Field 'id' is empty!",
    "# {0} - the id",
    "MSG.DTDummyCreator.id.exists=Id \"{0}\" already exists!",
    "MSG.DTDummyCreator.name.empty=Filed 'name' is empty!",
    "# {0} - the name",
    "MSG.DTDummyCreator.name.exists=Name \"{0}\" is already exists!"
})
public class DTDummyCreator extends JPanel implements DocumentListener {

    static void showDialog(DataTypeTableModel model) {
        DTDummyCreator creator = new DTDummyCreator(model);
        DialogDescriptor dd = createDescriptor(creator);
        Object option = DialogDisplayer.getDefault().notify(dd);
        if(option == creator.addButton)
            createDTDummy(creator);
    }
    
    private static DialogDescriptor createDescriptor(DTDummyCreator creator) {
        JButton[] buttons = new JButton[]{creator.addButton, creator.cancelButton}; 
        DialogDescriptor dd = new DialogDescriptor(creator, "Title", true, 
            buttons, creator.cancelButton, 
            DialogDescriptor.DEFAULT_ALIGN, null, null);
        dd.setClosingOptions(null);
        return dd;
    }
    
    private static void createDTDummy(DTDummyCreator creator) {
        Object[] row = new Object[] {Integer.parseInt(creator.idText.getText()),
        creator.nameText.getText(), creator.isTriangleCheck.isSelected()};
        creator.model.addRow(row);
    }
    
    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";

    private DataTypeTableModel model;
    private JTextField idText;
    private JTextField nameText;
    private JCheckBox isTriangleCheck;
    private JLabel msgLabel;
    private JButton addButton;
    private JButton cancelButton;
    
    private DTDummyCreator(DataTypeTableModel model) {
        this.model = model;
        initContents();
        initButtons();
        checkInput();
    }
    
    private void initContents() {
        setLayout(new BorderLayout(15, 15));
        
        add(getInputPanel(), BorderLayout.NORTH);
        add(Box.createGlue(), BorderLayout.CENTER);
        add(getMsgLabel(), BorderLayout.SOUTH);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy = 0;
        gc.weightx = 0d; gc.weighty = 0d;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(0, 0, 5, 5);
        panel.add(new JLabel(Bundle.LBL_DTDummyCreator_id()), gc);

        gc.gridy = 1;
        panel.add(new JLabel(Bundle.LBL_DTDummyCreator_name()), gc);
        
        gc.insets = new Insets(0, 0, 0, 5);
        gc.gridy = 2;
        panel.add(new JLabel(Bundle.LBL_DTDummyCreator_istriangle()), gc);
        
        gc.gridx=1; gc.gridy = 0;
        gc.weightx = 1d;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 5, 0);
        idText = new JTextField();
        idText.setDocument(new IntegerFilter());
        idText.setPreferredSize(new Dimension(150, 20));
        idText.getDocument().addDocumentListener(this);
        panel.add(idText, gc);
        
        gc.gridy = 1;
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(150, 20));
        nameText.getDocument().addDocumentListener(this);
        panel.add(nameText, gc);
        
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, 0, 0);
        isTriangleCheck = new JCheckBox();
        panel.add(isTriangleCheck, gc);
        
        return panel;
    }
    
    private void initButtons() {
        addButton = new JButton(Bundle.CTL_DTDummyCreator_add());
        cancelButton = new JButton(Bundle.CTL_DTDummyCreator_cancel());
    }
    
    private JPanel getMsgLabel() {
        ImageIcon icon = ImageUtilities.loadImageIcon(ERR_ICON, false);
        msgLabel = new JLabel(icon);
        msgLabel.setText("Test text!");
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(msgLabel, BorderLayout.LINE_START);
        panel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        return panel;
    }
    
    private void checkInput() {
        boolean valid = checkId() && checkName();
        msgLabel.setVisible(!valid);
        addButton.setEnabled(valid);
    }
    
    private boolean checkId() {
        String sId = idText.getText();
        return checkIdNotEmpty(sId) && checkNewId(sId);
    }
    
    private boolean checkIdNotEmpty(String sId) {
        if(isEmpty(sId)) {
            showError(Bundle.MSG_DTDummyCreator_id_empty());
            return false;
        }
        return true;
    }
    
    private boolean isEmpty(String str) {
        return str==null || str.trim().length() == 0;
    }
    
    private void showError(String msg) {
        msgLabel.setText(msg);
    }
    
    private boolean checkNewId(String sId) {
        if(model.getDummy(Integer.parseInt(sId)) != null) {
            showError(Bundle.MSG_DTDummyCreator_id_exists(sId));
            return false;
        }
        return true;
    }
    
    private boolean checkName() {
        String name = nameText.getText();
        return checkNameNotEmpty(name) && checkNameNotExists(name);
    }
    
    private boolean checkNameNotEmpty(String name) {
        if(isEmpty(name)) {
            showError(Bundle.MSG_DTDummyCreator_name_empty());
            return false;
        }
        return true;
    }
    
    private boolean checkNameNotExists(String name) {
        if(model.getDummy(name) != null) {
            showError(Bundle.MSG_DTDummyCreator_name_exists(name));
            return false;
        }
        return true;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
