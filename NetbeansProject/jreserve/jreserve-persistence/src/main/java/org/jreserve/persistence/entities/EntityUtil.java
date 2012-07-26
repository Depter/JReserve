package org.jreserve.persistence.entities;

import java.util.HashSet;
import java.util.Set;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntityUtil {
    private final static Logger logger = Logging.getLogger(EntityUtil.class.getName());
    
    private final static String FOLDER = "jreserve/entities";
    
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public EntityUtil() {
    }
    
    public Set<Class<?>> getEntities() {
        logger.debug("Loading database entities...");
        loadFolder();
        return classes;
    }
    
    private void loadFolder() {
        FileObject folder = FileUtil.getConfigFile(FOLDER);
        if(folder == null) {
            logger.warn("Folder '%s' is not found in the System Filesystem!", FOLDER);
            return;
        }
        loadEntities(folder);
    }
    
    private void loadEntities(FileObject folder) {
        for(FileObject child : folder.getChildren())
            if(child.getMIMEType().equalsIgnoreCase(EntityLoader.MIME_TYPE))
                loadEntity(child);
    }
    
    private void loadEntity(FileObject file) {
        try {
            DataObject obj = DataObject.find(file);
            addClass(obj.getLookup().lookup(Class.class));
        } catch (DataObjectNotFoundException ex) {
            logger.error(ex, "Entity DataObject for file '%s' is not found!", 
                    file.getPath());
        }
    }
    
    private void addClass(Class<?> clazz) {
        if(clazz != null)
            classes.add(clazz);
    }
}