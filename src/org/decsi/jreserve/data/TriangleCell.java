package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleCell extends Commentable implements Comparable<TriangleCell> {
    
    private int accidnet;
    private int development;
    
    int getAccident() {
        return accidnet;
    }
    
    int getDevelopment() {
        return development;
    }
    
    @Override
    public int compareTo(TriangleCell o) {
        if(o == null)
            return -1;
        int dif = accidnet-o.accidnet;
        return dif!=0? dif : development-o.development;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCell)
            return compareTo((TriangleCell) o) == 0;
        return false;
    }

    @Override
    public int hashCode() {
        return 17*(31+accidnet)+development;
    }
    
    @Override
    public String toString() {
        return String.format("TrinagleCell [%d, %d]", accidnet, development);
    }

    @Override
    public int getCommentTypeId() {
        return Comment.TRIANGLE_CELL;
    }
}
