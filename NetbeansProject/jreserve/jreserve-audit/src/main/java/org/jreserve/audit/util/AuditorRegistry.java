package org.jreserve.audit.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jreserve.audit.Auditor;
import static org.jreserve.audit.util.AuditorRegistrationProcessor.ENTITY_DIRECTORY;
import static org.jreserve.audit.util.AuditorRegistrationProcessor.PRIORITY;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class AuditorRegistry extends RegistrationRegistry<Auditor, Auditor> {
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPriority(f1);
            int p2 = getPriority(f2);
            return p1-p2;
        }
        
        private int getPriority(FileObject file) {
            Integer value = (Integer) file.getAttribute(PRIORITY);
            return value==null? Integer.MAX_VALUE : value;
        }
    };
    
    private static AuditorRegistry INSTANCE = new AuditorRegistry();
    
    public synchronized static List<Auditor> getAuditors(Object value) {
        return INSTANCE.getValues(value);
    }
    
    private List<Auditor> getValues(Object value) {
        List<Auditor> result = new ArrayList<Auditor>();
        for(Auditor auditor : getValues())
            if(auditor.isInterested(value))
                result.add(auditor);
        return result;
    }

    @Override
    protected String getDirectory() {
        return ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return FILE_COMPARATOR;
    }

    @Override
    protected Auditor getValue(Auditor instance, FileObject file) {
        return instance;
    }
}
