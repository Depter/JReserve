package org.jreserve.logging.view;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.jreserve.logging.Logger;

/**
 *
 * @author Peter Decsi
 */
class GuiAppender {

    private static GuiAppender INSTANCE = null;
    
    private static void createInstance() {
        INSTANCE = new GuiAppender(LogviewTopComponent.getDocument());
    }
    
    private final StyledDocument document;
    private final Map<Logger.Level, Style> styles = new EnumMap<Logger.Level, Style>(Logger.Level.class);
    
    private GuiAppender(StyledDocument document) {
        this.document = document;
        createStyles();
    }

    private void createStyles() {
        Style base = document.addStyle("levelBase", null);
        
        Style fatal = document.addStyle("fatal", base);
        StyleConstants.setForeground(fatal, Color.red);
        styles.put(Logger.Level.FATAL, fatal);
        
        Style error = document.addStyle("error", base);
        StyleConstants.setForeground(error, Color.red);
        styles.put(Logger.Level.ERROR, error);
        
        Style warn = document.addStyle("warn", base);
        StyleConstants.setForeground(warn, Color.orange);
        styles.put(Logger.Level.WARN, warn);
        
        Style info = document.addStyle("info", base);
        StyleConstants.setForeground(info, Color.blue);
        styles.put(Logger.Level.INFO, info);
        
        Style debug = document.addStyle("debug", base);
        StyleConstants.setForeground(debug, Color.gray);
        StyleConstants.setItalic(debug, true);
        styles.put(Logger.Level.DEBUG, debug);
        
        Style trace = document.addStyle("trace", base);
        StyleConstants.setForeground(trace, Color.gray);
        StyleConstants.setItalic(trace, true);
        styles.put(Logger.Level.TRACE, trace);
    }
    
    public static void append(final Logger.Level level, final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(INSTANCE == null)
                    createInstance();
                INSTANCE.appendToDocument(level, msg);
            }
        });
    }
    
    private void appendToDocument(Logger.Level level, String msg) {
        Style style = styles.get(level);
        try {
            document.insertString(document.getLength(), msg, style);
        } catch (BadLocationException ex) {}
    }
}
