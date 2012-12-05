package org.jreserve.estimates.factors.visual;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.jreserve.estimates.factors.factorestimator.FactorEstimatorComponent;
import org.jreserve.estimates.factors.factorestimator.FactorEstimatorRegistry;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidgetAdapter;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.FactorSelectionPanel.Title=Factor selection",
    "LBL.FactorSelectionPanel.Methods=Method",
    "LBL.FactorSelectionPanel.Manual=Manual"
})
public class FactorSelectionPanel extends NavigablePanel {

    private FactorsPanel factorsPanel;
    private FactorSelectionModel selectionModel;
    
    public FactorSelectionPanel(FactorsPanel factorsPanel) {
        super(Bundle.LBL_FactorSelectionPanel_Title(), null);
        initFactorsPanel(factorsPanel);
        initComponents();
    }
    
    private void initFactorsPanel(FactorsPanel factorsPanel) {
        this.factorsPanel = factorsPanel;
        factorsPanel.getTriangleWidget().addTriangleWidgetListener(new WidgetListener());
    }
    
    private void initComponents() {
        selectionModel = new FactorSelectionModel();
        JTable table = new JTable(selectionModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(false);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(200, 150));
        setContent(scroll);
    }
    
    private class WidgetListener extends TriangleWidgetAdapter {

        @Override
        public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
            selectionModel.updateInput();
        }

        @Override
        public void valuesChanged() {
            selectionModel.updateInput();
        }

        @Override
        public void structureChanged() {
            selectionModel.updateInput();
        }
    } 
    
    private class FactorSelectionModel extends AbstractTableModel {
        
        private List<FactorEstimatorComponent> estimators = FactorEstimatorRegistry.getFactorEstimates();
        private Map<FactorEstimatorComponent, double[]> estimations = new HashMap<FactorEstimatorComponent, double[]>();
        
        private int factorColumnCount;
        private double[][] factors;
        private double[][] input;
        
        private void updateInput() {
            clearEstimations();
            factors = factorsPanel.getTriangleWidget().flattenIncludedCells();
            input = factorsPanel.flattenIncludedInputs();
            calculateColumnCount();
            super.fireTableStructureChanged();
        }
        
        private void clearEstimations() {
            estimations.clear();
            for(FactorEstimatorComponent estimator : estimators)
                estimations.put(estimator, null);
        }
        
        private void calculateColumnCount() {
            factorColumnCount = 0;
            if(factors != null)
                for(double[] row : factors)
                    calculateColumnCount(row);
        }
        
        private void calculateColumnCount(double[] row) {
            if(row == null) return;
            int length = row.length;
            if(length <= factorColumnCount) return;
            for(int i=length; i>factorColumnCount; i--) {
                if(!Double.isNaN(row[i-1])) {
                    factorColumnCount = i;
                    return;
                }
            }
        }
        
        @Override
        public int getRowCount() {
            return estimators.size() + 1;
        }

        @Override
        public int getColumnCount() {
            return factorColumnCount + 1;
        }

        @Override
        public String getColumnName(int index) {
            if(index == 0)
                return Bundle.LBL_FactorSelectionPanel_Methods();
            return ""+index;
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            if(column == 0)
                return getRowName(row);
            return getFactor(row, column - 1);
        }
        
        private String getRowName(int row) {
            if(row < estimators.size())
                return estimators.get(row).getName();
            return Bundle.LBL_FactorSelectionPanel_Manual();
        }
        
        private Double getFactor(int row, int column) {
            if(row < estimators.size())
                return getEstimation(row, column);
            return null;
        }
        
        private Double getEstimation(int row, int column) {
            double[] estimates = getEstimation(row);
            if(column >= estimates.length || Double.isNaN(estimates[column]))
                return null;
            return estimates[column];
        }
        
        private double[] getEstimation(int row) {
            FactorEstimatorComponent estimator = estimators.get(row);
            double[] estimates = estimations.get(estimator);
            if(estimates == null) {
                estimates = estimator.getFactors(input, factors);
                estimations.put(estimator, estimates);
            }
            return estimates;
        }
    }
}
