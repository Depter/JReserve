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
    private Map<Date, Set<Data>> datas = new TreeMap<Date, Set<Data>>();
    private Date firstAccidentDate;
    private Date lastAccidentDate;
    
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
    
    private Set<Data> getCachedDatas(Date accidentDate) {
        Set<Data> data = datas.get(accidentDate);
        if(data == null) {
            data = new TreeSet<Data>();
            datas.put(accidentDate, data);
        }
        return data;
    }
}
