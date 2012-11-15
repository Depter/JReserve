package org.jreserve.chart;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.jfree.chart.ChartPanel;
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
    
    public static ChartPanel createChartPanel(Chart chart) {
        ChartPanel panel = new ChartPanel(chart.getJFreeChart());
        panel.setPopupMenu(null);
        return panel;
    }
    
    public static NavigablePanel createNavigablePanel(String displayName, Image img, Chart chart) {
        ChartPanel chartPanel = createChartPanel(chart);
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
        ChartPanel chartPanel = createChartPanel(chart);
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
}
