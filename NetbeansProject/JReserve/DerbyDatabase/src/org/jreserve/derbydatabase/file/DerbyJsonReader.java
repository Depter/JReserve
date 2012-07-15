package org.jreserve.derbydatabase.file;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.derbydatabase.DerbyDatabase;
import org.jreserve.json.AbstractJsonReader;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyJsonReader extends AbstractJsonReader<Set<DerbyDatabase>> {
    
    private Set<DerbyDatabase> databases = new TreeSet<DerbyDatabase>();
    
    public DerbyJsonReader(FileObject file) {
        super(FileUtil.toFile(file));
    }

    @Override
    protected Set<DerbyDatabase> processJson(Map json) {
        for(Object o : (List) json.get(DerbyJsonWriter.DATABASES))
            buildDatabase((Map) o);
        return databases;
    }
    
    private void buildDatabase(Map json) {
        String name = (String) json.get(DerbyJsonWriter.NAME);
        String path = (String) json.get(DerbyJsonWriter.PATH);
        boolean selected = (Boolean) json.get(DerbyJsonWriter.SELECTED);
        databases.add(new DerbyDatabase(name, path, selected));
    }
}
