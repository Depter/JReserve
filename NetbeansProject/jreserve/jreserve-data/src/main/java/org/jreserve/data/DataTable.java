package org.jreserve.data;

import java.util.*;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTable {

    private ProjectDataType dataType;
    private Map<Date, TreeSet<Data>> datas = new TreeMap<Date, TreeSet<Data>>();
    private Date firstAccidentDate;
    private Date lastAccidentDate;
    
    public DataTable(ProjectDataType dataType) {
        this.dataType = dataType;
    }
    
    public ProjectDataType getDataType() {
        return dataType;
    }
    
    public Project getProject() {
        return dataType.getProject();
    }
    
    public ClaimType getClaimType() {
        return getProject().getClaimType();
    }
    
    public List<Date> getAccidentDates() {
        return new ArrayList<Date>(datas.keySet());
    }
    
    public List<Data> getDatas(Date accidentDate) {
        Set<Data> data = datas.get(accidentDate);
        if(data == null)
            data = Collections.EMPTY_SET;
        return new ArrayList<Data>(data);
    }
    
    public Data getData(Date accidentDate, Date developmentDate) {
        Set<Data> data = datas.get(accidentDate);
        if(data == null)
            return null;
        return getData(developmentDate, data);
    }
    
    private Data getData(Date developmentDate, Set<Data> data) {
        for(Data d : data)
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
    
    public void addData(Data data) {
        setDates(data);
        Set<Data> aData = getCachedDatas(data.getAccidentDate());
        aData.add(data);
    }
    
    private void setDates(Data data) {
        setFirstAccidentDate(data);
        setLastAccidentDate(data);
    }
    
    private void setFirstAccidentDate(Data data) {
        Date date = data.getAccidentDate();
        if(firstAccidentDate==null || firstAccidentDate.after(date))
            firstAccidentDate = date;
    }
    
    private void setLastAccidentDate(Data data) {
        Date date = data.getAccidentDate();
        if(lastAccidentDate==null || lastAccidentDate.before(date))
            lastAccidentDate = date;
    }
    
    private TreeSet<Data> getCachedDatas(Date accidentDate) {
        TreeSet<Data> data = datas.get(accidentDate);
        if(data == null) {
            data = new TreeSet<Data>();
            datas.put(accidentDate, data);
        }
        return data;
    }
    
    public int getDataCount() {
        int count = 0;
        for(Set<Data> set : datas.values())
            count += set.size();
        return count;
    }
    
    public void cummulate() {
        for(Date accident : datas.keySet())
            cummulate(datas.get(accident));
    }
    
    private void cummulate(Set<Data> dataSet) {
        double sum = 0d;
        for(Data data : dataSet) {
            sum += data.getValue();
            data.setValue(sum);
        }
    }
    
    public void deCummulate() {
        for(Date accident : datas.keySet())
            deCummulate(datas.get(accident));
    }
    
    private void deCummulate(TreeSet<Data> dataSet) {
        Data prev = null;
        for(Iterator<Data> it = dataSet.descendingIterator(); it.hasNext();) {
            Data current = it.next();
            if(prev != null)
                prev.setValue(prev.getValue()-current.getValue());
            prev = current;
        }
    }
    
    @Override
    public String toString() {
        String format = "DataTable [project=%s; dataType=%s]";
        return String.format(format, dataType.getProject().getName(), dataType.getName());
    }
}
