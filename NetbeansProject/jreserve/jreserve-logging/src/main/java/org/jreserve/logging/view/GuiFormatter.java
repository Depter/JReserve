package org.jreserve.logging.view;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import org.jreserve.logging.LoggingUtil;

/**
 *
 * @author Peter Decsi
 */
public class GuiFormatter extends Formatter {

    private final static String FORMAT = "[%-"+LoggingUtil.MAX_USER_NAME+"s]: %s: %s%n";
    
    @Override
    public String format(LogRecord record) {
        String level = LoggingUtil.getUserName(record.getLevel());
        String source = record.getSourceClassName();
        String msg = super.formatMessage(record);
        msg = String.format(msg, record.getParameters());
        return String.format(FORMAT, level, source, msg);
    }
}
