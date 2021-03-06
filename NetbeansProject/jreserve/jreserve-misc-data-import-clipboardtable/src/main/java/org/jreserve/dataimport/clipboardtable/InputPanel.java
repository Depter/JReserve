package org.jreserve.dataimport.clipboardtable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.data.DataImport;
import org.jreserve.data.ProjectDataType;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.resources.textfieldfilters.CharacterDocument;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.InputPanel.ClaimType=Claim type:",
    "LBL.InputPanel.DataType=Data type:",
    "LBL.InputPanel.DateFormat=Date format:",
    "LBL.InputPanel.Cummulated=Cummulated:",
    "LBL.InputPanel.ImportMethod=Import method:",
    "LBL.InputPanel.DecimalSeparator=Decimal separator:",
    "LBL.InputPanel.ThousandSeparator=Thousand separator:"
})
public class InputPanel extends javax.swing.JPanel implements ActionListener, DocumentListener {
    
    public final static String DATA_TYPE_SELECT_ACTION = "DATA_TYPE_SELECTED";
    public final static String CUMMULATED_ACTION = "CUMMULATED";
    public final static String DATE_FORMAT_ACTION = "DATE_FORMAT";
    public final static String NUMBER_FORMAT_ACTION = "NUMBER_FORMAT";
    public final static String IMPORT_METHOD_ACTION = "IMPORT_METHOD";
    
    private final static Comparator<ProjectElement<ProjectDataType>> COMPARATOR = new Comparator<ProjectElement<ProjectDataType>>() {
        public int compare(ProjectElement<ProjectDataType> o1, ProjectElement<ProjectDataType> o2) {
            ProjectDataType d1 = o1.getValue();
            ProjectDataType d2 = o2.getValue();
            return d1.compareTo(d2);
        }
    };
    
    private final Date now = new Date();
    private final double number = 1234.56;
    
    private DateFormat dateFormat;
    private DecimalFormatSymbols decimalSymbols;
    private DecimalFormat numberFormat;

    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public InputPanel() {
        initComponents();
        initDateFormat();
        initNumberformat();
    }
    
    private void initDateFormat() {
        dateFormat = dateFormatCombo.getFormat();
        dateExampleLabel.setText(dateFormat.format(now));
    }
    
    private void initNumberformat() {
        numberFormat = LocaleSettings.getDecimalFormat();
        decimalSymbols = numberFormat.getDecimalFormatSymbols();
        formatNumberExample();
    }
    
    private void formatNumberExample() {
        String plus = numberFormat.format(number);
        String min = numberFormat.format(-number);
        String space = numberFormat.isGroupingUsed()? "" : " ";
        numberExampleLabel.setText(space+plus+" | "+space+min);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(DATA_TYPE_SELECT_ACTION.equals(command))
            checkTriangleDataType();
        else if(DATE_FORMAT_ACTION.equals(command))
            setDateFormatExample();
        else if(NUMBER_FORMAT_ACTION.equals(command))
            setNumberFormatExample();
        fireActionParformed(command);
    }

    private void checkTriangleDataType() {
        ProjectDataType type = getDataType();
        boolean enabled = type==null || type.isTriangle();
        cummulatedCheck.setEnabled(enabled);
    }
    
    private void setDateFormatExample() {
        if(dateFormatCombo.isValidFormat()) {
            dateFormat = dateFormatCombo.getFormat();
            dateExampleLabel.setText(dateFormat.format(now));
        }
    }
    
    private void setNumberFormatExample() {
        String str = decimalSeparatorText.getText();
        if(str.length() != 1)
            return;
        decimalSymbols.setDecimalSeparator(str.charAt(0));
        setGrouping();
        numberFormat.setDecimalFormatSymbols(decimalSymbols);
        formatNumberExample();
    }
    
    private void setGrouping() {
        String str = thousandSeparatorText.getText();
        if(str.length() == 0) {
            numberFormat.setGroupingUsed(false);
        } else {
            numberFormat.setGroupingUsed(true);
            decimalSymbols.setGroupingSeparator(str.charAt(0));
        }
    }
    
    private void fireActionParformed(String command) {
        ActionEvent e = createActionEvent(command);
        for(ActionListener listener : new ArrayList<ActionListener>(listeners))
            listener.actionPerformed(e);
    }
    
    private ActionEvent createActionEvent(String command) {
        return new ActionEvent(this, ActionEvent.ACTION_FIRST, command);
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
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_FIRST, NUMBER_FORMAT_ACTION);
        this.actionPerformed(evt);
    }
    
    public void setClaimType(ProjectElement<ClaimType> element) {
        String name = element==null? null : element.getValue().getName();
        this.claimTypeNameText.setText(name);
        dataTypeCombo.setElements(getDataTypeElements(element));
    }
    
    private List<ProjectElement> getDataTypeElements(ProjectElement<ClaimType> element) {
        List<ProjectElement<ProjectDataType>> elements = element.getChildren(ProjectDataType.class);
        Collections.sort(elements, COMPARATOR);
        return new ArrayList<ProjectElement>(elements);
    }
    
    public ProjectDataType getDataType() {
        return dataTypeCombo.getSelectedItem(ProjectDataType.class);
    }
    
    public DateFormat getDateFormat() {
        return dateFormat;
    }
    
    public String getDateFormatString() {
        return dateFormatCombo.getFormatValue();
    }
    
    public boolean isCummulated() {
        return cummulatedCheck.isEnabled() && cummulatedCheck.isSelected();
    }
    
    public DataImport.ImportType getImportType() {
        return (DataImport.ImportType) importMethodCombo.getSelectedItem();
    }
    
    public void addActionListener(ActionListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }
    
    public DecimalFormat getDecimalFormat() {
        return numberFormat;
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

        projectNameLabel = new javax.swing.JLabel();
        dataTypeLabel = new javax.swing.JLabel();
        cummulatedLabel = new javax.swing.JLabel();
        dateFormatLabel = new javax.swing.JLabel();
        decimalSeparatorLabel = new javax.swing.JLabel();
        thousandSeparatorLabel = new javax.swing.JLabel();
        claimTypeNameText = new javax.swing.JLabel();
        cummulatedCheck = new javax.swing.JCheckBox();
        dateFormatCombo = new org.jreserve.localesettings.util.DateFormatCombo();
        decimalSeparatorText = new javax.swing.JTextField();
        thousandSeparatorText = new javax.swing.JTextField();
        dateExampleLabel = new javax.swing.JLabel();
        numberExampleLabel = new javax.swing.JLabel();
        bottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        importMethodLabel = new javax.swing.JLabel();
        importMethodCombo = new javax.swing.JComboBox(DataImport.ImportType.values());
        dataTypeCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();

        setLayout(new java.awt.GridBagLayout());

        projectNameLabel.setText(Bundle.LBL_InputPanel_ClaimType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(projectNameLabel, gridBagConstraints);

        dataTypeLabel.setText(Bundle.LBL_InputPanel_DataType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dataTypeLabel, gridBagConstraints);

        cummulatedLabel.setText(Bundle.LBL_InputPanel_Cummulated());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(cummulatedLabel, gridBagConstraints);

        dateFormatLabel.setText(Bundle.LBL_InputPanel_DateFormat());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dateFormatLabel, gridBagConstraints);

        decimalSeparatorLabel.setText(Bundle.LBL_InputPanel_DecimalSeparator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(decimalSeparatorLabel, gridBagConstraints);

        thousandSeparatorLabel.setText(Bundle.LBL_InputPanel_ThousandSeparator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(thousandSeparatorLabel, gridBagConstraints);

        claimTypeNameText.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(claimTypeNameText, gridBagConstraints);

        cummulatedCheck.setText(null);
        cummulatedCheck.setActionCommand(CUMMULATED_ACTION);
        cummulatedCheck.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(cummulatedCheck, gridBagConstraints);

        dateFormatCombo.setSelectedItem(LocaleSettings.getDateFormatString());
        dateFormatCombo.addActionListener(this);
        dateFormatCombo.setActionCommand(DATE_FORMAT_ACTION);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dateFormatCombo, gridBagConstraints);

        decimalSeparatorText.setDocument(new CharacterDocument());
        decimalSeparatorText.setText(Character.toString(LocaleSettings.getDecimalSeparator()));
        decimalSeparatorText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(decimalSeparatorText, gridBagConstraints);

        thousandSeparatorText.setDocument(new CharacterDocument());
        thousandSeparatorText.setText(Character.toString(LocaleSettings.getGroupingSeparator()));
        thousandSeparatorText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(thousandSeparatorText, gridBagConstraints);

        dateExampleLabel.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(dateExampleLabel, gridBagConstraints);

        numberExampleLabel.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(numberExampleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(bottomFiller, gridBagConstraints);

        importMethodLabel.setText(Bundle.LBL_InputPanel_ImportMethod());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(importMethodLabel, gridBagConstraints);

        importMethodCombo.setActionCommand(IMPORT_METHOD_ACTION);
        importMethodCombo.setRenderer(new ImportTypeListRenderer());
        importMethodCombo.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(importMethodCombo, gridBagConstraints);

        dataTypeCombo.addLookupListener(ProjectDataType.class, new DataTypeListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dataTypeCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler bottomFiller;
    private javax.swing.JLabel claimTypeNameText;
    private javax.swing.JCheckBox cummulatedCheck;
    private javax.swing.JLabel cummulatedLabel;
    private org.jreserve.project.system.visual.ProjectElementComboBox dataTypeCombo;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JLabel dateExampleLabel;
    private org.jreserve.localesettings.util.DateFormatCombo dateFormatCombo;
    private javax.swing.JLabel dateFormatLabel;
    private javax.swing.JLabel decimalSeparatorLabel;
    private javax.swing.JTextField decimalSeparatorText;
    private javax.swing.JComboBox importMethodCombo;
    private javax.swing.JLabel importMethodLabel;
    private javax.swing.JLabel numberExampleLabel;
    private javax.swing.JLabel projectNameLabel;
    private javax.swing.JLabel thousandSeparatorLabel;
    private javax.swing.JTextField thousandSeparatorText;
    // End of variables declaration//GEN-END:variables

    private class DataTypeListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ActionEvent evt = new ActionEvent(dataTypeCombo, ActionEvent.ACTION_FIRST, DATA_TYPE_SELECT_ACTION);
            actionPerformed(evt);
        }
    
    }
}

