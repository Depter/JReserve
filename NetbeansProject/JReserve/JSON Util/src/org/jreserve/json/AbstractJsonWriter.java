package org.jreserve.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractJsonWriter<E> {

    private File file;
    private BufferedWriter writer;
    
    public AbstractJsonWriter(File file) {
        this.file = file;
    }
    
    public void writeDatabases(E object) throws Exception {
        try {
            openWriter();
            writeJsonString(object);
        } finally {
            closeWriter();
        }
    }
    
    private void openWriter() throws IOException {
        writer = new BufferedWriter(new FileWriter(file));
    }
    
    private void writeJsonString(E object) throws Exception {
        JSONObject obj = createJson(object);
//        JSONObject obj = new JSONObject();
//        obj.put(DATABASES, createJsonDbArray(databases));
        obj.writeJSONString(writer);
    }
    
    protected abstract JSONObject createJson(E object) throws Exception;
    
//    private JSONArray createJsonDbArray(List<DerbyDatabase> databases) {
//        JSONArray array = new JSONArray();
//        for(DerbyDatabase db : databases)
//            array.add(createJsonDb(db));
//        return array;
//    } 
//    
//    private JSONObject createJsonDb(DerbyDatabase db) {
//        JSONObject dbObj = new JSONObject();
//        dbObj.put(NAME, db.getName());
//        dbObj.put(PATH, db.getPath());
//        return dbObj;
//    }
    
    private void closeWriter() {
        if(writer == null)
            return;
        try {writer.close();}
        catch (IOException ex) {}
    }    
}
