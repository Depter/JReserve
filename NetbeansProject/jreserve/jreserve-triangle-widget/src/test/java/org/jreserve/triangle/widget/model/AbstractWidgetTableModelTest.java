package org.jreserve.triangle.widget.model;

import java.util.Date;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.TriangularData;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractWidgetTableModelTest {
    
    private final static double[][] DATA = {
        {1 , 2 , 3, 4},
        {5 , 6 , 7, 8},
        {9 , 10},
        {11},
    };

    
    private static TriangularData data;
    private WidgetTableModel model;
    
    public AbstractWidgetTableModelTest() {
    }
    
    @Before
    public void setUp() {
        model = new AbstractWidgetTableModelImpl();
    }

    @Test
    public void testSetData() {
    }

    @Test
    public void testSetCummulated() {
    }

    @Test
    public void testIsCummulated() {
    }

    @Test
    public void testSetWidgetEditor() {
    }

    @Test
    public void testGetWidgetEditor() {
    }

    @Test
    public void testGetLayerId() {
    }

    @Test
    public void testGetRowCount() {
    }

    @Test
    public void testGetColumnCount() {
    }

    @Test
    public void testGetColumnClass() {
    }

    @Test
    public void testGetRowTitleClass() {
    }

    @Test
    public void testIsCellEditable() {
    }

    @Test
    public void testGetValueAt() {
    }

    @Test
    public void testGetRowName() {
    }

    @Test
    public void testGetData() {
    }

    @Test
    public void testSetValueAt() {
    }

    @Test
    public void testStateChanged() {
    }

    public class AbstractWidgetTableModelImpl extends AbstractWidgetTableModel {

        @Override
        public String getRowName(int row) {
            return "";
        }

        @Override
        protected int getAccident(int row, int column) {
            return row;
        }

        @Override
        protected int getDevelopment(int row, int column) {
            return column;
        }
    }

    private static class TriangularDataImpl implements TriangularData {

        private double[][] data;
        
        @Override
        public int getAccidentCount() {
            return data.length;
        }

        @Override
        public int getDevelopmentCount() {
            return getDevelopmentCount(0);
        }

        @Override
        public int getDevelopmentCount(int accident) {
            if(data == null)
                return 0;
            return data[accident].length;
        }

        @Override
        public Date getAccidentName(int accident) {
            return new Date(accident);
        }

        @Override
        public Date getDevelopmentName(int accident, int development) {
            return new Date(accident+development);
        }

        @Override
        public double getValue(int accident, int development) {
            return data[accident][development];
        }

        @Override
        public void addChangeListener(ChangeListener listener) {
        }

        @Override
        public void removeChangeListener(ChangeListener listener) {
        }

        @Override
        public double[][] getData() {
            return data;
        }

        @Override
        public String getLayerTypeId(int accident, int development) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
    }
}