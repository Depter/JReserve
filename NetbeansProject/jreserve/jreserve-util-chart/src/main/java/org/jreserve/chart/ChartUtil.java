package org.jreserve.chart;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.navigator.NavigablePanelCopyButton;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class ChartUtil {

    public final static Image CHART_BAR = ImageUtilities.loadImage("resources/chart_bar.png", false);
    public final static Image CHART_XY = ImageUtilities.loadImage("resources/chart_xy.png", false);
    public final static java.awt.Color BACKGROUND = new java.awt.Color(255, 125, 48);
    
    public static ChartPanel createChartPanel(Chart chart, boolean fixedRangeAxis) {
        ChartPanel panel = new ChartPanel(chart.getJFreeChart());
        panel.setPopupMenu(fixedRangeAxis? null : createPopUp(chart));
        return panel;
    }
    
    private static JPopupMenu createPopUp(Chart chart) {
        JPopupMenu popUp = new JPopupMenu();
        popUp.add(new FixRangeAction(chart));
        return popUp;
    }
    
    public static NavigablePanel createNavigablePanel(String displayName, Image img, Chart chart, boolean fixedRangeAxis) {
        ChartPanel chartPanel = createChartPanel(chart, fixedRangeAxis);
        return createNavigablePanel(displayName, img, chartPanel, chartPanel);
    }
    
    private static NavigablePanel createNavigablePanel(String displayName, Image img, ChartPanel chart, JComponent content) {
        NavigablePanel panel = new NavigablePanel(displayName, img);
        panel.setOpened(false);
        panel.setContent(content);
        panel.setBackground(BACKGROUND);
        panel.addUserTitleComponent(new NavigablePanelCopyButton(new ChartCopy(chart)));
        return panel;
    }
    
    public static <R extends Comparable<R>, C extends Comparable<C>> NavigablePanel createMultiSeriesNavigablePanel(String displayName, Image img, MultiSeriesCategoryChart<R, C> chart) {
        SeriesCheckBoxPanel<R, C> checkPanel = new SeriesCheckBoxPanel<R, C>(chart);
        ChartPanel chartPanel = createChartPanel(chart, false);
        JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(checkPanel), chartPanel);
        splitPanel.setDividerLocation(0.2);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.getComponent(2).setBackground(BACKGROUND);
        
        return createNavigablePanel(displayName, img, chartPanel, splitPanel);
    }
    
    private static class ChartCopy implements ActionListener {
        
        private ChartPanel panel;

        private ChartCopy(ChartPanel panel) {
            this.panel = panel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.doCopy();
        }
    }
    
    private static class FixRangeAction extends AbstractAction {
        
        private Chart chart;
        private boolean fixScale = true;
        
        private FixRangeAction(Chart chart) {
            this.chart = chart;
            putValue(NAME, "Fix scale");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            CategoryPlot plot = (CategoryPlot) chart.getJFreeChart().getPlot();
            NumberAxis axis = (NumberAxis) plot.getRangeAxis();
            axis.setAutoRangeIncludesZero(fixScale);
            
            fixScale = !fixScale;
            putValue(NAME, fixScale? "Fix scale" : "Dynamic scale");
        }
        
    }
}
