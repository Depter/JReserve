package org.jreserve.triangle.widget.util;

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
import org.jreserve.triangle.entities.Comment;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DeleteCommentDialog.Title=Delete comments",
    "LBL.DeleteCommentDialog.Delete=Delete",
    "LBL.DeleteCommentDialog.Date=Date",
    "LBL.DeleteCommentDialog.UserName=User",
    "LBL.DeleteCommentDialog.CommentText=Comment",
    "CTL.DeleteCommentDialog.Delete=Delete",
    "CTL.DeleteCommentDialog.Cancel=Cancel"
})
public class DeleteCommentDialog extends JPanel implements ActionListener {

    public static List<Comment> getDeletedComments(List<Comment> comments) {
        DeleteCommentDialog content = new DeleteCommentDialog(comments);
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        showDialog(dd, content);
        return content.deleted;
    }
    
    private static DialogDescriptor createDescriptor(DeleteCommentDialog content) {
        return new DialogDescriptor(
            content,
            Bundle.LBL_DeleteCommentDialog_Title(),
            true,
            null, content.deleteButton,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private static void showDialog(DialogDescriptor dd, DeleteCommentDialog content) {
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }

    private Dialog dialog;
    
    private List<Comment> comments = new ArrayList<Comment>();
    private List<Comment> deleted = new ArrayList<Comment>();
    
    private JTable table;
    private CommentModel model;
    
    private JButton deleteButton;
    private JButton cancelButton;
    
    private DeleteCommentDialog(List<Comment> comments) {
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        deleteButton = new JButton(Bundle.CTL_DeleteCommentDialog_Delete());
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        
        cancelButton = new JButton(Bundle.CTL_DeleteCommentDialog_Cancel());
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(deleteButton == e.getSource()) {
            for(int i=0, size=comments.size(); i<size; i++)
                if(model.selected[i])
                    deleted.add(comments.get(i));
        }
        dialog.dispose();
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
                case 0: return Bundle.LBL_DeleteCommentDialog_Delete();
                case 1: return Bundle.LBL_DeleteCommentDialog_Date();
                case 2: return Bundle.LBL_DeleteCommentDialog_UserName();
                case 3: return Bundle.LBL_DeleteCommentDialog_CommentText();
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
            Comment comment = comments.get(row);
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
