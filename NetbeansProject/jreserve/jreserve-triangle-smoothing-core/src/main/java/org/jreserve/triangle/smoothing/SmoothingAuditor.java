package org.jreserve.triangle.smoothing;

import org.jreserve.triangle.util.TriangleModificationAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "MSG.SmoothingAuditor.Added=Added: \"{0}\".",
    "# {0} - name",
    "MSG.SmoothingAuditor.Deleted=Deleted \"{0}\"."
})
public abstract class SmoothingAuditor<E extends Smoothing> extends TriangleModificationAuditor<E> {
    
    public SmoothingAuditor(Class<E> clazz, String typeName) {
        super(clazz, typeName);
    }

    @Override
    protected String getAddChange(E current) {
        String name = current.getName();
        return Bundle.MSG_SmoothingAuditor_Added(name);
    }

    @Override
    protected String getDeleteChange(E current) {
        String name = current.getName();
        return Bundle.MSG_SmoothingAuditor_Deleted(name);
    }
}
