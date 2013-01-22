package org.jreserve.factor.core.linkratio.smoothing;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.factor.core.linkratio.smoothing.util.DefaultLinkRatioSmoothingMethod;
import org.jreserve.persistence.AbstractPersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSmoothingContainer extends AbstractPersistentObject {

    private int developmentCount;
    
    private Set<LinkRatioSmoothing> cells = new TreeSet<LinkRatioSmoothing>();
    
    
    public int getDevelopmentCount() {
        return developmentCount;
    }
    
    public void setDevelopmentCount(int count) {
        cleanUpCellsAfterLength(count);
        fillCellsTillLength(count);
        this.developmentCount = count<0? 0 : count;
    }
    
    private void cleanUpCellsAfterLength(int length) {
        for(Iterator<LinkRatioSmoothing> it = cells.iterator(); it.hasNext();)
            if(it.next().getDevelopment() >= length)
                it.remove();
    }
    
    private void fillCellsTillLength(int length) {
        for(int d=this.developmentCount; d<length; d++)
            cells.add(new LinkRatioSmoothing(d, DefaultLinkRatioSmoothingMethod.ID));
    }
    
    public void setMethodId(int development, String methodId) {
        checkDevelopmentIndex(development);
        getCell(development).setMethodId(methodId);
    }
    
    private void checkDevelopmentIndex(int development) {
        if(development >= 0 || development < developmentCount)
            return;
        String msg = "Development %d is out of bounds [0; %d[";
        msg = String.format(msg, development, developmentCount);
        throw new IndexOutOfBoundsException(msg);
    }
    
    private LinkRatioSmoothing getCell(int development) {
        for(LinkRatioSmoothing cell : cells)
            if(cell.getDevelopment() == development)
                return cell;
        return null;
    }
    
    public String getMethodId(int development) {
        checkDevelopmentIndex(development);
        return getCell(development).getMethodId();
    }
    
    public LinkRatioSmoothingData createSmoothingData() {
    
    }
}
