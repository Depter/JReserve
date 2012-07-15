package org.jreserve.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractJsonReader<E> {
    
    private final static ContainerFactory CONTAINER_FACTORY = new ContainerFactory() {
        @Override
        public Map createObjectContainer() {
            return new HashMap(1);
        }

        @Override
        public List creatArrayContainer() {
            return new ArrayList();
        }
    };
    private JSONParser parser = new JSONParser();
        
    private File file;
    private BufferedReader reader;
    
    public AbstractJsonReader(File file) {
        this.file = file;
    }
    
    public E loadDatabases() throws IOException {
        if(file != null)
            return loadFile();
        return null;
    }
    
    private E loadFile() throws IOException {
        try {
            openReader();
            return parseJson();
        } finally {
            closeReader();
        }
    }
    
    private void openReader() throws IOException {
        reader = new BufferedReader(new FileReader(file));
    }
    
    private void closeReader() {
        if(reader == null)
            return;
        try {reader.close();}
        catch (IOException ex) {}
    }
    
    private E parseJson() throws IOException {
        try {
            Map json = (Map) parser.parse(reader, CONTAINER_FACTORY);
            return processJson(json);
        } catch (Exception ex) {
            throw new IOException("Unable to parse JSON!", ex);
        }
    }
    
    protected abstract E processJson(Map json);
}
