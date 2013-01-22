package org.jreserve.factor.core.linkratio.smoothing.util;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingMethod;
import static org.jreserve.factor.core.linkratio.smoothing.util.LinkRatioSmoothingMethodRegistrationProcessor.*;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSmoothingMethodRegistry extends RegistrationRegistry<LinkRatioSmoothingMethodImpl, LinkRatioSmoothingMethod> {
    
    private final static Logger logger = Logger.getLogger(LinkRatioSmoothingMethodRegistry.class.getName());
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPosition(f1);
            int p2 = getPosition(f2);
            return p1-p2;
        }
        
        private int getPosition(FileObject file) {
            Integer value = (Integer) file.getAttribute(POSITION);
            return value==null? Integer.MAX_VALUE : value;
        }
    };

    private static LinkRatioSmoothingMethodRegistry INSTANCE;
    private static List<LinkRatioSmoothingMethodImpl> models;
    
    public static List<LinkRatioSmoothingMethodImpl> getModels() {
        if(models == null)
            loadModels();
        return Collections.unmodifiableList(models);
    }
    
    private static void loadModels() {
        if(INSTANCE == null)
            INSTANCE = new LinkRatioSmoothingMethodRegistry();
        INSTANCE.ids.clear();
        models = INSTANCE.getValues();
    }
    
    private final Set<String> ids = new HashSet<String>();
    
    @Override
    protected String getDirectory() {
        return ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return FILE_COMPARATOR;
    }

    @Override
    protected LinkRatioSmoothingMethodImpl getValue(LinkRatioSmoothingMethod instance, FileObject file) {
        try {
            return loadValue(instance, file);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, "Unable to load LinkRatioSmoothingMethod from file \"{0}\"!", file);
            throw ex;
        }
    }
    
    private LinkRatioSmoothingMethodImpl loadValue(LinkRatioSmoothingMethod instance, FileObject file) {
        String id = getId(file);
        int position = (Integer) file.getAttribute(POSITION);
        String displayName = (String) file.getAttribute(DISPLAY_NAME);
        return new LinkRatioSmoothingMethodImpl(position, id, displayName, instance);
    }
    
    private String getId(FileObject file) {
        String id = (String) file.getAttribute(ID);
        if(!ids.add(id))
            logger.log(Level.WARNING, "Id \"{0}\" used more then once! {1}", new Object[]{id, file});
        return id;
    }
}
