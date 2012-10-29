package org.jreserve.triangle.mvc2.data;

import java.util.*;
import org.jreserve.data.Data;
import org.jreserve.data.DataComment;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCell {

    private final static Comparator<DataComment> COMMENT_COMPARATOR = new CommentComparator();
    
    private Date accidentBegin;
    private Date accidentEnd;
    private Date developmentBegin;
    private Date developmentEnd;
    
    private List<Double> values = new ArrayList<Double>();
    private List<DataComment> comments;
    
    public TriangleCell(Date aBegin, Date aEnd, Date dBegin, Date dEnd) {
        this.accidentBegin = aBegin;
        this.accidentEnd = aEnd;
        this.developmentBegin = dBegin;
        this.developmentEnd = dEnd;
    }
    
    public void setValue(List<List<Data<PersistentObject, Double>>> datas) {
        this.values.clear();
        for(List<Data<PersistentObject, Double>> dataList : datas)
            values.add(sum(dataList));
    }
    
    private Double sum(List<Data<PersistentObject, Double>> datas) {
        Double sum = null;
        for(Data<? extends PersistentObject, Double> data : datas)
            if(acceptsData(data))
                sum = add(sum, data.getValue());
        return sum;
    }
    
    private boolean acceptsData(Data data) {
        return accidentAccepts(data.getAccidentDate()) &&
               developmentAccepts(data.getDevelopmentDate());
    }
    
    private boolean accidentAccepts(Date date) {
        if(date.before(accidentBegin))
            return false;
        return date.before(accidentEnd);
    }
    
    private boolean developmentAccepts(Date date) {
        if(date.before(developmentBegin))
            return false;
        return date.before(developmentEnd);
    }
    
    private Double add(Double sum, Double v) {
        if(v == null) { 
            return sum;
        } else if(sum == null || Double.isNaN(sum) || Double.isNaN(v)) {
            return v;
        } else {
            return sum + v;
        }
    }
    
    public int getLayerCount() {
        return values.size();
    }
    
    public Double getValueAt(int layer) {
        return values.get(layer);
    }
    
    public int getTopValueLayer() {
        int size = values.size();
        for(int l=size-1; l>=0; l--)
            if(values.get(l) != null)
                return l;
        return -1;
    }
    
    public Double getValue() {
        int size = values.size();
        for(int l=size-1; l>=0; l--) {
            Double value = values.get(l);
            if(value != null)
                return value;
        }
        return null;
    }
    
    public void setValue(int layer, Double value) {
        this.values.set(layer, value);
    }
    
    public void clearValue() {
        this.values.clear();
    }
    
    public void cummulate(TriangleCell previous) {
        checkSize(previous);
        cummulateWith(previous.values);
    }
    
    private void checkSize(TriangleCell previous) {
        int mySize = values.size();
        int prevSize = previous.values.size();
        if(mySize != prevSize)
            throwDifferentSizeException(mySize, prevSize);
    }
    
    private void throwDifferentSizeException(int mySize, int prevSize) {
        String msg = "Different value sizes! this=%d, previous=%d";
        msg = String.format(msg, mySize, prevSize);
        throw new IllegalArgumentException(msg);
    }
    
    private void cummulateWith(List<Double> previous) {
        for(int i=0, length=values.size(); i<length; i++) {
            Double v = values.get(i);
            Double p = previous.get(i);
            values.set(i, add(v, p));
        }
    }
    
    public void deCummulate(TriangleCell previous) {
        checkSize(previous);
        deCummulateWith(previous.values);
    }
    
    private void deCummulateWith(List<Double> previous) {
        for(int i=0, length=values.size(); i<length; i++) {
            Double v = values.get(i);
            Double p = previous.get(i);
            values.set(i, subtract(v, p));
        }
    }
    
    private Double subtract(Double a, Double b) {
        if(b == null) 
            return a;
        if(a == null)
            return b==null? null : -b;
        if(Double.isNaN(a) || Double.isNaN(b))
            return Double.NaN;
        return a - b;
    }
    
    public void addComment(Data<? extends PersistentObject, DataComment> data) {
        if(data == null)
            throw new NullPointerException("Comment data is null!");
        if(acceptsData(data))
            addComment(data.getValue());
    }
    
    private void addComment(DataComment comment) {
        if(comment == null)
            throw new NullPointerException("Comment is null!");
        if(!comments.contains(comment)) {
            comments.add(comment);
            Collections.sort(comments, COMMENT_COMPARATOR);
        }
    }
    
    public void clearComments() {
        comments.clear();
    }
    
    private static class CommentComparator implements Comparator<DataComment> {

        @Override
        public int compare(DataComment o1, DataComment o2) {
            int dif = compare(o1.getCreationDate(), o2.getCreationDate());
            if(dif != 0)
                return dif;
            return compare(o1.getUserName(), o2.getUserName());
        }
    
        private <T extends Comparable<T>> int compare(T c1, T c2) {
            if(c1==null)
                return c2==null? 0 : 1;
            return c2==null? -1 : c1.compareTo(c2);
        }
    }
}
