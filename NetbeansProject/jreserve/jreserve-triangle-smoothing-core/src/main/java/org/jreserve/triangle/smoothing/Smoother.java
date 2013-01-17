package org.jreserve.triangle.smoothing;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.value.TriangleCoordiante;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Smoother {

    public Smoothing createSmoothing(ModifiableTriangularData triangle, List<TriangleCoordiante> cells, int visibleDigits);
    
    public static @interface Registration {
        
        public String displayName();
        
        public int position() default Integer.MAX_VALUE;
    }
}
