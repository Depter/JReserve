package org.jreserve.chart;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author Peter Decsi
 */
public class ChartFormat {
    
    public String getChartTitle() {
        return null;
    }
    
    public String getDomainAxisTitle() {
        return null;
    }
    
    public String getRangeAxisTitle() {
        return null;
    }
    
    public PlotOrientation getPlotOrientation() {
        return PlotOrientation.VERTICAL;
    }
    
    public boolean showLegend() {
        return true;
    }
    
    public boolean showToolTip() {
        return true;
    }
    
    public boolean showURLs() {
        return false;
    }
    
    public void formatPlot(Plot plot) {
    }

}
