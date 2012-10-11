package org.jreserve.localesettings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.localesettings.util.LocaleSettings;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 */
@NbBundle.Messages({
    "LBL.LocaleSettingPanel.Locale=Locale:",
    "CTL.LocaleSettingPanel.BrowseLocale=Browse",
    "LBL.LocaleSettingPanel.DateFormat=Date format:",
    "LBL.LocaleSettingPanel.NumberFormat=Number format:",
    "LBL.LocaleSettingPanel.DecimalSeparatort=Decimal separator:",
    "LBL.LocaleSettingPanel.ThousandSeparator=Thousand separator:",
    "LBL.LocaleSettingPanel.NaN=Not a number:",
    "CTL.LocaleSettingPanel.ResetLocale=Reset language",
    "CTL.LocaleSettingPanel.Reset=Reset"
})
public class LocaleSettingPanel extends JPanel implements ActionListener, DocumentListener {

    private final static String BROWSE_LOCALE_ACTION = "BROWSE_LOCALE";
    private final static String DATE_ACTION = "DATE_ACTION";
    private final static String NUMBER_ACTION = "NUMBER_ACTION";
    private final static String DECIMAL_ACTION = "DECIMAL_ACTION";
    private final static String GROUPING_ACTION = "GROUPING_ACTION";
    private final static String NAN_ACTION = "NAN_ACTION";
    private final static String RESET_LOCALE_ACTION = "RESET_LOCALE_ACTION";
    private final static String RESET_ACTION = "RESET_ACTION";
    private final static String ERROR_ICON = "org/netbeans/modules/dialogs/error.gif";
    private final static Locale[] LOCALES = Locale.getAvailableLocales();
    static {
        Arrays.sort(LOCALES, new Comparator<Locale>() {
            @Override
            public int compare(Locale l1, Locale l2) {
                String n1 = l1.getDisplayCountry(Locale.ENGLISH);
                String n2 = l2.getDisplayCountry(Locale.ENGLISH);
                return n1.compareToIgnoreCase(n2);
            }
        
        });
    }
    
    
    private boolean isValid = false;
    
    private final Date now = new Date();
    private final double positiveNumber = 1265.4356;
    private final double negativeNumber = -1265.4356;
    private DecimalFormatSymbols decimalSymbols = null;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private LocaleSettingValidator validator = new LocaleSettingValidator(this);
    private Locale locale;
    
    public LocaleSettingPanel() {
        initComponents();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        fireActionEvent(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        fireActionEvent(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void fireActionEvent(DocumentEvent e) {
        String command = getActionCommand(e.getDocument());
        ActionEvent evt = new ActionEvent(e.getDocument(), ActionEvent.ACTION_FIRST, command);
        actionPerformed(evt);
    }
    
    private String getActionCommand(javax.swing.text.Document document) {
        if(decimalSeparatorText.getDocument() == document) {
            return DECIMAL_ACTION;
        } else if (groupingSeparatorText.getDocument() == document){
            return GROUPING_ACTION;
        } else if (nanText.getDocument() == document){
            return NAN_ACTION;
        } else {
            throw new IllegalArgumentException("Unknown document!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(BROWSE_LOCALE_ACTION.equals(command)) {
            selectLocale();
        } else if(DATE_ACTION.equals(command)) {
            setDateFormat();
        } else if(NUMBER_ACTION.equals(command)) {
            setNumberFormat();
        } else if(DECIMAL_ACTION.equals(command)) {
            setNumberFormat();
        } else if(GROUPING_ACTION.equals(command)) {
            setNumberFormat();
        } else if(NAN_ACTION.equals(command)) {
            setNaN();
        } else if(RESET_LOCALE_ACTION.equals(command)) {
            resetLocale(getSelectedLocale());
        } else if(RESET_ACTION.equals(command)) {
            Locale l = Locale.getDefault();
            setSelectedLocale(l);
            resetLocale(l);
        }
    }
    
    private void selectLocale() {
        Locale l = LocaleDialog.selectLocale();
        if(l == null)
            return;
        setSelectedLocale(l);
        validateInput();
    }
    
    private void setSelectedLocale(Locale locale) {
        this.locale = locale;
        String language = locale==null? null : locale.getDisplayLanguage(Locale.ENGLISH);
        localeText.setText(language);
    }
    
    private void setDateFormat() {
        DateFormat format = dateFormatCombo.getFormat();
        if(format != null)
            dateExampleLabel.setText(format.format(now));
        validateInput();
    }
    
    private void validateInput() {
        isValid = validator.validatePanel();
        resetLanguageButton.setEnabled(getSelectedLocale() != null);
        fireChange();
    }
    
    void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(msg != null);
    }

    Locale getSelectedLocale() {
        return locale;
    }
    
    DateFormat getDateFormat() {
        return dateFormatCombo.getFormat();
    }
    
    DecimalFormat getDecimalFormat() {
        return numberFormatCombo.getFormat();
    }
    
    char getDecimalSeparator() {
        return decimalSeparatorText.getChar();
    }

    char getGroupingSeparator() {
        return groupingSeparatorText.getChar();
    }
    
    String getNaN() {
        return nanText.getText();
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    private void setNumberFormat() {
        if(validator.isNumberValid()) {
            decimalSymbols.setDecimalSeparator(getDecimalSeparator());
            decimalSymbols.setGroupingSeparator(getGroupingSeparator());
            rerenderNumberExample();
        }
        validateInput();
    }
    
    private void rerenderNumberExample() {
        DecimalFormat format = getDecimalFormat();
        format.setDecimalFormatSymbols(decimalSymbols);
        String positive = format.format(positiveNumber);
        String negative = format.format(negativeNumber);
        String number = ""+positiveNumber;
        String msg = String.format("%s | %s | %s", number, positive, negative);
        numberExampleLabel.setText(msg);
    }
    
    private void setNaN() {
        if(validator.isNaNValid())
            decimalSymbols.setNaN(getNaN());
        validateInput();
    }
    
    private void resetLocale(Locale locale) {
        resetDateLocale(locale);
        resetDecimalFormat(locale);
        validateInput();
    }
    
    private void resetDateLocale(Locale l) {
        String pattern = LocaleSettings.getDefaultDateFormatString(l);
        dateFormatCombo.setSelectedItem(pattern);
    }
    
    private void resetDecimalFormat(Locale l) {
        numberFormatCombo.setSelectedItem(LocaleSettings.getDefaultDecimalFormatString(l));
        decimalSymbols = LocaleSettings.getDefaultDecimalSymbols(l);
        resetDecimalSymbols();
    }
    
    private void resetDecimalSymbols() {
        decimalSeparatorText.setChar(decimalSymbols.getDecimalSeparator());
        groupingSeparatorText.setChar(decimalSymbols.getGroupingSeparator());
        nanText.setText(decimalSymbols.getNaN());
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    void load() {
        setSelectedLocale(LocaleSettings.getLocale());
        decimalSymbols = LocaleSettings.getDecimalSymbols();
        decimalSeparatorText.setText(""+decimalSymbols.getDecimalSeparator());
        groupingSeparatorText.setText(""+decimalSymbols.getGroupingSeparator());
        nanText.setText(decimalSymbols.getNaN());
        
        dateFormatCombo.setSelectedItem(LocaleSettings.getDateFormatString());
        numberFormatCombo.setSelectedItem(LocaleSettings.getDecimalFormatString());
        validateInput();
    }
    
    void store() {
        LocaleSettings.setLocale(getSelectedLocale());
        LocaleSettings.setDateFormat(dateFormatCombo.getFormatValue());
        LocaleSettings.setDecimalFormatString(numberFormatCombo.getFormatValue());
        LocaleSettings.setDecimalSeparator(decimalSymbols.getDecimalSeparator());
        LocaleSettings.setGroupingSeparator(decimalSymbols.getGroupingSeparator());
        LocaleSettings.setNaN(decimalSymbols.getNaN());
    }

    boolean isValidSettings() {
        return isValid;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dateFormatLabel = new javax.swing.JLabel();
        numberFormatLabel = new javax.swing.JLabel();
        decimalSeparatorLabel = new javax.swing.JLabel();
        groupingSeparatorLabel = new javax.swing.JLabel();
        dateFormatCombo = new org.jreserve.localesettings.util.DateFormatCombo();
        numberFormatCombo = new org.jreserve.localesettings.util.NumberFormatCombo();
        dateExampleLabel = new javax.swing.JLabel();
        numberExampleLabel = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        msgLabel = new javax.swing.JLabel();
        decimalSeparatorText = new org.jreserve.resources.textfieldfilters.CharacterTextField();
        groupingSeparatorText = new org.jreserve.resources.textfieldfilters.CharacterTextField();
        nanLabel = new javax.swing.JLabel();
        nanText = new javax.swing.JTextField();
        localeLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        resetLanguageButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        localeText = new javax.swing.JTextField();
        browseLocaleButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        dateFormatLabel.setText(Bundle.LBL_LocaleSettingPanel_DateFormat());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dateFormatLabel, gridBagConstraints);

        numberFormatLabel.setText(Bundle.LBL_LocaleSettingPanel_NumberFormat());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(numberFormatLabel, gridBagConstraints);

        decimalSeparatorLabel.setText(Bundle.LBL_LocaleSettingPanel_DecimalSeparatort());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(decimalSeparatorLabel, gridBagConstraints);

        groupingSeparatorLabel.setText(Bundle.LBL_LocaleSettingPanel_ThousandSeparator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(groupingSeparatorLabel, gridBagConstraints);

        dateFormatCombo.setActionCommand(DATE_ACTION);
        dateFormatCombo.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dateFormatCombo, gridBagConstraints);

        numberFormatCombo.setActionCommand(NUMBER_ACTION);
        numberFormatCombo.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(numberFormatCombo, gridBagConstraints);

        dateExampleLabel.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(dateExampleLabel, gridBagConstraints);

        numberExampleLabel.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(numberExampleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);

        msgLabel.setIcon(ImageUtilities.loadImageIcon(ERROR_ICON, false));
        msgLabel.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(msgLabel, gridBagConstraints);

        decimalSeparatorText.setText(null);
        decimalSeparatorText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(decimalSeparatorText, gridBagConstraints);

        groupingSeparatorText.setText(null);
        groupingSeparatorText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(groupingSeparatorText, gridBagConstraints);

        nanLabel.setText(Bundle.LBL_LocaleSettingPanel_NaN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(nanLabel, gridBagConstraints);

        nanText.setText(null);
        nanText.setActionCommand(NAN_ACTION);
        nanText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(nanText, gridBagConstraints);

        localeLabel.setText(Bundle.LBL_LocaleSettingPanel_Locale());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(localeLabel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        resetLanguageButton.setText(Bundle.CTL_LocaleSettingPanel_ResetLocale());
        resetLanguageButton.setActionCommand(RESET_LOCALE_ACTION);
        resetLanguageButton.addActionListener(this);
        buttonPanel.add(resetLanguageButton);

        resetButton.setText(Bundle.CTL_LocaleSettingPanel_Reset());
        resetButton.setActionCommand(RESET_ACTION);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(buttonPanel, gridBagConstraints);

        localeText.setEditable(false);
        localeText.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(localeText, gridBagConstraints);

        browseLocaleButton.setText(Bundle.CTL_LocaleSettingPanel_BrowseLocale());
        browseLocaleButton.setActionCommand(BROWSE_LOCALE_ACTION);
        browseLocaleButton.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(browseLocaleButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseLocaleButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel dateExampleLabel;
    private org.jreserve.localesettings.util.DateFormatCombo dateFormatCombo;
    private javax.swing.JLabel dateFormatLabel;
    private javax.swing.JLabel decimalSeparatorLabel;
    private org.jreserve.resources.textfieldfilters.CharacterTextField decimalSeparatorText;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel groupingSeparatorLabel;
    private org.jreserve.resources.textfieldfilters.CharacterTextField groupingSeparatorText;
    private javax.swing.JLabel localeLabel;
    private javax.swing.JTextField localeText;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JLabel nanLabel;
    private javax.swing.JTextField nanText;
    private javax.swing.JLabel numberExampleLabel;
    private org.jreserve.localesettings.util.NumberFormatCombo numberFormatCombo;
    private javax.swing.JLabel numberFormatLabel;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton resetLanguageButton;
    // End of variables declaration//GEN-END:variables
}
