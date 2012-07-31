package org.jreserve.persistence.entities;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntityDataObject extends MultiDataObject {

    final static String CLASS_ATTRIBUTE = "class";
    final static String GENERATED_ID = "generatedId";
    private final Lookup lookup;
    
    public EntityDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException {
        super(fo, loader);
        lookup = Lookups.singleton(getEntityClass(fo));
    }
    
    private Class<?> getEntityClass(FileObject file) {
        Object attr = file.getAttribute(CLASS_ATTRIBUTE);
        if(attr instanceof String)
            return getEntityClass((String) attr);
        else
            throw new IllegalArgumentException(
                String.format("Attribute '%s' on file '%s' is not a string!", 
                CLASS_ATTRIBUTE, file.getPath()));
    }
    
    private Class<?> getEntityClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                String.format("Unable to load class: %s", className));
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public String toString() {
        String path = getPrimaryFile().getPath();
        return String.format("EntityDataObject [%s]", path);
    }
}
