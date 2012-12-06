package org.jreserve.rutil.visual;

import java.awt.Image;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.rutil.RFunction;
import org.jreserve.rutil.util.FunctionRegistry;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RFunctionChildren extends Children.Keys<RFunction> {

    private final static Image IMG = ImageUtilities.loadImage("resources/function.png", false);
    
    private final static Comparator<RFunction> COMPARATOR = new Comparator<RFunction>() {
        @Override
        public int compare(RFunction o1, RFunction o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareTo(n2);
        }
    };
    
    @Override
    protected void addNotify() {
        List<RFunction> functions = FunctionRegistry.getFunctions();
        Collections.sort(functions, COMPARATOR);
        setKeys(functions);
        super.addNotify();
    }
    
    @Override
    protected Node[] createNodes(RFunction t) {
        return new Node[]{new FunctionNode(t)};
    }

    private static class FunctionNode extends AbstractNode {
        
        private FunctionNode(RFunction function) {
            super(Children.LEAF, Lookups.fixed(function));
            super.setDisplayName(function.getName());
        }

        @Override
        public Image getIcon(int type) {
            return IMG;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }
}
