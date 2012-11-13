package org.jreserve.rutil;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.navigator.NavigablePanelCopyButton;
import org.jreserve.rutil.util.RCodeTextPane;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NavigableRCodePanel extends NavigablePanel {

    private final static Image IMG = ImageUtilities.loadImage("resources/r.png", false);
    
    private RCode rCode = new RCode();
    private RCodeTextPane textPane;
    
    public NavigableRCodePanel(String displayName) {
        super(displayName, IMG);
        textPane = new RCodeTextPane(rCode);
        
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        super.setContent(scroll);
        super.setBackground(Color.WHITE);
        super.setForeground(Color.BLACK);
        super.addUserTitleComponent(new NavigablePanelCopyButton(new CodeCopy()));
    }
    
    public RCode getRCode() {
        return rCode;
    }
    
    private class CodeCopy implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = textPane.getText();
            if(str == null || str.trim().length()==0)
                return;
            putToClipboard(str);
        }
        
        private void putToClipboard(String str) {
            StringSelection text = new StringSelection(str);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
    }
}
