package org.jreserve.triangle.widget.model;

import java.awt.Image;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EmptyTriangleModel implements TriangleModel {

    //final static TriangleModel EMPTY = new EmptyTriangleModel();
    
    EmptyTriangleModel() {
    }
    
    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public String getToolTipName() {
        return null;
    }

    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
    }

    @Override
    public void setTriangleCells(TriangleCell[][] cells) {
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public String getRowName(int row) {
        return null;
    }

    @Override
    public TriangleCell getCellAt(int row, int column) {
        return null;
    }

    @Override
    public TriangleModel createInstance() {
        return new EmptyTriangleModel();
    }
}
