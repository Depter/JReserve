package org.jreserve.database.oracle;

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
public class OracleDatabaseLoader extends AbstractDatabaseLoader {
    
    public OracleDatabaseLoader() {
        super(OracleDatabaseProvider.DRIVER);
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new OracleDatabase(primaryFile, this);
    }
}
