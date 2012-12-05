package org.jreserve.estimates.factors.factorestimator;

import java.util.Comparator;
import java.util.List;
import static org.jreserve.estimates.factors.factorestimator.util.FactorEstimatorRegistrationProcessor.*;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorEstimatorRegistry extends RegistrationRegistry<FactorEstimatorComponent, FactorEstimator>{
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPosition(f1);
            int p2 = getPosition(f2);
            return p1-p2;
        }
    };
    
    private static FactorEstimatorRegistry INSTANCE = new FactorEstimatorRegistry();
    
    public synchronized static List<FactorEstimatorComponent> getFactorEstimates() {
        return INSTANCE.getValues();
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
    protected FactorEstimatorComponent getValue(FactorEstimator instance, FileObject file) {
        int position = getPosition(file);
        String displayName = getDisplayName(file);
        return new FactorEstimatorComponent(instance, displayName, position);
    }
        
    private static int getPosition(FileObject file) {
        Integer value = (Integer) file.getAttribute(POSITION);
        return value==null? Integer.MAX_VALUE : value;
    }
    
    private static String getDisplayName(FileObject file) {
        String name = (String) file.getAttribute(DISPLAY_NAME);
        return name==null? "" : name;
    }
}
