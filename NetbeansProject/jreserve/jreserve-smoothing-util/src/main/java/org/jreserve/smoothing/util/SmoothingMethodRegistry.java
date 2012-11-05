package org.jreserve.smoothing.util;

import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.resources.annotation.RegistrationRegistry;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.SmoothingFactory;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class SmoothingMethodRegistry extends RegistrationRegistry<SmoothingFactory, SmoothingMethod> {
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int id1 = (Integer) o1.getAttribute(SmoothingMethodRegistrationProcessor.ID);
            int id2 = (Integer) o2.getAttribute(SmoothingMethodRegistrationProcessor.ID);
            return id1 - id2;
        }
    };
    
    @Override
    public List<SmoothingFactory> getValues() {
        return super.getValues();
    }
    
    @Override
    protected String getDirectory() {
        return SmoothingMethodRegistrationProcessor.ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return COMPARATOR;
    }

    @Override
    protected SmoothingFactory getValue(SmoothingMethod instance, FileObject file) {
        int id = (Integer) file.getAttribute(SmoothingMethodRegistrationProcessor.ID);
        String displayName = (String) file.getAttribute(SmoothingMethodRegistrationProcessor.DISPLAY_NAME);
        return new FactoryDelegate(id, displayName, instance);
    }

    private static class FactoryDelegate implements SmoothingFactory {

        private final int id;
        private final String name;
        private final SmoothingMethod method;
        
        private FactoryDelegate(int id, String name, SmoothingMethod method) {
            this.id = id;
            this.name = name;
            this.method = method;
        }
        
        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getDisplayName() {
            return name;
        }

        @Override
        public Smoothing createSmoothing(PersistentObject owner, TriangleWidget widget, TriangleCell[] cells) {
            return method.createSmoothing(owner, widget, cells);
        }

        @Override
        public List<Smoothing> getSmoothings(PersistentObject owner) {
            return method.getSmoothings(owner);
        }
    }
}
