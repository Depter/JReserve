package org.jreserve.data;

import java.util.*;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTable {

    private ProjectDataType dataType;
    private Map<Date, TreeSet<Data<ProjectDataType, Double>>> datas = new TreeMap<Date, TreeSet<Data<ProjectDataType, Double>>>();
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
    
    public List<Data<ProjectDataType, Double>> getDatas(Date accidentDate) {
        Set<Data<ProjectDataType, Double>> data = datas.get(accidentDate);
        if(data == null)
            data = Collections.EMPTY_SET;
        return new ArrayList<Data<ProjectDataType, Double>>(data);
    }
    
    public Data<ProjectDataType, Double> getData(Date accidentDate, Date developmentDate) {
        Set<Data<ProjectDataType, Double>> data = datas.get(accidentDate);
        if(data == null)
            return null;
        return getData(developmentDate, data);
    }
    
    private Data<ProjectDataType, Double> getData(Date developmentDate, Set<Data<ProjectDataType, Double>> data) {
        for(Data<ProjectDataType, Double> d : data)
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
    
    public void addData(Data<ProjectDataType, Double> data) {
        setDates(data);
        Set<Data<ProjectDataType, Double>> aData = getCachedDatas(data.getAccidentDate());
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
    
    private TreeSet<Data<ProjectDataType, Double>> getCachedDatas(Date accidentDate) {
        TreeSet<Data<ProjectDataType, Double>> data = datas.get(accidentDate);
        if(data == null) {
            data = new TreeSet<Data<ProjectDataType, Double>>();
            datas.put(accidentDate, data);
        }
        return data;
    }
    
    public int getDataCount() {
        int count = 0;
        for(Set<Data<ProjectDataType, Double>> set : datas.values())
            count += set.size();
        return count;
    }
    
    public void cummulate() {
        for(Date accident : datas.keySet())
            cummulate(datas.get(accident));
    }
    
    private void cummulate(Set<Data<ProjectDataType, Double>> dataSet) {
        double sum = 0d;
        for(Data<ProjectDataType, Double> data : dataSet) {
            sum += data.getValue();
            data.setValue(sum);
        }
    }
    
    public void deCummulate() {
        for(Date accident : datas.keySet())
            deCummulate(datas.get(accident));
    }
    
    private void deCummulate(TreeSet<Data<ProjectDataType, Double>> dataSet) {
        Data<ProjectDataType, Double> prev = null;
        for(Iterator<Data<ProjectDataType, Double>> it = dataSet.descendingIterator(); it.hasNext();) {
            Data<ProjectDataType, Double> current = it.next();
            if(prev != null)
                prev.setValue(prev.getValue()-current.getValue());
            prev = current;
        }
    }
    
    @Override
    public String toString() {
        String format = "DataTable [project=%s; dataType=%s]";
        return String.format(format, dataType.getClaimType().getName(), dataType.getName());
    }
}
