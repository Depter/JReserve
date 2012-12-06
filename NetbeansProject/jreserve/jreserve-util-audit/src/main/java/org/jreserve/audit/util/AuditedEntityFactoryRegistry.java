package org.jreserve.audit.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jreserve.audit.AuditedEntityFactory;
import static org.jreserve.audit.util.AuditedEntityFactoryRegistrationProcessor.ENTITY_DIRECTORY;
import static org.jreserve.audit.util.AuditedEntityFactoryRegistrationProcessor.PRIORITY;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AuditedEntityFactoryRegistry extends RegistrationRegistry<AuditedEntityFactory, AuditedEntityFactory> {
    
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
    
    private static AuditedEntityFactoryRegistry INSTANCE = new AuditedEntityFactoryRegistry();
    
    public synchronized static List<AuditedEntityFactory> getFactories(Object value) {
        return INSTANCE.getValues(value);
    }
    
    private List<AuditedEntityFactory> getValues(Object value) {
        List<AuditedEntityFactory> result = new ArrayList<AuditedEntityFactory>();
        for(AuditedEntityFactory factory : getValues())
            if(factory.isInterested(value))
                result.add(factory);
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
    protected AuditedEntityFactory getValue(AuditedEntityFactory instance, FileObject file) {
        return instance;
    }
}
