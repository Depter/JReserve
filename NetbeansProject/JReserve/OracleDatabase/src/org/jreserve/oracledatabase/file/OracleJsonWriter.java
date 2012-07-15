package org.jreserve.oracledatabase.file;

import java.util.List;
import org.jreserve.json.AbstractJsonWriter;
import org.jreserve.oracledatabase.OracleDatabase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OracleJsonWriter extends AbstractJsonWriter<List<OracleDatabase>> {
    
    public OracleJsonWriter(FileObject file) {
        super(FileUtil.toFile(file));
    }
    
    @Override
    protected JSONObject createJson(List<OracleDatabase> databases) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put(OracleJsonReader.DATABASES, createJsonDbArray(databases));
        return obj;
    }
    
    private JSONArray createJsonDbArray(List<OracleDatabase> databases) {
        JSONArray array = new JSONArray();
        for(OracleDatabase db : databases)
            array.add(createJsonDb(db));
        return array;
    } 
    
    private JSONObject createJsonDb(OracleDatabase db) {
        JSONObject dbObj = new JSONObject();
        dbObj.put(OracleJsonReader.SERVER, db.getServer());
        dbObj.put(OracleJsonReader.PORT, db.getPort());
        dbObj.put(OracleJsonReader.SID, db.getName());
        dbObj.put(OracleJsonReader.OPENED, db.isOpened());
        dbObj.put(OracleJsonReader.SELECTED, db.isSelected());
        return dbObj;
    }
}
