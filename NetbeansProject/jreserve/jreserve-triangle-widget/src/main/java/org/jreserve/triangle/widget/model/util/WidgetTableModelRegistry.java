package org.jreserve.triangle.widget.model.util;

import java.awt.Image;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.jreserve.triangle.widget.model.WidgetTableModel;
import static org.jreserve.triangle.widget.model.util.WidgetTableModelRegistrationProcessor.*;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetTableModelRegistry extends RegistrationRegistry<WidgetTableModelImpl, WidgetTableModel> {
    
    private final static Logger logger = Logger.getLogger(WidgetTableModelRegistry.class.getName());
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPriority(f1);
            int p2 = getPriority(f2);
            return p1-p2;
        }
        
        private int getPriority(FileObject file) {
            Integer value = (Integer) file.getAttribute(POSITION);
            return value==null? Integer.MAX_VALUE : value;
        }
    };

    private static WidgetTableModelRegistry INSTANCE;
    private static List<WidgetTableModelImpl> models;
    
    public static List<WidgetTableModelImpl> getModels() {
        if(models == null)
            loadModels();
        return Collections.unmodifiableList(models);
    }
    
    private static void loadModels() {
        if(INSTANCE == null)
            INSTANCE = new WidgetTableModelRegistry();
        INSTANCE.positions.clear();
        models = INSTANCE.getValues();
    }
    
    private final Set<Integer> positions = new HashSet<Integer>();
    
    @Override
    protected String getDirectory() {
        return ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return FILE_COMPARATOR;
    }

    @Override
    protected WidgetTableModelImpl getValue(WidgetTableModel instance, FileObject file) {
        try {
            return loadValue(instance, file);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, "Unable to load WidgetTableModel from file \"{0}\"!", file);
            throw ex;
        }
    }
    
    private WidgetTableModelImpl loadValue(WidgetTableModel instance, FileObject file) {
        int position = getPosition(file);
        String name = (String) file.getAttribute(DISPLAY_NAME);
        Image icon = getIcon(file);
        return new WidgetTableModelImpl(icon, name, position, instance);
    }
    
    private int getPosition(FileObject file) {
        int position = (Integer) file.getAttribute(POSITION);
        if(!positions.add(position))
            logger.log(Level.WARNING, "Position {0} used more then once! {1}", new Object[]{position, file});
        return position;
    }
    
    private Image getIcon(FileObject file) {
        String name = (String) file.getAttribute(ICON);
        return ImageUtilities.loadImage(name, false);
    }
    
}