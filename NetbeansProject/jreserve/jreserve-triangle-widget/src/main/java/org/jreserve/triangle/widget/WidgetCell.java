package org.jreserve.triangle.widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetCell implements Comparable<WidgetCell> {

    private final int accident;
    private final int development;

    public WidgetCell(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }

    public int getAccident() {
        return accident;
    }

    public int getDevelopment() {
        return development;
    }

    @Override
    public int compareTo(WidgetCell c) {
        if(c == null)
            return -1;
        int dif = accident - c.accident;
        return dif != 0 ? dif : development - c.development;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof WidgetCell)
            return compareTo((WidgetCell)o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accident;
        return 17 * hash + development;
    }
    
    @Override
    public String toString() {
        return String.format(
            "WidgetCellSelection [%d; %d]",
            accident, development);
    }
}
