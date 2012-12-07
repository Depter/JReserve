package org.jreserve.triangle.widget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.resources.textfieldfilters.DateSpinner;
import org.jreserve.resources.textfieldfilters.IntegerFilter;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.GeometrySettingPanel.StartDate=Start date:",
    "LBL.GeometrySettingPanel.IsSymmetric=Symmetric:",
    "LBL.GeometrySettingPanel.Periods=Periods",
    "LBL.GeometrySettingPanel.Months=Month per period",
    "LBL.GeometrySettingPanel.Accident=Accident:",
    "LBL.GeometrySettingPanel.Development=Development:",
    "MSG.GeometrySettingPanel.Accident.Invalid.From=Field 'Start date' is invalid!",
    "MSG.GeometrySettingPanel.Accident.Invalid.Periods=Field 'Periods' in group 'Accident' is invalid!",
    "MSG.GeometrySettingPanel.Accident.Invalid.Months=Field 'Month per step' in group 'Accident' is invalid!",
    "MSG.GeometrySettingPanel.Development.Invalid.Periods=Field 'Periods' in group 'Development' is invalid!",
    "MSG.GeometrySettingPanel.Development.Invalid.Months=Field 'Month per step' in group 'Development' is invalid!"
})
public class GeometrySettingPanel extends JPanel {

    public final static String PROPERTY_TRIANGLE_GEOMETRY = "TRIANGLE GEOMETRY";
    public final static String PROPERTY_ERROR = "TRIANGLE GEOMETRY ERROR";

    private final static int DEFAULT_PERIODS = 1;
    private final static int DEFAULT_STEPS = 12;
    
    private final boolean isTriangle;
    private final ChangeListener componentListener = new ComponentListener();
    private final DateSpinner dateSpinner = new DateSpinner();
    private final JCheckBox symmetricCheck = new JCheckBox();
    private final JSpinner accidentPeriodsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_PERIODS, 1, Integer.MAX_VALUE, 1));
    private final JSpinner accidentMonthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_STEPS, 1, Integer.MAX_VALUE, 1));
    private final JSpinner developmentPeriodsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_PERIODS, 1, Integer.MAX_VALUE, 1));
    private final JSpinner developmentMonthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_STEPS, 1, Integer.MAX_VALUE, 1));
    
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private TriangleGeometry geometry;
    private boolean isValid = true;
    
    public GeometrySettingPanel(boolean isTriangle) {
        this.isTriangle = isTriangle;
        initComponents();
        createGeometry();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        initStartDateComponents();
        initTitleComponents();
        initAccidentComponents();
        if(isTriangle) {
            initSymmetricComponents();
            initDevelopmentComponents();
        }
    }
    
    private void initStartDateComponents() {
        GridBagConstraints gc = createGbConstrainc(0);
        gc.insets = new Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_StartDate()), gc);
        
        dateSpinner.setPreferredSize(new Dimension(40, 18));
        dateSpinner.addChangeListener(componentListener);
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 0, 0);
        add(dateSpinner, gc);
        
        gc.gridx=2;
        gc.weightx=1d;
        add(Box.createHorizontalGlue(), gc);
    }
    
    private GridBagConstraints createGbConstrainc(int row) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=row;
        gc.weightx=0d; gc.weighty=0d;
        gc.fill=GridBagConstraints.BASELINE_LEADING;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        return gc;
    }
    
    private void initSymmetricComponents() {
        GridBagConstraints gc = createGbConstrainc(1);
        gc.insets = new Insets(5, 0, 0, 5);
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_IsSymmetric()), gc);
        
        symmetricCheck.setSelected(true);
        symmetricCheck.addChangeListener(componentListener);
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(5, 0, 0, 0);
        add(symmetricCheck, gc);
        
        gc.gridx=2;
        gc.weightx=1d;
        add(Box.createHorizontalGlue(), gc);
    }
    
    private void initTitleComponents() {
        GridBagConstraints gc = createGbConstrainc(isTriangle? 2 : 1);
        gc.insets = new Insets(5, 0, 0, 5);
        add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Periods()), gc);
        
        gc.gridx=2;
        gc.insets = new Insets(5, 0, 0, 0);
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Months()), gc);
    }
    
    private void initAccidentComponents() {
        GridBagConstraints gc = createGbConstrainc(isTriangle? 3 : 2);
        gc.insets = new Insets(5, 0, 0, 5);
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Accident()), gc);
        
        accidentPeriodsSpinner.setPreferredSize(new Dimension(40, 18));
        accidentPeriodsSpinner.setEditor(new IntegerEditor(accidentPeriodsSpinner));
        accidentPeriodsSpinner.addChangeListener(componentListener);
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(5, 0, 0, 5);
        add(accidentPeriodsSpinner, gc);
        
        accidentMonthSpinner.setPreferredSize(new Dimension(40, 18));
        accidentMonthSpinner.setEditor(new IntegerEditor(accidentMonthSpinner));
        accidentMonthSpinner.addChangeListener(componentListener);
        gc.gridx=2;
        gc.insets = new Insets(5, 0, 0, 0);
        add(accidentMonthSpinner, gc);
    }
    
    private void initDevelopmentComponents() {
        GridBagConstraints gc = createGbConstrainc(4);
        gc.insets = new Insets(5, 0, 0, 5);
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Development()), gc);
        
        developmentPeriodsSpinner.setPreferredSize(new Dimension(40, 18));
        developmentPeriodsSpinner.setEnabled(false);
        developmentPeriodsSpinner.setEditor(new IntegerEditor(developmentPeriodsSpinner));
        developmentPeriodsSpinner.addChangeListener(componentListener);
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(5, 0, 0, 5);
        add(developmentPeriodsSpinner, gc);
        
        developmentMonthSpinner.setPreferredSize(new Dimension(40, 18));
        developmentMonthSpinner.setEnabled(false);
        developmentMonthSpinner.setEditor(new IntegerEditor(developmentMonthSpinner));
        developmentMonthSpinner.addChangeListener(componentListener);
        gc.gridx=2;
        gc.insets = new Insets(5, 0, 0, 0);
        add(developmentMonthSpinner, gc);
    }
    
    private void createGeometry() {
        int aps = getAccidentPeriods();
        int amp = getAccidentMonths();
        int dps = getDevelopmentPeriods();
        int dmp = getDevelopmentMonths(aps, amp);
        geometry = new TriangleGeometry(dateSpinner.getDate(), aps, amp, dps, dmp);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        if(isValid)
            return geometry.copy();
        return null;
    }
    
    private int getAccidentPeriods() {
        return (Integer) accidentPeriodsSpinner.getValue();
    }
    
    private int getAccidentMonths() {
        return (Integer) accidentMonthSpinner.getValue();
    }
    
    private int getDevelopmentPeriods() {
        if(isTriangle)
           return (Integer) developmentPeriodsSpinner.getValue();
        return 1;
    }
    
    private int getDevelopmentMonths() {
        int aps = getAccidentPeriods();
        int amp = getAccidentMonths();
        return getDevelopmentMonths(aps, amp);
    }
    
    private int getDevelopmentMonths(int aps, int amp) {
        if(isTriangle)
            return (Integer) developmentMonthSpinner.getValue();
        return aps * amp;
    }
    
    private void setSymmetric(boolean isSymmetric) {
        if(!isTriangle) return;
        developmentPeriodsSpinner.setEnabled(!isSymmetric);
        developmentMonthSpinner.setEnabled(!isSymmetric);
        if(isSymmetric) {
            developmentPeriodsSpinner.setValue(accidentPeriodsSpinner.getValue());
            developmentMonthSpinner.setValue(accidentMonthSpinner.getValue());
        }
    }
    
    private void updateGeoemtry() {
        checkInput();
        if(isValid) {
            createGeometry();
            fireChangeEvent();
        }
    }
    
    private void checkInput() {
        isValid = checkStartDate() &&
                checkAccidentPeriods() && checkDevelopmentPeriods() &&
                checkAccidentMonths() && checkDevelopmentMonths();
        if(isValid)
            putClientProperty(PROPERTY_ERROR, null);
    }
    
    private boolean checkStartDate() {
        if(dateSpinner.getDate() == null) {
            String msg = Bundle.MSG_GeometrySettingPanel_Accident_Invalid_From();
            putClientProperty(PROPERTY_ERROR, msg);
            return false;
        }
        return true;
    }
    
    private boolean checkAccidentPeriods() {
        if(getAccidentPeriods() < 1) {
            String msg = Bundle.MSG_GeometrySettingPanel_Accident_Invalid_Periods();
            putClientProperty(PROPERTY_ERROR, msg);
            return false;
        }
        return true;
    }
    
    private boolean checkAccidentMonths() {
        if(getAccidentMonths() < 1) {
            String msg = Bundle.MSG_GeometrySettingPanel_Accident_Invalid_Months();
            putClientProperty(PROPERTY_ERROR, msg);
            return false;
        }
        return true;
    }
    
    private boolean checkDevelopmentPeriods() {
        if(getDevelopmentPeriods() < 1) {
            String msg = Bundle.MSG_GeometrySettingPanel_Development_Invalid_Periods();
            putClientProperty(PROPERTY_ERROR, msg);
            return false;
        }
        return true;
    }
    
    private boolean checkDevelopmentMonths() {
        if(getDevelopmentMonths() < 1) {
            String msg = Bundle.MSG_GeometrySettingPanel_Development_Invalid_Periods();
            putClientProperty(PROPERTY_ERROR, msg);
            return false;
        }
        return true;
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    private class ComponentListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(symmetricCheck == e.getSource())
                setSymmetric(symmetricCheck.isSelected());
            updateGeoemtry();
        }
    }
    
    private static class IntegerEditor extends JPanel implements DocumentListener, ChangeListener {
        
        private JSpinner spinner;
        private JTextField editor = IntegerFilter.getTextField();
        private boolean fireEditorEvent = true;
        private boolean fireSpinnerEvent = true;
        
        IntegerEditor(JSpinner spinner) {
            super(new BorderLayout());
            super.add(editor, BorderLayout.CENTER);
            this.spinner = spinner;
            initListeners();
            readModelValue();
        }
        
        private void initListeners() {
            editor.getDocument().addDocumentListener(this);
            spinner.addChangeListener(this);
        }
        
        private void readModelValue() {
            fireEditorEvent = false;
            Object value = spinner.getModel().getValue();
            editor.setText(""+value);
            fireEditorEvent = true;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            setModelValue();
        }
        
        private void setModelValue() {
            if(!fireEditorEvent)
                return;
            fireSpinnerEvent = false;
            int value = getFieldValue();
            spinner.getModel().setValue(value);
            fireSpinnerEvent = true;
        }
        
        private int getFieldValue() {
            String str = editor.getText();
            if(str == null || str.trim().length() == 0)
                return 0;
            return Integer.parseInt(str);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setModelValue();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if(!fireSpinnerEvent)
                return;
            try {
                readModelValue();
            } catch (RuntimeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
    }    
}
