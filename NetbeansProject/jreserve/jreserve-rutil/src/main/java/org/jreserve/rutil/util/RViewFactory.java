package org.jreserve.rutil.util;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RViewFactory implements ViewFactory {

    @Override
    public View create(Element element) {
        return new RView(element);
    }
}
