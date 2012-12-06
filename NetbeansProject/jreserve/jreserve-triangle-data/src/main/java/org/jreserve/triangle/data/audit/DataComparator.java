package org.jreserve.triangle.data.audit;

import java.util.Comparator;
import org.jreserve.audit.JReserveRevisionEntity;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class DataComparator implements Comparator<Object[]> {

    public final static DataComparator COMPARATOR = new DataComparator();
            
    @Override
    public int compare(Object[] o1, Object[] o2) {
        if(o1==null)
            return o2==null? 0 : 1;
        if(o2 == null)
            return -1;
        return compareNotNull(o1, o2);
    }
    

    private int compareNotNull(Object[] o1, Object[] o2) {
        int dif = compareEntity(o1[0], o2[0]);
        if(dif != 0)
            return dif;
        return compareDates(o1[1], o2[1]);
    }
    
    private int compareEntity(Object o1, Object o2) {
        PersistentObject d1 = (PersistentObject) o1;
        PersistentObject d2 = (PersistentObject) o2;
        return d1.getId().compareTo(d2.getId());
    }
    
    private int compareDates(Object o1, Object o2) {
        long t1 = ((JReserveRevisionEntity) o1).getRevisionTimestamp();
        long t2 = ((JReserveRevisionEntity) o2).getRevisionTimestamp();
        if(t1 < t2)
            return -1;
        return t1>t2? 1 : 0;
    }
}
