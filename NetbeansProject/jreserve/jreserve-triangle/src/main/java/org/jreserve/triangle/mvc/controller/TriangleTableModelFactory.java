package org.jreserve.triangle.mvc.controller;

import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleTableModelFactory {

    public TriangleTableModel createModel(TriangleTable table);
}
