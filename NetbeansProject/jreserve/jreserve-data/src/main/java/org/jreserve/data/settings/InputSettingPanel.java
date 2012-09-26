package org.jreserve.data.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.data.util.DataImportSettings;
import org.jreserve.data.util.DateFormatCombo;
import org.jreserve.data.util.NumberFormatCombo;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.InputSettingPanel.DateFormat=Date format:",
    "LBL.InputSettingPanel.NumberFormat=Number format:",
    "LBL.InputSettingPanel.DecimalSeparatort=Decimal separator:",
    "LBL.InputSettingPanel.ThousandSeparator=Thousand separator:",
    "MSG.InputSettingPanel.InvalidDateFormat=Date format is invalid!",
    "MSG.InputSettingPanel.InvalidNumberFormat=Number format is invalid!",
    "MSG.InputSettingPanel.InvalidDecimalSeparator=Decimal separator is invalid!",
    "MSG.InputSettingPanel.InvalidThousandSeparator=Thousand separatoris invalid!"
})
class InputSettingPanel extends JPanel implements ActionListener, DocumentListener {

    private final static String DATE_ACTION = "DATE_ACTION";
    private final static String NUMBER_ACTION = "NUMBER_ACTION";
    private final static String ERROR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private DateFormatCombo dateFormatCombo;
    private JLabel dateLabel;
    private NumberFormatCombo numberFormatCombo;
    private JTextField decimalSeparator;
    private JTextField thousandSeparator;
    private JLabel numberLabel;
    private JLabel msgLabel;
    private boolean isValid = false;
    
    private final Date now = new Date();
    private final double positiveNumber = 1265.4356;
    private final double negativeNumber = -1265.4356;
    private DecimalFormatSymbols decimalSymbols = null;
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    InputSettingPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.weightx=0d; gc.weighty=0d;
        gc.fill=GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_InputSettingPanel_DateFormat()), gc);
        
        gc.gridy=1;
        add(new JLabel(Bundle.LBL_InputSettingPanel_DateFormat()), gc);
        
        gc.gridy=2;
        add(new JLabel(Bundle.LBL_InputSettingPanel_DecimalSeparatort()), gc);
        
        gc.gridy=3;
        gc.insets = new Insets(0, 0, 5, 0);
        add(new JLabel(Bundle.LBL_InputSettingPanel_ThousandSeparator()), gc);
        
        gc.gridx=1; gc.gridy=0;
        gc.weightx=1d;
        gc.fill=GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 5, 5);
        dateFormatCombo = new DateFormatCombo();
        dateFormatCombo.setActionCommand(DATE_ACTION);
        dateFormatCombo.addActionListener(this);
        add(dateFormatCombo, gc);
        
        gc.gridy=1;
        numberFormatCombo = new NumberFormatCombo();
        numberFormatCombo.setActionCommand(NUMBER_ACTION);
        numberFormatCombo.addActionListener(this);
        add(numberFormatCombo, gc);
        
        gc.gridy=2;
        decimalSeparator = new JTextField();
        decimalSeparator.getDocument().addDocumentListener(this);
        add(decimalSeparator, gc);
        
        gc.gridy=3;
        gc.insets = new Insets(0, 0, 0, 5);
        thousandSeparator = new JTextField();
        thousandSeparator.getDocument().addDocumentListener(this);
        add(thousandSeparator, gc);
        
        gc.gridx=2; gc.gridy=0;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.weightx=0d;
        gc.insets = new Insets(0, 0, 5, 0);
        dateLabel = new JLabel();
        add(dateLabel, gc);
        
        gc.gridy=1;
        numberLabel = new JLabel();
        add(numberLabel, gc);
         
        gc.gridx=0; gc.gridy=4;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.weightx=1d; gc.weighty=1d;
        gc.fill=GridBagConstraints.BOTH;
        gc.gridwidth=3;
        gc.insets = new Insets(0, 0, 0, 0);
        add(Box.createGlue(), gc);
        
        gc.gridx=0; gc.gridy=5;
        gc.weighty=0d;
        gc.fill=GridBagConstraints.HORIZONTAL;
        
        msgLabel = new JLabel();
        msgLabel.setIcon(ImageUtilities.loadImageIcon(ERROR_ICON, false));
        msgLabel.setVisible(false);
        add(msgLabel, gc);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setNumberFormat();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setNumberFormat();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(DATE_ACTION.equals(command)) {
            setDateFormat();
        } else if(NUMBER_ACTION.equals(command)) {
            setNumberFormat();
        }
    }
    
    private void setDateFormat() {
        DateFormat format = dateFormatCombo.getFormat();
        if(format != null)
            dateLabel.setText(format.format(now));
        validateInput();
    }
    
    private void setNumberFormat() {
        DecimalFormat format = numberFormatCombo.getFormat();
        if(format != null && isDecimalSeparatorValid() && isThousandSeparatorValid())
            setNumberFormat(format);
        validateInput();
    }
    
    private void setNumberFormat(DecimalFormat format) {
        format.setDecimalFormatSymbols(decimalSymbols);
        String positive = format.format(positiveNumber);
        String negative = format.format(negativeNumber);
        String number = ""+positiveNumber;
        String msg = String.format("%s | %s | %s", number, positive, negative);
        numberLabel.setText(msg);
    }
    
    private void validateInput() {
        isValid = isDateFormatValid() && isNumberFormatValid() & 
                isDecimalSeparatorValid() && isThousandSeparatorValid();
        if(isValid)
            showError(null);
        fireChange();
    }
    
    private boolean isDateFormatValid() {
        if(dateFormatCombo.isValidFormat())
            return true;
        showError(Bundle.MSG_InputSettingPanel_InvalidDateFormat());
        return false;
    }
    
    private void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(msg != null);
    }
    
    private boolean isNumberFormatValid() {
        if(numberFormatCombo.isValidFormat())
            return true;
        showError(Bundle.MSG_InputSettingPanel_InvalidNumberFormat());
        return false;
    }
    
    private boolean isDecimalSeparatorValid() {
        String str = decimalSeparator.getText();
        if(isCharacter(str)) {
            decimalSymbols.setDecimalSeparator(str.charAt(0));
            return true;
        }
        showError(Bundle.MSG_InputSettingPanel_InvalidDecimalSeparator());
        return false;
    }
    
    private boolean isCharacter(String str) {
        return str != null &&
               str.trim().length() == 1 &&
               str.length() == 1;
    }
    
    private boolean isThousandSeparatorValid() {
        String str = thousandSeparator.getText();
        if(str != null && str.length()==1) {
            decimalSymbols.setGroupingSeparator(str.charAt(0));
            return true;
        }
        showError(Bundle.MSG_InputSettingPanel_InvalidThousandSeparator());
        return false;
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    void load() {
        decimalSymbols = DataImportSettings.getDecimalSymbols();
        decimalSeparator.setText(""+decimalSymbols.getDecimalSeparator());
        thousandSeparator.setText(""+decimalSymbols.getGroupingSeparator());
        
        dateFormatCombo.setSelectedItem(DataImportSettings.getDateFormat());
        numberFormatCombo.setSelectedItem(DataImportSettings.getDecimalFormat());
        validateInput();
    }
    
    void store() {
        DataImportSettings.setDateFormat(dateFormatCombo.getFormatValue());
        DataImportSettings.setDecimalFormat(numberFormatCombo.getFormatValue());
        DataImportSettings.setDecimalSeparator(decimalSymbols.getDecimalSeparator());
        DataImportSettings.setThousandSeparator(decimalSymbols.getGroupingSeparator());
    }

    boolean isValidSettings() {
        return isValid;
    }
}

