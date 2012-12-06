package org.jreserve.database.oracle.create;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DummyDatabase {
    
    private String host;
    private int port;
    private String sid;

    DummyDatabase(String host, int port, String sid) {
        this.host = host;
        this.port = port;
        this.sid = sid;
    }

    String getHost() {
        return host;
    }

    int getPort() {
        return port;
    }

    String getSid() {
        return sid;
    }
}
