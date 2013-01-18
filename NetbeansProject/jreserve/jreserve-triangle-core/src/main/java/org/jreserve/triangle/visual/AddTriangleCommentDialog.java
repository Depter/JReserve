package org.jreserve.triangle.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    "LBL.AddTriangleCommentDialog.Title=Add Comment",
    "LBL.AddTriangleCommentDialog.Ok=Save",
    "LBL.AddTriangleCommentDialog.Cancel=Cancel"
})
public class AddTriangleCommentDialog extends JPanel {

    public static String getComment() {
        AddTriangleCommentDialog content = new AddTriangleCommentDialog();
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        showDialog(dd, content);
        return content.comment;
    }
    
    private static DialogDescriptor createDescriptor(AddTriangleCommentDialog content) {
        return new DialogDescriptor(
            content,
            Bundle.LBL_AddTriangleCommentDialog_Title(),
            true,
            null, content.okButton,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private static void showDialog(DialogDescriptor dd, AddTriangleCommentDialog content) {
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }
    
    private java.awt.Dialog dialog;
    private JTextArea commentText;
    private JButton okButton;
    private JButton cancelButton;
    private String comment;
    
    private AddTriangleCommentDialog() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        commentText = new JTextArea();
        commentText.setColumns(TriangleCommentRenderer.MAX_ROW_WIDTH);
        commentText.setRows(10);
        commentText.getDocument().addDocumentListener(new CommentTextListener());
        JScrollPane scroll = new JScrollPane(commentText);
        scroll.setBorder(new LineBorder(Color.BLACK, 1, true));
        add(scroll, BorderLayout.CENTER);
        
        ActionListener buttonListener = new ButtonListener();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        okButton = new JButton(Bundle.LBL_AddTriangleCommentDialog_Ok());
        okButton.setEnabled(false);
        okButton.addActionListener(buttonListener);
        buttonPanel.add(okButton);
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        
        cancelButton = new JButton(Bundle.LBL_AddTriangleCommentDialog_Cancel());
        cancelButton.addActionListener(buttonListener);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private String getCommentText() {
        String str = commentText.getText();
        if(str == null || str.trim().length()==0)
            return null;
        return str;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(source == okButton)
                comment = getCommentText();
            dialog.dispose();
        }
    }

    private class CommentTextListener implements DocumentListener {
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
    }
}
