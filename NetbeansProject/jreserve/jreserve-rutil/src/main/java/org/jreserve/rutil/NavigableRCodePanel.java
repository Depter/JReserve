package org.jreserve.rutil;

import java.awt.Color;
import java.awt.Dimension;
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
import org.jreserve.rutil.visual.RCodeTextPane;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NavigableRCodePanel extends NavigablePanel {

    private final static Image IMG = ImageUtilities.loadImage("resources/r.png", false);
    private final static Color BACKGROUND = new Color(186, 184, 135);
    private final static Color FOREGROUND = Color.BLACK;
    
    private RCode rCode = new RCode(true);
    private RCodeTextPane textPane;
    
    public NavigableRCodePanel(String displayName) {
        super(displayName, IMG);
        super.setOpened(false);

        textPane = new RCodeTextPane(rCode);
        textPane.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(textPane);
        
        Dimension size = textPane.getPreferredSize();
        size.height = 300;
        scroll.setMaximumSize(size);
        scroll.setPreferredSize(size);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        super.setContent(scroll);
        super.setBackground(BACKGROUND);
        super.setForeground(FOREGROUND);
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
