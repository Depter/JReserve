package org.jreserve.triangle.widget.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.triangle.widget.WidgetEditor;
import static org.jreserve.triangle.widget.util.WidgetEditorRegistrationProcessor.DIRECTORY;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetEditorLoader {

    private final static Logger logger = Logger.getLogger(WidgetEditorLoader.class.getName());
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {

        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPosition(f1);
            int p2 = getPosition(f2);
            return p1 - p2;
        }

        private int getPosition(FileObject f) {
            Integer position = (Integer) f.getAttribute("position");
            return position==null? Integer.MAX_VALUE : position;
        }
    };
    
    public static WidgetEditor loadEditor(String category) {
        logger.log(Level.FINE, "Loading WidgetEditor from category \"{0}\".", category);
        FileObject file = getFile(DIRECTORY+"/"+category);
        if(file == null) {
            logger.log(Level.FINER, "No instances under category \"{0}\".", category);
            return null;
        }
        return loadEditor(file);
    }
    
    private static FileObject getFile(String path) {
        FileObject home = FileUtil.getConfigFile(path);
        if(home == null)
            return null;
        FileObject[] children = home.getChildren();
        if(children.length == 0)
            return null;
        Arrays.sort(children, COMPARATOR);
        return children[0];
    }
    
    private static WidgetEditor loadEditor(FileObject f) {
        try {
            DataObject obj = DataObject.find(f);
            InstanceCookie ic = obj.getLookup().lookup(InstanceCookie.class);
            
            WidgetEditor editor = (WidgetEditor) ic.instanceCreate();
            logger.log(Level.FINE, "Loaded WidgetEditor from \"{0}\".", f.getPath());
            return editor;
        } catch (Exception ex) {
            String msg = "Unable to load WidgetEditor from \"%s\"";
            msg = String.format(msg, f.getPath());
            logger.log(Level.SEVERE, msg, ex);
            return null;
        }
    }
    
    private WidgetEditorLoader() {}
}
