package org.jreserve.factor.core.linkratio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.factor.core.LinkRatioMethod;
import static org.jreserve.factor.core.linkratio.LinkRatioMethodRegistrationProcessor.*;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioMethodRegistry extends RegistrationRegistry<LinkRatioMethodImpl, LinkRatioMethod> {
    
    private final static Logger logger = Logger.getLogger(LinkRatioMethodRegistry.class.getName());
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int dif = comparePosition(f1, f2);
            if(dif != 0)
                return dif;
            return compareDisplayName(f1, f2);
        }
        
        private int comparePosition(FileObject f1, FileObject f2) {
            int p1 = getPosition(f1);
            int p2 = getPosition(f2);
            return p1-p2;
        }
        
        private int getPosition(FileObject file) {
            Integer value = (Integer) file.getAttribute(POSITION);
            return value==null? Integer.MAX_VALUE : value;
        }
        
        private int compareDisplayName(FileObject f1, FileObject f2) {
            String n1 = getDisplayName(f1);
            String n2 = getDisplayName(f2);
            return n1.compareToIgnoreCase(n2);
        }
        
        private String getDisplayName(FileObject file) {
            String value = (String) file.getAttribute(DISPLAY_NAME);
            return value==null? "" : value;
        }
    };

    private static LinkRatioMethodRegistry INSTANCE;
    private static List<LinkRatioMethodImpl> methods;
    
    public static List<LinkRatioMethodImpl> getLinkRatioMethods() {
        if(methods == null)
            loadMethods();
        return Collections.unmodifiableList(methods);
    }
    
    private static void loadMethods() {
        if(INSTANCE == null)
            INSTANCE = new LinkRatioMethodRegistry();
        //INSTANCE.positions.clear();
        methods = INSTANCE.getValues();
    }
    
    @Override
    protected String getDirectory() {
        return DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return FILE_COMPARATOR;
    }

    @Override
    protected LinkRatioMethodImpl getValue(LinkRatioMethod instance, FileObject file) {
        try {
            return loadValue(instance, file);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, "Unable to load LinkRatioMethod from file \"{0}\"!", file);
            throw ex;
        }
    }
    
    private LinkRatioMethodImpl loadValue(LinkRatioMethod instance, FileObject file) {
        int position = (Integer) file.getAttribute(POSITION);
        String name = (String) file.getAttribute(DISPLAY_NAME);
        return new LinkRatioMethodImpl(instance, position, name);
    }
}
