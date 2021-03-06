package org.jreserve.triangle.visual.createdialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
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
    "LBL.GeometrySettingPanel.Periods=Periods:",
    "LBL.GeometrySettingPanel.Months=Month per period:",
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
    private final JSpinner accidentMonthsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_STEPS, 1, Integer.MAX_VALUE, 1));
    private final JSpinner developmentPeriodsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_PERIODS, 1, Integer.MAX_VALUE, 1));
    private final JSpinner developmentMonthsSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_STEPS, 1, Integer.MAX_VALUE, 1));
    
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private TriangleGeometry geometry;
    private boolean isValid = true;
    
    private boolean myAction = false;
    
    public GeometrySettingPanel(boolean isTriangle) {
        this.isTriangle = isTriangle;
        if(isTriangle)
            initTriangleComponents();
        else
            initVectorComponents();
        createGeometry();
    }

    private void initTriangleComponents() {
        setLayout(new GridLayout(5, 3, 5, 5));

        //ROW 1
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_StartDate()));
        add(initDateSpinner());
        add(Box.createGlue());

        //ROW 2
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_IsSymmetric()));
        add(initSymmetricCheck());
        add(Box.createGlue());
        
        //ROW 3
        add(Box.createGlue());
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Accident(), SwingConstants.CENTER));
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Development(), SwingConstants.CENTER));

        //ROW 4
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Periods()));
        add(initIntegerSpinner(accidentPeriodsSpinner, true));
        add(initIntegerSpinner(developmentPeriodsSpinner, false));
        
        //ROW 5
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Months()));
        add(initIntegerSpinner(accidentMonthsSpinner, true));
        add(initIntegerSpinner(developmentMonthsSpinner, false));
    }
    
    private JSpinner initDateSpinner() {
        dateSpinner.setMinimumSize(new java.awt.Dimension(60, 17));
        dateSpinner.setPreferredSize(new java.awt.Dimension(60, 17));
        dateSpinner.getModel().addChangeListener(componentListener);
        return dateSpinner;
    }

    private JCheckBox initSymmetricCheck() {
        symmetricCheck.setMinimumSize(new java.awt.Dimension(21, 17));
        symmetricCheck.setPreferredSize(new java.awt.Dimension(21, 17));
        symmetricCheck.addChangeListener(componentListener);
        symmetricCheck.setSelected(true);
        return symmetricCheck;
    }
    
    private JSpinner initIntegerSpinner(JSpinner spinner, boolean enabled) {
        spinner.setMinimumSize(new java.awt.Dimension(60, 17));
        spinner.setPreferredSize(new java.awt.Dimension(60, 17));
        spinner.setEditor(new IntegerEditor(spinner));
        spinner.getModel().addChangeListener(componentListener);
        spinner.setEnabled(enabled);
        return spinner;
    }
    
    private void initVectorComponents() {
        setLayout(new GridLayout(4, 2, 5, 5));

        //ROW 1
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_StartDate()));
        add(initDateSpinner());
        
        //ROW 2
        add(Box.createGlue());
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Accident(), SwingConstants.CENTER));

        //ROW 3
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Periods()));
        add(initIntegerSpinner(accidentPeriodsSpinner, true));
        
        //ROW 4
        add(new JLabel(Bundle.LBL_GeometrySettingPanel_Months()));
        add(initIntegerSpinner(accidentMonthsSpinner, true));
    }
    
    private void createGeometry() {
        int aps = getAccidentPeriods();
        int amp = getAccidentMonths();
        int dps = getDevelopmentPeriods();
        int dmp = getDevelopmentMonths(aps, amp);
        geometry = new TriangleGeometry(dateSpinner.getDate(), aps, amp, dps, dmp);
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        setStartDate(geometry.getStartDate());
        accidentPeriodsSpinner.setValue(geometry.getAccidentPeriods());
        accidentMonthsSpinner.setValue(geometry.getAccidentMonths());
        if(isTriangle && !symmetricCheck.isSelected()) {
            developmentPeriodsSpinner.setValue(geometry.getDevelopmentPeriods());
            developmentMonthsSpinner.setValue(geometry.getDevelopmentMonths());
        }
    }
    
    public void setStartDate(Date date) {
        if(date == null)
            date = new Date();
        dateSpinner.setValue(date);
    }
    
    public boolean isInputValid() {
        return isValid;
    }
    
    public String getErrorMsg() {
        return (String) getClientProperty(PROPERTY_ERROR);
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
        return (Integer) accidentMonthsSpinner.getValue();
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
            return (Integer) developmentMonthsSpinner.getValue();
        return aps * amp;
    }
    
    private void setSymmetric(boolean isSymmetric) {
        if(!isTriangle) return;
        
        developmentPeriodsSpinner.setEnabled(!isSymmetric);
        developmentMonthsSpinner.setEnabled(!isSymmetric);
        if(isSymmetric) {
            myAction = true;
            developmentPeriodsSpinner.setValue(accidentPeriodsSpinner.getValue());
            developmentMonthsSpinner.setValue(accidentMonthsSpinner.getValue());
            myAction = false;
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
            if(myAction) return;
            
            if(symmetricCheck == e.getSource())
                setSymmetric(symmetricCheck.isSelected());
            else if(isTriangle && symmetricCheck.isSelected())
                synchronizeDevelopments();
            updateGeoemtry();
        }
        
        private void synchronizeDevelopments() {
            myAction = true;
            developmentPeriodsSpinner.setValue(accidentPeriodsSpinner.getValue());
            developmentMonthsSpinner.setValue(accidentMonthsSpinner.getValue());
            myAction = false;
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
