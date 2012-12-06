package org.jreserve.rutil.util;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.rutil.RFunction;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionRegistry {

    private static List<RFunction> functions = null;
    
    public static RFunction getFunction(String name) {
        initFunctions();
        for(RFunction function : functions)
            if(function.getName().equals(name))
                return function;
        return null;
    }
    
    private static void initFunctions() {
        if(functions == null)
            functions = new ArrayList<RFunction>(Lookup.getDefault().lookupAll(RFunction.class));
    }
    
    public static List<RFunction> getFunctions() {
        initFunctions();
        return new ArrayList<RFunction>(functions);
    }
}
