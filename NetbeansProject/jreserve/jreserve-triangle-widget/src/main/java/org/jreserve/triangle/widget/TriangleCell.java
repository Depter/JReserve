package org.jreserve.triangle.widget;

import java.util.*;
import org.jreserve.triangle.entities.Comment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCell {
    
    public final static int VALUE_LAYER = 10;
    public final static int SMOOTHING_LAYER = 20;
    public final static int CORRECTION_LAYER = 30;
    
    private int row;
    private int column;
    private Date accidentBegin;
    private Date accidentEnd;
    private Date developmentBegin;
    private Date developmentEnd;
    
    private Map<Integer, Double> mValues = new TreeMap<Integer, Double>();
    
    //private List<Double> values = new ArrayList<Double>();
    private int displayLayer = -1;
    
    private TriangleCell previous;
    private boolean cummulated;
    
    private List<Comment> comments = new ArrayList<Comment>();
    
    public TriangleCell(TriangleCell previous, int row, int column, Date aBegin, Date aEnd, Date dBegin, Date dEnd) {
        this.previous = previous;
        this.row = row;
        this.column = column;
        this.accidentBegin = aBegin;
        this.accidentEnd = aEnd;
        this.developmentBegin = dBegin;
        this.developmentEnd = dEnd;
    }

    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    public Date getAccidentBegin() {
        return accidentBegin;
    }

    public Date getAccidentEnd() {
        return accidentEnd;
    }

    public Date getDevelopmentBegin() {
        return developmentBegin;
    }

    public Date getDevelopmentEnd() {
        return developmentEnd;
    }
    
    public WidgetData<Double> getData(int layer) {
        Double value = mValues.get(layer);
//        Double value = values.get(layer);
        if(value == null)
            return null;
        return new WidgetData<Double>(accidentBegin, developmentBegin, value);
    }
    
    public int getLayerCount() {
        Integer max = null;
        for(Integer l : mValues.keySet())
            if(max == null || max.intValue()<l.intValue())
                max = l;
        return max==null? 0 : max.intValue();
    }
    
    public Double getValueAt(int layer) {
        return mValues.get(layer);
        //return values.get(layer);
    }
    
    void setValueAt(int layer, Double value) {
        if(value == null) {
            mValues.remove(layer);
        } else {
            mValues.put(layer, value);
        }
        //this.values.set(layer, value);
        setDisplayLayer();
    }
    
    private void setDisplayLayer() {
        displayLayer = -1;
        for(Integer layer : mValues.keySet())
            displayLayer = layer.intValue();
//        for(int i=0, size=values.size(); i<size; i++) 
//            if(values.get(i) != null)
//                displayLayer = i;
    }
    
    public int getDisplayedLayer() {
        return displayLayer;
    }
    
    public Double getDisplayValue() {
        Double value = mValues.get(displayLayer);
        //Double value = displayLayer<0? null : values.get(displayLayer);
        if(cummulated && previous != null)
            value = TriangleCellUtil.add(value, previous.getDisplayValue());
        return value;
    }
    
    void setCummulated(boolean cummulated) {
        this.cummulated = cummulated;
    }
    
    void clear() {
        displayLayer = -1;
        mValues.clear();
        //values.clear();
    }
    
    void setValues(List<Double> values) {
        clear();
        for(int i=0, size=values.size(); i<size; i++) {
            Double value = values.get(i);
            if(value != null)
                mValues.put(i, value);
        }
        setDisplayLayer();
        //if(values != null && !values.isEmpty()) {
        //    this.values.addAll(values);
        //    setDisplayLayer();
        //}
    }
    
    List<Double> getValues() {
        if(displayLayer < 0)
            return Collections.EMPTY_LIST;
        List<Double> values = new ArrayList<Double>(displayLayer+1);
        for(int i=0; i<=displayLayer; i++)
            values.add(mValues.get(i));
        return new ArrayList<Double>(values);
    }
    
    public boolean acceptsData(WidgetData data) {
        return acceptsDates(
                data.getAccident(), 
                data.getDevelopment()
                );
    }
    
    public boolean acceptsDates(Date accident, Date development) {
        return accidentAccepts(accident) &&
               developmentAccepts(development);
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
    
    public Double getValueUnder(int layer) {
        for(int l=layer-1; l>=0; l--) {
            Double value = mValues.get(l);
            if(value != null)
                return value;
        }
        return null;
    }
    
    public void addComment(WidgetData<Comment> data) {
        if(acceptsData(data))
            addComment(data.getValue());
    }
    
    private void addComment(Comment comment) {
        if(comment == null)
            throw new NullPointerException("Comment is null!");
        if(!comments.contains(comment)) {
            comments.add(comment);
            Collections.sort(comments, CommentComparator.INSTANCE);
        }
    }
    
    public void clearComments() {
        comments.clear();
    }
    
    public List<Comment> getComments() {
        return new ArrayList<Comment>(comments);
    }
    
    public boolean hasComments() {
        return !comments.isEmpty();
    }
}
