package org.jreserve.triangle.widget.actions;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.Lookup.Result;
import org.openide.util.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractPopUpAction extends AbstractAction implements LookupListener, ContextAwareAction {
    
    private Result<TriangleWidget> wResult;
    
    protected TriangleWidget widget;
    private Lookup lookup;
    
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
        if(wResult != null)
            return;
        wResult = lookup.lookupResult(TriangleWidget.class);
        wResult.addLookupListener(this);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        boolean enable = checkEnabled();
        setEnabled(enable);
    }
    
    protected boolean checkEnabled() {
        widget = lookupOne(wResult);
        return widget != null;
    }
    
    protected <T> T lookupOne(Result<T> result) {
        List<T> all = new ArrayList<T>(result.allInstances());
        if(all.size() == 1)
            return all.get(0);
        return null;
    }

}
