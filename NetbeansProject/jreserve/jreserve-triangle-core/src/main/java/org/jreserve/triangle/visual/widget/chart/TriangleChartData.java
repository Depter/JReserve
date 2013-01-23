package org.jreserve.triangle.visual.widget.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.chart.ChartData;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.util.TriangleUtil;
import org.jreserve.triangle.visual.widget.TriangleWidgetProperties;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class TriangleChartData<R extends Comparable<R>, C extends Comparable<C>> extends ChartData<R, C> implements ChangeListener {
    
    protected Lookup lookup;
    private Result<TriangularData.Provider> dataResult;
    private LookupListener dataListener = new DataListener();
    private ChangeListener changeListener = new DataChangeListener();
    protected TriangularData data = TriangularData.EMPTY;
    
    private Result<TriangleWidgetProperties> propertyResult;
    private LookupListener propertiesListener = new PropertiesListener();
    private ChangeListener propertiesChangeListener = new PropertiesChangeListener();
    private TriangleWidgetProperties properties;
    private boolean cummulated;
    
    protected TriangleChartData(Lookup lookup) {
        if(lookup != null)
            initState(lookup);
    }
    
    protected TriangleChartData(TriangularData data) {
        initData(data);
    }
    
    private void initState(Lookup lookup) {
        this.lookup = lookup;
        initDataState();
        initPropertiesState();
    }
    
    private void initDataState() {
        this.dataResult = lookup.lookupResult(TriangularData.Provider.class);
        this.dataResult.addLookupListener(dataListener);
        initData(lookup.lookup(TriangularData.class));
    }
    
    private void initData(TriangularData data) {
        this.data = data==null? TriangularData.EMPTY : data;
        this.data.addChangeListener(changeListener);
    }
    
    private void initPropertiesState() {
        propertyResult = lookup.lookupResult(TriangleWidgetProperties.class);
        propertyResult.addLookupListener(propertiesListener);
        initProperties(lookup.lookup(TriangleWidgetProperties.class));
    }
    
    private void initProperties(TriangleWidgetProperties property) {
        if(this.properties != null)
            this.properties.removeChangeListener(propertiesChangeListener);
        
        this.properties = property;
        if(this.properties != null)
            this.properties.addChangeListener(propertiesChangeListener);
        
        setCummulated(isCummulated());
    }
    
    private void setCummulated(boolean cummulated) {
        if(this.cummulated != cummulated) {
            this.cummulated = cummulated;
            fireChangeEvent();
        }
    }
    
    private boolean isCummulated() {
        return properties!=null && 
               properties.isCummualted();
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        fireChangeEvent();
    }
    
    protected List<Date> getAccidentDates() {
        int aCount = data.getAccidentCount();
        List<Date> dates = new ArrayList<Date>(aCount);
        for(int a=0; a<aCount; a++)
            dates.add(data.getAccidentName(a));
        return dates;
    }
    
    protected double[][] getValues() {
        double[][] values = data.toArray();
        if(cummulated)
            TriangleUtil.cummulate(values);
        return values;
    }
    
    private class DataListener implements LookupListener {
        @Override
        public void resultChanged(LookupEvent le) {
            data.removeChangeListener(changeListener);
            lookupData();
            fireChangeEvent();
        }
        
        private void lookupData() {
            TriangularData.Provider source = lookup.lookup(TriangularData.Provider.class);
            initData(source==null? null : source.getTriangularData());
        }
    }
    
    private class DataChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }
    }    
    
    private class PropertiesListener implements LookupListener {
        @Override
        public void resultChanged(LookupEvent le) {
            data.removeChangeListener(changeListener);
            lookupData();
            fireChangeEvent();
        }
        
        private void lookupData() {
            TriangularData.Provider source = lookup.lookup(TriangularData.Provider.class);
            initData(source==null? null : source.getTriangularData());
        }
    }
    
    private class PropertiesChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            setCummulated(properties.isCummualted());
        }
    }    
}
