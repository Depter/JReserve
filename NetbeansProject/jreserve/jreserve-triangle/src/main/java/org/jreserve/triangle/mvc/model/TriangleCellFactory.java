package org.jreserve.triangle.mvc.model;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCellFactory<V> implements CellFactory<V> {
    
    @Override
    public TriangleCell<V> createCell(Date developmentBegin, Date developmentEnd) {
        return new TriangleCell<V>(developmentBegin, developmentEnd);
    }

    
}
