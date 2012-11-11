package org.jreserve.triangle.widget;

/**
 *
 * @author Peter Decsi
 */
public interface TriangleWidgetListener {

    public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue);

    public void commentsChanged();

    public void valuesChanged();

    public void structureChanged();
}
