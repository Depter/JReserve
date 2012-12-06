package org.jreserve.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.chart.MultiSeriesCategoryChart.SelectableSeries;

/**
 *
 * @author Peter Decsi
 */
class SeriesCheckBoxPanel<R extends Comparable<R>, C extends Comparable<C>> extends JPanel implements ChangeListener {

    private MultiSeriesCategoryChart<R, C> chart;
    private GridBagConstraints gc;
    private boolean myChange = false;
    
    SeriesCheckBoxPanel(MultiSeriesCategoryChart<R, C> chart) {
        this.chart = chart;
        chart.addChangeListener(this);
        super.setLayout(new GridBagLayout());
        super.setBorder(new EmptyBorder(0, 5, 5, 5));
    }
    
    private void initGc() {
        gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.gridx = 0;
        gc.weightx = 1d;
        gc.weighty = 0d;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.insets = new java.awt.Insets(5, 0, 0, 0);
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if(!myChange) { 
            clearPanel();
            fillPanel();
        }
    }
    
    private void clearPanel() {
        super.removeAll();
        initGc();
    }

    private void fillPanel() {
        for(SelectableSeries serie : chart.series)
            createCheckBox(serie);
        addGlue();
    }
    
    private void createCheckBox(final SelectableSeries serie) {
        String str = serie.getName();
        final JCheckBox cb = new JCheckBox(str, serie.isSelected());
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeriesCheckBoxPanel.this.myChange = true;
                chart.setSeriesShown((R) serie.getRowKey(), cb.isSelected());
                SeriesCheckBoxPanel.this.myChange = false;
            }
        });
        super.add(cb, gc);
        gc.gridy++;
    }

    private void addGlue() {
        gc.weightx = 1d;
        gc.weighty = 1d;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new java.awt.Insets(0, 0, 0, 0);
        super.add(Box.createGlue(), gc);
    }
}
