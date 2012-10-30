package org.jreserve.triangle.widget;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.resources.textfieldfilters.IntegerFilter;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IntegerSpinnerEditor extends JPanel implements DocumentListener, ChangeListener {

    private JSpinner spinner;
    private JTextField editor = IntegerFilter.getTextField();
    private boolean fireEditorEvent = true;
    private boolean fireSpinnerEvent = true;

    IntegerSpinnerEditor(JSpinner spinner) {
        super(new BorderLayout());
        super.add(editor, BorderLayout.CENTER);
        editor.getDocument().addDocumentListener(this);
        this.spinner = spinner;
        spinner.addChangeListener(this);
        readModelValue();
    }

    private void readModelValue() {
        fireEditorEvent = false;
        Object value = spinner.getModel().getValue();
        editor.setText("" + value);
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
