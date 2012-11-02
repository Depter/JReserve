package org.jreserve.triangle.widget.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCell {
    
    private Date accidentBegin;
    private Date accidentEnd;
    private Date developmentBegin;
    private Date developmentEnd;
    
    private List<Double> values = new ArrayList<Double>();
    private int displayLayer = -1;
    
    private List<Comment> comments = new ArrayList<Comment>();
    
    
    public TriangleCell(Date aBegin, Date aEnd, Date dBegin, Date dEnd) {
        this.accidentBegin = aBegin;
        this.accidentEnd = aEnd;
        this.developmentBegin = dBegin;
        this.developmentEnd = dEnd;
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
        Double value = values.get(layer);
        if(value == null)
            return null;
        return new WidgetData<Double>(accidentBegin, developmentBegin, value);
    }
    
    public int getLayerCount() {
        return values.size();
    }
    
    public Double getValueAt(int layer) {
        return values.get(layer);
    }
    
    public void setValueAt(int layer, Double value) {
        this.values.set(layer, value);
        setDisplayLayer();
    }
    
    private void setDisplayLayer() {
        for(int i=0, size=values.size(); i<size; i++) 
            if(values.get(i) != null)
                displayLayer = i;
    }
    
    public int getDisplayedLayer() {
        return displayLayer;
    }
    
    public Double getDisplayValue() {
        if(displayLayer < 0)
            return null;
        return values.get(displayLayer);
    }
    
    void clear() {
        displayLayer = -1;
        values.clear();
    }
    
    void setValues(List<Double> values) {
        clear();
        if(values != null && !values.isEmpty()) {
            this.values.addAll(values);
            setDisplayLayer();
        }
    }
    
    List<Double> getValues() {
        return new ArrayList<Double>(values);
    }
    
    boolean acceptsData(WidgetData data) {
        return accidentAccepts(data.getAccident()) &&
               developmentAccepts(data.getDevelopment());
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