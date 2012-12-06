package org.jreserve.resources.textfieldfilters;

import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 *
 * @author Peter Decsi
 */
public class DateSpinner extends JSpinner {

    private final static String FORMAT = "yyyy-MM";
    private DateEditor editor;
            
    public DateSpinner() {
        super(getDateModel());
        initEditor();
    }
    
    private void initEditor() {
        editor = new JSpinner.DateEditor(this, FORMAT);
        setEditor(editor);
        editor.getTextField().setHorizontalAlignment(javax.swing.JTextField.LEADING);
    }

    public Date getDate() {
        return (Date) super.getValue();
    }
    
    public String getText() {
        return editor.getTextField().getText();
    }
    
    private static SpinnerDateModel getDateModel() {
        Date value = getBeginDate();
        return new SpinnerDateModel(value, null, null, Calendar.MONTH);
    }
    
    private static Date getBeginDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
