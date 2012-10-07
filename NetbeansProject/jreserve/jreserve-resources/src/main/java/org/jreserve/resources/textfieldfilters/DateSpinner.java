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
        Calendar c = getCalendar();
        Date now = c.getTime();
        c.add(Calendar.YEAR, -100);
        Date start = c.getTime();
        c.add(Calendar.YEAR, 200);
        Date end = c.getTime();
        return new SpinnerDateModel(now, start, end, Calendar.MONTH);
    }
    
    private static Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }
}
