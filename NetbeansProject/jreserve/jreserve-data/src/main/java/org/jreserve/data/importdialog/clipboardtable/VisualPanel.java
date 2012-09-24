package org.jreserve.data.importdialog.clipboardtable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.project.entities.Project;
import org.openide.NotificationLineSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.VisualPanel.name=Import table",
    "LBL.VisualPanel.project=Project:",
    "LBL.VisualPanel.DataType=Data type:",
    "LBL.VisualPanel.DateFormat=Date format:",
    "# {0} - the date format",
    "MSG.VisualPanel.Error.DateFormat=Date format \"{0}\" is invalid!",
    "LBL.VisualPanel.Paste=Paste"
})
class VisualPanel extends JPanel implements ActionListener, DocumentListener {

    private final static String DATA_TYPE_SELECT_ACTION = "DATA_TYPE_SELECTED";
    private final static String PASTE_ACTION = "PASTE";
    private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    private JLabel projectLabel;
    
    private ProjectDataTypeComboModel comboModel;
    private JComboBox dataTypeCombo;
    
    private final Date now = new Date();
    private DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    private JTextField dateFormatText;
    private JLabel dateLabel;
    
    private DataTableModel tableModel;
    private JTable dataTable;
    private NotificationLineSupport nlSupport;
    
    VisualPanel() {
        initComponents();
        setName(Bundle.LBL_VisualPanel_name());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(getInputPanel(), BorderLayout.NORTH);
        add(getTableScroll(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 5, 5);
        panel.add(new JLabel(Bundle.LBL_VisualPanel_project()), gc);
        
        gc.gridy=1;
        panel.add(new JLabel(Bundle.LBL_VisualPanel_DataType()), gc);
        
        gc.gridy=2;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(new JLabel(Bundle.LBL_VisualPanel_DateFormat()), gc);
        
        gc.gridx=1; gc.gridy=0;
        gc.weightx=1d;
        gc.insets = new Insets(0, 0, 5, 5);
        projectLabel = new JLabel();
        panel.add(projectLabel, gc);
        
        gc.gridy=1;
        comboModel = new ProjectDataTypeComboModel();
        dataTypeCombo = new JComboBox(comboModel);
        dataTypeCombo.setActionCommand(DATA_TYPE_SELECT_ACTION);
        dataTypeCombo.setRenderer(new ProjectDataTypeComboRenderer());
        dataTypeCombo.addActionListener(this);
        panel.add(dataTypeCombo, gc);
        
        gc.gridy=2;
        gc.insets = new Insets(0, 0, 0, 5);
        dateFormatText = new JTextField(DEFAULT_DATE_FORMAT);
        dateFormatText.getDocument().addDocumentListener(this);
        panel.add(dateFormatText, gc);
        
        
        gc.gridx=2;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.weightx=0d;
        dateLabel = new JLabel(dateFormat.format(now));
        panel.add(dateLabel, gc);
        
        return panel;
    }
    
    private JScrollPane getTableScroll() {
        tableModel = new DataTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setFillsViewportHeight(true);
        dataTable.addMouseListener(new PopUpMenu());
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        dataTable.getInputMap().put(stroke, PASTE_ACTION);
        dataTable.setFocusable(true);
        
        JScrollPane scroll = new JScrollPane(dataTable);
        scroll.getInputMap().put(stroke, PASTE_ACTION);
        return scroll;
    }
    
    void setProject(Project project) {
        String name = project==null? null : project.getName();
        projectLabel.setText(name);
        comboModel.setProject(project);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(DATA_TYPE_SELECT_ACTION.equals(action)) {
            setDataType();
        } else if(PASTE_ACTION.equals(action)) {
            pasteFromClipboard();
        }
    }
    
    private void setDataType() {
        ProjectDataType type = (ProjectDataType) dataTypeCombo.getSelectedItem();
        tableModel.setDataType(type);
    }
    
    private void pasteFromClipboard() {
        String data = getDataFromClipboard();
        if(data != null)
            tableModel.setClipboardData(data);
    }

    private String getDataFromClipboard() {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            return (String) cb.getData(DataFlavor.stringFlavor); 
        } catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        dateFormatChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        dateFormatChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void dateFormatChanged() {
        String format = dateFormatText.getText();
        if(isValidFormat(format))
            dateLabel.setText(dateFormat.format(now));
    }
    
    private boolean isValidFormat(String format) {
        try {
            dateFormat = new SimpleDateFormat(format);
            return true;
        } catch (RuntimeException ex) {
            showError(Bundle.MSG_VisualPanel_Error_DateFormat(format));
            return false;
        }
    }
    
    private void showError(String msg) {
        if(nlSupport != null)
            nlSupport.setErrorMessage(msg);
    }
    
    private void clearError() {
        if(nlSupport != null)
            nlSupport.clearMessages();
    }
    
    private class PopUpMenu extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }
        
        private void popUp(MouseEvent evt) {
            JPopupMenu popUp = buildMenu();
            popUp.show(dataTable, evt.getX(), evt.getY());
        }
        
        private JPopupMenu buildMenu() {
            JPopupMenu menu = new JPopupMenu();
            menu.add(getPasteItem());
            return menu;
        }
        
        private JMenuItem getPasteItem() {
            JMenuItem item = new JMenuItem(Bundle.LBL_VisualPanel_Paste());
            item.setActionCommand(PASTE_ACTION);
            item.addActionListener(VisualPanel.this);
            return item;
        }
    }
}
