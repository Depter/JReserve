package org.jreserve.audit.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import javax.swing.AbstractAction;
import org.jreserve.audit.AuditElement;

/**
 *
 * @author Peter Decsi
 */
public class CopyDataAction extends AbstractAction {

    private StringBuilder sb = new StringBuilder();
    private AuditTable table;
    
    public CopyDataAction(AuditTable table) {
        this.table = table;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        StringSelection text = new StringSelection(getText());
        sb.setLength(0);
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(text, null);
    }

    private String getText() {
        DateFormat df = table.getDateFormat();
        for (AuditElement element : table.getChanges()) {
            append(df, element);
        }
        return sb.toString();
    }

    private void append(DateFormat df, AuditElement element) {
        newLine();
        sb.append(df.format(element.getDate())).append("\t");
        sb.append(element.getUser()).append("\t");
        sb.append(element.getType()).append("\t");
        sb.append(element.getChange());
    }

    private void newLine() {
        if (sb.length() > 0) {
            sb.append("\n");
        }
    }
}
