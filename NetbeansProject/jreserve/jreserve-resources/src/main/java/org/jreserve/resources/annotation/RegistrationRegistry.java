package org.jreserve.resources.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class RegistrationRegistry<T, V> {

    private final static Logger logger = Logger.getLogger(RegistrationRegistry.class.getName());

    
    private List<T> values;
    
    
    protected List<T> getValues() {
        if(values == null)
            initialize();
        return values;
    }
    
    private void initialize() {
        logger.log(Level.FINE, "Loading values from \"{0}\"...", getDirectory());
        values = new ArrayList<T>();
        for(FileObject file : getFiles()) {
            V instance = loadInstance(file);
            values.add(getValue(instance, file));
        }
    }
    
    protected abstract String getDirectory();
    
    private FileObject[] getFiles() {
        FileObject home = FileUtil.getConfigFile(getDirectory());
        FileObject[] children = home.getChildren();
        Arrays.sort(children, getFileComparator());
        return children;
    }
    
    protected abstract Comparator<FileObject> getFileComparator();
    
    protected V loadInstance(FileObject file) {
        try {
            DataObject data = DataObject.find(file);
            InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
            V value = (V) cookie.instanceCreate();
            logger.log(Level.FINE, "Loaded instance: {0}", value.getClass().getName());
            return value;
        } catch (Exception ex) {
            logger.log(Level.WARNING, String.format("Unable to load instance from file: %s", file.getPath()), ex);
            return null;
        } 
    }
    
    protected abstract T getValue(V instance, FileObject file);
}