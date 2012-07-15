package org.jreserve.branding.img;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Images {
    
    public final static ImageIcon BLANK = new ImageIcon();
    public final static ImageIcon ERROR = getIcon("error.png");
    public final static ImageIcon WARNING = getIcon("warning.png");
    public final static ImageIcon QUESTION = getIcon("question.png");
    public final static ImageIcon INFO = getIcon("info.png");
    public final static ImageIcon DATABASE = getIcon("database.png");
    public final static ImageIcon DB_SELECTED = getIcon("db_selected.png");

    private final static Logger logger = Logger.getLogger(Images.class.getName());
    
    private static ImageIcon getIcon(String name) {
        URL url = Images.class.getResource(name);
        if(url == null) {
            logger.log(Level.WARNING, "Unable to load image '%s'.", name);
            return new ImageIcon();
        }
        return new ImageIcon(url);
    }
    
    private Images() {}
}
