package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleInput implements TriangularData {

    private final int accidentCount;
    private final int developmentCount;
    private final Date[] accidentDates;
    private final Date[][] developmentDates;
    private final double[][] values;

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    TriangleInput(Date[] accidentDates, Date[][] developmentDates, double[][] values) {
        this.accidentCount = values.length;
        this.developmentCount = accidentCount==0? 0 : values[0].length;
        this.accidentDates = accidentDates;
        this.developmentDates = developmentDates;
        this.values = values;
    }
    
    @Override
    public int getAccidentCount() {
        return values.length;
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(accident >= accidentCount)
            return 0;
        return values[accident].length;
    }

    @Override
    public Date getAccidentName(int accident) {
        return accidentDates[accident];
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return developmentDates[accident][development];
    }

    @Override
    public double getValue(int accident, int development) {
        return values[accident][development];
    }
    
    @Override
    public String toString() {
        return String.format("TriangleInput [%d; %d]", accidentCount, developmentCount);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
