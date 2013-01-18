package org.jreserve.triangle.visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.entities.TriangleComment;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DeleteTriangleCommentDialog.Title=Delete comments",
    "LBL.DeleteTriangleCommentDialog.Delete=Delete",
    "LBL.DeleteTriangleCommentDialog.Date=Date",
    "LBL.DeleteTriangleCommentDialog.UserName=User",
    "LBL.DeleteTriangleCommentDialog.CommentText=Comment",
    "CTL.DeleteTriangleCommentDialog.Delete=Delete",
    "CTL.DeleteTriangleCommentDialog.Cancel=Cancel"
})
public class DeleteTriangleCommentDialog extends JPanel {

    public static List<TriangleComment> getDeletedComments(List<TriangleComment> comments) {
        DeleteTriangleCommentDialog content = new DeleteTriangleCommentDialog(comments);
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        showDialog(dd, content);
        return content.deleted;
    }
    
    private static DialogDescriptor createDescriptor(DeleteTriangleCommentDialog content) {
        return new DialogDescriptor(
            content,
            Bundle.LBL_DeleteTriangleCommentDialog_Title(),
            true,
            null, content.deleteButton,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private static void showDialog(DialogDescriptor dd, DeleteTriangleCommentDialog content) {
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }

    private Dialog dialog;
    
    private List<TriangleComment> comments = new ArrayList<TriangleComment>();
    private List<TriangleComment> deleted = new ArrayList<TriangleComment>();
    
    private JTable table;
    private CommentModel model;
    
    private JButton deleteButton;
    private JButton cancelButton;
    
    private DeleteTriangleCommentDialog(List<TriangleComment> comments) {
        this.comments.addAll(comments);
        initComponents();
    }
    
    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new BorderLayout(15, 15));
        
        model = new CommentModel();
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(Color.BLACK, 1, true));
        scroll.setPreferredSize(new Dimension(300, 100));
        add(scroll, BorderLayout.CENTER);
        
        ActionListener listener = new ButtonListener();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        deleteButton = new JButton(Bundle.CTL_DeleteTriangleCommentDialog_Delete());
        deleteButton.addActionListener(listener);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        
        cancelButton = new JButton(Bundle.CTL_DeleteTriangleCommentDialog_Cancel());
        cancelButton.addActionListener(listener);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(deleteButton == e.getSource())
                addDeletedComments();
            dialog.dispose();
        }
        
        private void addDeletedComments() {
            for(int i=0, size=comments.size(); i<size; i++)
                if(model.selected[i])
                    deleted.add(comments.get(i));
        }
    }
    
    private class CommentModel implements TableModel {
        
        private boolean[] selected;
        private DateRenderer dateRenderer = new DateRenderer();
        private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
        
        private CommentModel() {
            selected = new boolean[comments.size()];
            java.util.Arrays.fill(selected, true);
        }
        
        @Override
        public int getRowCount() {
            return comments.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0: return Bundle.LBL_DeleteTriangleCommentDialog_Delete();
                case 1: return Bundle.LBL_DeleteTriangleCommentDialog_Date();
                case 2: return Bundle.LBL_DeleteTriangleCommentDialog_UserName();
                case 3: return Bundle.LBL_DeleteTriangleCommentDialog_CommentText();
                default: throw new IllegalArgumentException("Unknown column index: "+column);
            }
        }

        @Override
        public Class<?> getColumnClass(int column) {
            switch(column) {
                case 0: return Boolean.class;
                case 1: return String.class;
                case 2: return String.class;
                case 3: return String.class;
                default: throw new IllegalArgumentException("Unknown column index: "+column);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column==0;
        }

        @Override
        public Object getValueAt(int row, int column) {
            if(column==0)
                return selected[row];
            TriangleComment comment = comments.get(row);
            switch(column) {
                case 1: return dateRenderer.toString(comment.getCreationDate());
                case 2: return comment.getUserName();
                case 3: return comment.getCommentText();
                default: throw new IllegalArgumentException("Unknown column index: "+column);
            }
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            if(column != 0)
                return;
            boolean s = (Boolean) value;
            selected[row] = s;
            deleteButton.setEnabled(hasSelected());
        }
        
        private boolean hasSelected() {
            for(boolean b : selected)
                if(b)
                    return true;
            return false;
        }

        @Override
        public void addTableModelListener(TableModelListener listener) {
            if(!listeners.contains(listener))
                listeners.add(listener);
        }

        @Override
        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }    
    }
}
