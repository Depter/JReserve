package org.jreserve.triangle.mvc.view;

import java.awt.Component;
import java.text.DecimalFormatSymbols;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.resources.textfieldfilters.DoubleFilter;

/**
 *
 * @author Peter Decsi
 */
public class DoubleTextEditor extends LayerTextEditor {

    private JTextField editor;
    private DoubleRenderer valueRenderer = new DoubleRenderer();
    
    public DoubleTextEditor(Callback callback) {
        super(callback);
        initEditor();
    }
    
    private void initEditor() {
        DecimalFormatSymbols symbols = LocaleSettings.getDecimalSymbols();
        editor = new JTextField();
        editor.setDocument(new DoubleFilter(symbols.getDecimalSeparator()));
    }
    
    @Override
    public Object getEditorComponentValue() {
        String str = editor.getText();
        if(str==null || str.trim().length()==0)
            return null;
        return valueRenderer.parse(str);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        originalValue = value;
        editedRow = row;
        editedColumn = column;
        editor.setText(getStringValue(value));
        return editor;
    }
    
    private String getStringValue(Object value) {
        Double d = TriangleDoubleUtil.getTableValue(value);
        return valueRenderer.toString(d);
    }
}
