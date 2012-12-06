package org.jreserve.database.derby;

import java.io.IOException;
import org.jreserve.database.AbstractDatabaseLoader;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyDatabaseLoader extends AbstractDatabaseLoader {
    
    public DerbyDatabaseLoader() {
        super(DerbyDatabaseProvider.DRIVER);
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new DerbyDatabase(primaryFile, this);
    }
}
