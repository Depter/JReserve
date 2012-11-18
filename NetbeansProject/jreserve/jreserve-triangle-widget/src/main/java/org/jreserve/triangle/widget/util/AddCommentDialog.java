package org.jreserve.triangle.widget.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.triangle.data.Comment;
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
    "LBL.AddCommentDialog.Title=Add Comment",
    "LBL.AddCommentDialog.Ok=Save",
    "LBL.AddCommentDialog.Cancel=Cancel"
})
public class AddCommentDialog extends JPanel implements ActionListener, DocumentListener {

    public static Comment getComment() {
        AddCommentDialog content = new AddCommentDialog();
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        showDialog(dd, content);
        return content.comment;
    }
    
    private static DialogDescriptor createDescriptor(AddCommentDialog content) {
        return new DialogDescriptor(
            content,
            Bundle.LBL_AddCommentDialog_Title(),
            true,
            null, content.okButton,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private static void showDialog(DialogDescriptor dd, AddCommentDialog content) {
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }
    
    private java.awt.Dialog dialog;
    private JTextArea commentText;
    private JButton okButton;
    private JButton cancelButton;
    private Comment comment;
    
    private AddCommentDialog() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        commentText = new JTextArea();
        commentText.setColumns(CommentRenderer.MAX_ROW_WIDTH);
        commentText.setRows(10);
        commentText.getDocument().addDocumentListener(this);
        JScrollPane scroll = new JScrollPane(commentText);
        scroll.setBorder(new LineBorder(Color.BLACK, 1, true));
        add(scroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        okButton = new JButton(Bundle.LBL_AddCommentDialog_Ok());
        okButton.setEnabled(false);
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        
        cancelButton = new JButton(Bundle.LBL_AddCommentDialog_Cancel());
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == okButton)
            comment = new SimpleComment(getCommentText());
        dialog.dispose();
    }
    
    private String getCommentText() {
        String str = commentText.getText();
        if(str == null || str.trim().length()==0)
            return null;
        return str;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        commentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        commentChanged();
    }
    
    private void commentChanged() {
        boolean enabled = (getCommentText() != null);
        okButton.setEnabled(enabled);
    }
    
    private static class SimpleComment implements Comment {
        
        private final String userName = System.getProperty("user.name");
        private final Date date = new Date();
        private String comment;
        
        private SimpleComment(String comment) {
            this.comment = comment;
        }
        
        @Override
        public String getUserName() {
            return userName;
        }

        @Override
        public Date getCreationDate() {
            return date;
        }

        @Override
        public String getCommentText() {
            return comment;
        }
    }
}
