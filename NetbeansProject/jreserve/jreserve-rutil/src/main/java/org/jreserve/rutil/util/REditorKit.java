package org.jreserve.rutil.util;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class REditorKit extends StyledEditorKit {

    private ViewFactory viewFactory;
    
    public REditorKit() {
        this.viewFactory = new RViewFactory();
    }
    
    @Override
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    
    @Override
    public String getContentType() {
        return "text/r-code";
    }
}
