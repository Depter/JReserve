package org.jreserve.triangle.visual.widget.action;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.openide.util.Lookup.Result;
import org.openide.util.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractPopUpAction extends AbstractAction implements LookupListener, ContextAwareAction {
    
    protected Lookup lookup;
    private Result<ModifiableTriangle> triangleResult;
    private Result<TriangularData> dataResult;
    
    protected ModifiableTriangle triangle;
    protected TriangularData data;
    
    protected AbstractPopUpAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    protected AbstractPopUpAction(Lookup lookup) {
        this.lookup = lookup;
    }
    
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
    
    protected void init() {
        init(lookup);
        resultChanged(null);
    }
    
    protected void init(Lookup lookup) {
        if(triangleResult != null)
            return;
        triangleResult = lookup.lookupResult(ModifiableTriangle.class);
        triangleResult.addLookupListener(this);
        dataResult = lookup.lookupResult(TriangularData.class);
        dataResult.addLookupListener(this);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        boolean enable = checkEnabled();
        setEnabled(enable);
    }
    
    protected boolean checkEnabled() {
        triangle = lookupOne(triangleResult);
        data = lookupOne(dataResult);
        return triangle != null && data != null;
    }
    
    protected <T> T lookupOne(Result<T> result) {
        List<T> all = new ArrayList<T>(result.allInstances());
        if(all.size() == 1)
            return all.get(0);
        return null;
    }
    
    protected <T> List<T> lookupAll(Result<T> result) {
        return new ArrayList<T>(result.allInstances());
    }
    
    protected <T> List<T> lookupAll(Class<T> clazz) {
        return new ArrayList<T>(lookup.lookupAll(clazz));
    }
    
    protected <T> T lookupOne(Class<T> clazz) {
        List<T> all = lookupAll(clazz);
        if(all.size() == 1)
            return all.get(0);
        return null;
    }
}
