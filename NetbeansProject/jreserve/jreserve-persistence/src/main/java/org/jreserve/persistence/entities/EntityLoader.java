package org.jreserve.persistence.entities;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntityLoader extends UniFileLoader {

    final static String MIME_TYPE = "jreserve/entity";
    
    public EntityLoader() {
        super(EntityDataObject.class.getName());
    }

    @Override
    protected FileObject findPrimaryFile(FileObject fo) {
        if(!fo.getMIMEType().equals(MIME_TYPE))
            return null;
        return hasClassAttribute(fo)? fo : null;
    }

    private boolean hasClassAttribute(FileObject fo) {
        return fo.getAttribute(EntityDataObject.CLASS_ATTRIBUTE) != null;
    }
    
    @Override
    protected MultiDataObject createMultiObject(FileObject fo) throws DataObjectExistsException, IOException {
        return new EntityDataObject(fo, this);
    }
}
