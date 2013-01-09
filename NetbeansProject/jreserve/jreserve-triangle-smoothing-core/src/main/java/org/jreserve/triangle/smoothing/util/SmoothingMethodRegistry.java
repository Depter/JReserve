package org.jreserve.triangle.smoothing.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.jreserve.triangle.smoothing.Smoother;
import static org.jreserve.triangle.smoothing.util.SmootherRegistrationProcessor.*;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingMethodRegistry extends RegistrationRegistry<SmootherImpl, Smoother> {
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int id1 = (Integer) o1.getAttribute(POSITION);
            int id2 = (Integer) o2.getAttribute(POSITION);
            return id1 - id2;
        }
    };
    
    private static SmoothingMethodRegistry INSTANCE;
    
    public synchronized static List<SmootherImpl> getSmoothers() {
        if(INSTANCE == null)
            INSTANCE = new SmoothingMethodRegistry();
        return new ArrayList<SmootherImpl>(INSTANCE.getValues());
    }

    @Override
    protected String getDirectory() {
        return SmootherRegistrationProcessor.ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return COMPARATOR;
    }

    @Override
    protected SmootherImpl getValue(Smoother instance, FileObject file) {
        int id = (Integer) file.getAttribute(POSITION);
        String displayName = (String) file.getAttribute(DISPLAY_NAME);
        return new SmootherImpl(id, displayName, instance);
    }
}
