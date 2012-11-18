package org.jreserve.data;

import java.util.*;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTable {

    private ProjectDataType dataType;
    private Map<Date, TreeSet<ClaimValue>> datas = new TreeMap<Date, TreeSet<ClaimValue>>();
    private Date firstAccidentDate;
    private Date lastAccidentDate;
    
    public DataTable(ProjectDataType dataType) {
        this.dataType = dataType;
    }
    
    public ProjectDataType getDataType() {
        return dataType;
    }
    
    public ClaimType getClaimType() {
        return dataType.getClaimType();
    }
    
    public List<Date> getAccidentDates() {
        return new ArrayList<Date>(datas.keySet());
    }
    
    public List<ClaimValue> getDatas(Date accidentDate) {
        Set<ClaimValue> data = datas.get(accidentDate);
        if(data == null)
            data = Collections.EMPTY_SET;
        return new ArrayList<ClaimValue>(data);
    }
    
    public ClaimValue getData(Date accidentDate, Date developmentDate) {
        Set<ClaimValue> data = datas.get(accidentDate);
        if(data == null)
            return null;
        return getData(developmentDate, data);
    }
    
    private ClaimValue getData(Date developmentDate, Set<ClaimValue> data) {
        for(ClaimValue d : data)
            if(d.getDevelopmentDate().equals(developmentDate))
                return d;
        return null;
    }
    
    public Date getFirstAccidnetDate() {
        return firstAccidentDate;
    }
    
    public Date getLastAccidentDate() {
        return lastAccidentDate;
    }
    
    public void addData(ClaimValue data) {
        setDates(data);
        Set<ClaimValue> aData = getCachedDatas(data.getAccidentDate());
        aData.add(data);
    }
    
    private void setDates(ClaimValue data) {
        setFirstAccidentDate(data);
        setLastAccidentDate(data);
    }
    
    private void setFirstAccidentDate(ClaimValue data) {
        Date date = data.getAccidentDate();
        if(firstAccidentDate==null || firstAccidentDate.after(date))
            firstAccidentDate = date;
    }
    
    private void setLastAccidentDate(ClaimValue data) {
        Date date = data.getAccidentDate();
        if(lastAccidentDate==null || lastAccidentDate.before(date))
            lastAccidentDate = date;
    }
    
    private TreeSet<ClaimValue> getCachedDatas(Date accidentDate) {
        TreeSet<ClaimValue> data = datas.get(accidentDate);
        if(data == null) {
            data = new TreeSet<ClaimValue>();
            datas.put(accidentDate, data);
        }
        return data;
    }
    
    public int getDataCount() {
        int count = 0;
        for(Set<ClaimValue> set : datas.values())
            count += set.size();
        return count;
    }
    
    public void cummulate() {
        for(Date accident : datas.keySet())
            cummulate(datas.get(accident));
    }
    
    private void cummulate(Set<ClaimValue> dataSet) {
        double sum = 0d;
        for(ClaimValue data : dataSet) {
            sum += data.getClaimValue();
            data.setClaimValue(sum);
        }
    }
    
    public void deCummulate() {
        for(Date accident : datas.keySet())
            deCummulate(datas.get(accident));
    }
    
    private void deCummulate(TreeSet<ClaimValue> dataSet) {
        ClaimValue prev = null;
        for(Iterator<ClaimValue> it = dataSet.descendingIterator(); it.hasNext();) {
            ClaimValue current = it.next();
            if(prev != null)
                prev.setClaimValue(prev.getClaimValue()-current.getClaimValue());
            prev = current;
        }
    }
    
    @Override
    public String toString() {
        String format = "DataTable [project=%s; dataType=%s]";
        return String.format(format, dataType.getClaimType().getName(), dataType.getName());
    }
}
