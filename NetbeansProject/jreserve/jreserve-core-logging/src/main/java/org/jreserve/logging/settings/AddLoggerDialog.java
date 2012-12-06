package org.jreserve.logging.settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.AddLoggerDialog.title=Configure logger",
    "LBL.AddLoggerDialog.logger=Logger:",
    "LBL.AddLoggerDialog.level=Level:",
    "CTL.AddLoggerDialog.add=Add",
    "CTL.AddLoggerDialog.cancel=Cancel",
    "MSG.AddLoggerDialog.allloggers=All loggers",
    "MSG.AddLoggerDialog.logger.empty=Field 'Logger' is empty!",
    "MSG.AddLoggerDialog.logger.point=Name of logger can not end with '.'!",
    "MSG.AddLoggerDialog.level.empty=Field 'Level' is empty!",
    "# {0} - the level, typed by the user",
    "MSG.AddLoggerDialog.level.invalid=Level \"{0}\" is invalid!"
})
class AddLoggerDialog extends JPanel implements ActionListener, DocumentListener, ListSelectionListener {
    
    static void showDialog(LogLevelTableModel tableModel) {
        AddLoggerDialog panel = new AddLoggerDialog(tableModel);
        DialogDescriptor dd = createDescriptor(panel);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        panel.dialog = dialog;
        dialog.setVisible(true);
    }
    
    private static DialogDescriptor createDescriptor(AddLoggerDialog panel) {
        JButton[] buttons = new JButton[]{panel.addButton, panel.cancelButton};
        DialogDescriptor dd = new DialogDescriptor(
                panel, Bundle.LBL_AddLoggerDialog_title(), true, 
                buttons, panel.cancelButton, DialogDescriptor.DEFAULT_ALIGN, 
                HelpCtx.DEFAULT_HELP, null);
        return dd;
    }
    
    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private JTextField loggerName;
    private JTextField levelText;
    private JList loggerList;
    private JLabel msgLabel;
    private JButton addButton;
    private JButton cancelButton;
    private LoggerListModel listModel;
    private LogLevelTableModel tableModel;
    private Dialog dialog;
    
    private AddLoggerDialog(LogLevelTableModel tableModel) {
        this.tableModel = tableModel;
        initComponents();
        inputChanged();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(getInputPanel(), BorderLayout.NORTH);
        add(getLoggerList(), BorderLayout.CENTER);
        add(getSouthPanel(), BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0; c.gridy=0;
        c.anchor=GridBagConstraints.NORTHWEST;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=0d;c.weighty=0d;
        c.insets = new Insets(0, 0, 5, 5);
        panel.add(new JLabel(Bundle.LBL_AddLoggerDialog_logger()), c);
        
        c.gridy=1;
        c.insets = new Insets(0, 0, 0, 5);
        panel.add(new JLabel(Bundle.LBL_AddLoggerDialog_level()), c);
        
        c.gridx=1; c.gridy=0;
        c.weightx=1d;
        c.insets = new Insets(0, 0, 5, 0);
        loggerName = new JTextField();
        loggerName.getDocument().addDocumentListener(this);
        panel.add(loggerName, c);
        
        c.gridy=1;
        c.insets = new Insets(0, 0, 0, 0);
        levelText = new JTextField();
        levelText.getDocument().addDocumentListener(this);
        panel.add(levelText, c);

        return panel;
    }
    
    private JScrollPane getLoggerList() {
        listModel = new LoggerListModel();
        loggerList = new JList(listModel);
        loggerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loggerList.getSelectionModel().addListSelectionListener(this);
        
        JScrollPane scroll = new JScrollPane(loggerList);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scroll.setPreferredSize(new Dimension(400, 300));
        return scroll;
    }
    
    private JPanel getSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.add(getMsgLabel(), BorderLayout.PAGE_START);
        createButtons();
        return panel;
    }
    
    private JPanel getMsgLabel() {
        ImageIcon icon = ImageUtilities.loadImageIcon(ERR_ICON, false);
        msgLabel = new JLabel(icon);
        msgLabel.setText("Test text!");
        msgLabel.setVisible(false);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(msgLabel, BorderLayout.LINE_START);
        panel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        return panel;
    }
    
    private void createButtons() {
        addButton = new JButton(Bundle.CTL_AddLoggerDialog_add());
        addButton.setEnabled(false);
        addButton.addActionListener(this);
        
        cancelButton = new JButton(Bundle.CTL_AddLoggerDialog_cancel());
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(cancelButton == source) {
            dialog.dispose();
        } else if(addButton == source) {
            addLoggerToTable();
            dialog.dispose();
        }
    }

    private void addLoggerToTable() {
        String logger = loggerName.getText();
        Level level = Level.parse(levelText.getText());
        tableModel.addValue(logger, level);
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        inputChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        inputChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void inputChanged() {
        String logger = loggerName.getText();
        String level = levelText.getText();
        boolean valid = checkLoggerName(logger) && checkLevel(level);
        addButton.setEnabled(valid);
        msgLabel.setVisible(!valid);
        listModel.setRootName(logger);
    }
    
    private boolean checkLoggerName(String name) {
        if(isEmpty(name)) {
            showError(Bundle.MSG_AddLoggerDialog_logger_empty());
            return false;
        }
        if(name.endsWith(".")) {
            showError(Bundle.MSG_AddLoggerDialog_logger_point());
            return false;
        }
        return true;
    }
    
    private boolean isEmpty(String str) {
        return str==null || str.trim().length() == 0;
    }
    
    private void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(true);
    }
    
    private boolean checkLevel(String level) {
        if(isEmpty(level)) {
            showError(Bundle.MSG_AddLoggerDialog_level_empty());
            return false;
        }
        try {
            Level.parse(level);
            return true;
        } catch (IllegalArgumentException ex) {
            showError(Bundle.MSG_AddLoggerDialog_level_invalid(level));
            return false;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        String selected = (String) loggerList.getSelectedValue();
        if(selected != null)
            loggerName.setText(selected);
    }
    
    private static class LoggerListModel extends AbstractListModel {
        
        private List<String> loggers = new ArrayList<String>();

        private LoggerListModel() {
            loggers.add(Bundle.MSG_AddLoggerDialog_allloggers());
        }
        
        @Override
        public int getSize() {
            return loggers==null? 0 : loggers.size();
        }

        @Override
        public Object getElementAt(int index) {
            return loggers.get(index);
        }
        
        private void setRootName(String name) {
            clearList();
            setContent(name);
            fireContentChange();
        }
        
        private void clearList() {
            if(loggers.isEmpty())
                return;
            int size = loggers.size();
            loggers.clear();
            super.fireIntervalRemoved(this, 0, size-1);
        }
        
        private void setContent(String name) {
            if(name==null || name.trim().length()==0) {
                loggers.add(Bundle.MSG_AddLoggerDialog_allloggers());
            } else {
                addLoggers(name, LogManager.getLogManager().getLoggerNames());
            }
        }
        
        private void addLoggers(String name, Enumeration<String> names) {
            while(names.hasMoreElements()) {
                String loggerName = names.nextElement();
                if(loggerName.startsWith(name))
                    loggers.add(loggerName);
            }
            Collections.sort(loggers);
        }
        
        private void fireContentChange() {
            int size = loggers.size();
            super.fireIntervalAdded(this, 0, size);
        }
    }
}
