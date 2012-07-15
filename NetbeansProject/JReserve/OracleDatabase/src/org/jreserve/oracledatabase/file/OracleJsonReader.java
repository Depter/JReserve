package org.jreserve.oracledatabase.file;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.json.AbstractJsonReader;
import org.jreserve.oracledatabase.OracleDatabase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OracleJsonReader extends AbstractJsonReader<Set<OracleDatabase>> {
    
    final static String DATABASES = "databases";
    final static String SID = "sid";
    final static String PORT = "port";
    final static String SERVER = "server";
    final static String OPENED = "opened";
    final static String SELECTED = "selected";
    
    private Set<OracleDatabase> databases = new TreeSet<OracleDatabase>();

    public OracleJsonReader(FileObject file) {
        super(FileUtil.toFile(file));
    }
    
    @Override
    protected Set<OracleDatabase> processJson(Map json) {
        for(Object o : (List) json.get(DATABASES))
            buildDatabase((Map) o);
        return databases;
    }
    
    private void buildDatabase(Map json) {
        String sid = (String) json.get(SID);
        int port = ((Long) json.get(PORT)).intValue();
        String server = (String) json.get(SERVER);
        boolean opened = (Boolean) json.get(OPENED);
        boolean selected = (Boolean) json.get(SELECTED);
        databases.add(new OracleDatabase(server, port, sid, opened, selected));
    }
}
