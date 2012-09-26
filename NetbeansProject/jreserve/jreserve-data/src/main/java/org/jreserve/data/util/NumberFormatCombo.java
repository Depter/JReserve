package org.jreserve.data.util;

import java.text.DecimalFormat;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NumberFormatCombo extends AbstractFormatCombo<DecimalFormat> {

    private final static String FORMATS[] = {
        "0.00",
        "#,###0.00",
        "#,###0.00;(#,###0.00)"
    };
    
    public NumberFormatCombo() {
        super(FORMATS);
    }
    
    @Override
    protected DecimalFormat createFormat(String format) {
        try {
            return new DecimalFormat(format);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
