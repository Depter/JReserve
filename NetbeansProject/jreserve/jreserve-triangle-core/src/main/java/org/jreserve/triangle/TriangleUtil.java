package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public class TriangleUtil {

    public static void cummulate(double[][] triangle) {
        for(double[] row : triangle)
            cummulate(row);
    }

    private static void cummulate(double[] row) {
        for(int i=1, size=row.length; i<size; i++) {
            double prev = row[i-1];
            double current = row[i];
            if(!Double.isNaN(prev) && !Double.isNaN(current))
                row[i] = prev+current;
        }
    }
    
    public static void deCummulate(double[][] triangle) {
        for(double[] row : triangle)
            deCummulate(row);
    }

    private static void deCummulate(double[] row) {
        for(int i=row.length-1; i>0; i--) {
            double prev = row[i-1];
            double current = row[i];
            if(!Double.isNaN(prev) && !Double.isNaN(current))
                row[i] = current - prev;
        }
    }
    
    public static List<ModifiedTriangularData> loadData(PersistentObject owner) {
        List<ModifiedTriangularData> result = new ArrayList<ModifiedTriangularData>();
        for(ModificationLoader loader : Lookup.getDefault().lookupAll(ModificationLoader.class))
            result.addAll(loader.loadModifications(owner));
        Collections.sort(result);
        return result;
    }
    
    public static void save(Session session, List<ModifiedTriangularData> original, List<ModifiedTriangularData> current) {
        deleteOld(session, original, current);
        saveExisting(session, current);
    }
    
    private static void deleteOld(Session session, List<ModifiedTriangularData> original, List<ModifiedTriangularData> current) {
        for(ModifiedTriangularData mod : original)
            if(!current.contains(mod))
                mod.delete(session);
    }
    
    private static void saveExisting(Session session, List<ModifiedTriangularData> currents) {
        for(ModifiedTriangularData mod : currents)
            mod.save(session);
    }
}
