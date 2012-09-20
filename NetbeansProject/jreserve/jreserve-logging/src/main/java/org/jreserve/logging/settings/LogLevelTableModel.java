package org.jreserve.logging.settings;

import java.util.*;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import org.jreserve.logging.LoggerProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.LogLevelTableModel.logger=Logger",
    "LBL.LogLevelTableModel.level=Level",
    "# {0} - level, typed by the user",
    "MSG.LogLevelTableModel.wronglevel=Value \"{0}\" is not a valid level.\nUse a number or one of the following values 'OFF', 'SEVERE','WARNING', 'INFO', 'CONFIG', 'FINE', 'FINER', 'FINEST' or 'ALL'"
})
class LogLevelTableModel extends DefaultTableModel {

    private final static String LEVEL_END = ".level";
    private final static int COLUMN_COUNT = 2;
    final static int COLUMN_LOGGER = 0;
    final static int COLUMN_LEVEL = 1;
    
    private Map<String, Level> levels = new HashMap<String, Level>();
    private List<String> loggers = new ArrayList<String>();
    
    LogLevelTableModel() {
        Properties props = LoggerProperties.getProperties();
        initProperties(props);
    }
    
    private void initProperties(Properties props) {
        for(String key : props.stringPropertyNames())
            if(isLevelProperty(key))
                addLevelProperty(key, props.getProperty(key));
        Collections.sort(loggers);
    }
    
    private boolean isLevelProperty(String property) {
        return property.endsWith(LEVEL_END) && property.length() > LEVEL_END.length();
    }
    
    private void addLevelProperty(String property, String value) {
        String logger = getLoggerName(property);
        loggers.add(logger);
        levels.put(logger, Level.parse(value));
    }
    
    private String getLoggerName(String property) {
        int index = property.lastIndexOf('.');
        return property.substring(0, index);
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case COLUMN_LOGGER:
                return Bundle.LBL_LogLevelTableModel_logger();
            case COLUMN_LEVEL:
                return Bundle.LBL_LogLevelTableModel_level();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public int getRowCount() {
        if(loggers == null)
            return 0;
        return loggers.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        String logger = loggers.get(row);
        switch(column) {
            case COLUMN_LOGGER:
                return logger;
            case COLUMN_LEVEL:
                return levels.get(logger).getName();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return COLUMN_LEVEL == column;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Level level = getLevel((String) value);
        if(level == null)
            return;
        levels.put(loggers.get(row), level);
        super.fireTableCellUpdated(row, column);
    }

    private Level getLevel(String strLevel) {
        try {
            return Level.parse(strLevel==null? "" : strLevel);
        } catch (IllegalArgumentException ex) {
            showError(Bundle.MSG_LogLevelTableModel_wronglevel(strLevel));
            return null;
        }
    }
    
    private void showError(String msg) {
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case COLUMN_LOGGER:
                return String.class;
            case COLUMN_LEVEL:
                return String.class;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }
    
    void addValue(String logger, Level level) {
        if(!loggers.contains(logger)) {
            loggers.add(logger);
            Collections.sort(loggers);
        }
        levels.put(logger, level);
        int index = loggers.indexOf(logger);
        super.fireTableRowsInserted(index, index);
    }
    
    void deleteRow(int row) {
        String logger = loggers.remove(row);
        levels.remove(logger);
        super.fireTableRowsDeleted(row, row);
    }
    
    void storeLevels(Properties props) {
        for(String logger : loggers)
            props.put(logger, levels.get(logger).getName());
    }
}
