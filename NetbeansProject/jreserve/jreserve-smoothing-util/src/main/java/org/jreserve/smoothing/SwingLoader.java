package org.jreserve.smoothing;

import org.jreserve.smoothing.core.Smoothing;
import java.util.List;
import javax.swing.SwingWorker;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class SwingLoader extends SwingWorker<List<Smoothing>, Void> {
    
    private PersistentObject owner;
    
    public SwingLoader(PersistentObject owner) {
        this.owner = owner;
    }
    
    @Override
    protected List<Smoothing> doInBackground() throws Exception {
        return SmoothingUtil.getSmoothings(owner);
    }
}
