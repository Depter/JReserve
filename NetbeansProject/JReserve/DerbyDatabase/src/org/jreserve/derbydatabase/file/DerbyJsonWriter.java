package org.jreserve.derbydatabase.file;

import java.util.List;
import org.jreserve.derbydatabase.DerbyDatabase;
import org.jreserve.json.AbstractJsonWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyJsonWriter extends AbstractJsonWriter<List<DerbyDatabase>> {
    
    final static String DATABASES = "databases";
    final static String NAME = "name";
    final static String PATH = "path";
    final static String SELECTED = "selected";
    
    public DerbyJsonWriter(FileObject file) {
        super(FileUtil.toFile(file));
    }

    @Override
    protected JSONObject createJson(List<DerbyDatabase> databases) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put(DATABASES, createJsonDbArray(databases));
        return obj;
    }
    
    private JSONArray createJsonDbArray(List<DerbyDatabase> databases) {
        JSONArray array = new JSONArray();
        for(DerbyDatabase db : databases)
            array.add(createJsonDb(db));
        return array;
    } 
    
    private JSONObject createJsonDb(DerbyDatabase db) {
        JSONObject dbObj = new JSONObject();
        dbObj.put(NAME, db.getName());
        dbObj.put(PATH, db.getPath());
        dbObj.put(SELECTED, db.isSelected());
        return dbObj;
    }
}
