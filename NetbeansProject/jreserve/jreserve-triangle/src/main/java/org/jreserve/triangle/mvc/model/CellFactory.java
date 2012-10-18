package org.jreserve.triangle.mvc.model;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CellFactory<V> {

    public TriangleCell<V> createCell(Date developmentBegin, Date developmentEnd);
}
