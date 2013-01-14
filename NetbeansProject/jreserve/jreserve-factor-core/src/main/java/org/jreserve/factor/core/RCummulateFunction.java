package org.jreserve.factor.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jreserve.rutil.RFunction;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RCummulateFunction.Description=Cummulates the given triangle.",
    "LBL.RCummulateFunction.Triangle.Description=The triangle to cummulate.",
    "LBL.RCummulateFunction.Return.Description=The cummulated triangle."
})
@ServiceProvider(service=RFunction.class)
public class RCummulateFunction implements RFunction {

    private final static Map<String, String> PARAMS = new LinkedHashMap<String, String>(4);
    static {
        PARAMS.put("triangle", Bundle.LBL_RCummulateFunction_Triangle_Description());
    }

    public static String cummulate(String triangle) {
        String source = "%1$s = %2$s(%1$s)";
        return String.format(source, triangle, NAME);
    }
    
    private final static String DECLARATION = 
        "function(triangle) {\n"+
        "  accidents = nrow(triangle);\n"+
        "  developments = ncol(t);\n"+
        "  if(accidents > 0 && developments > 0) {\n"+
        "    for(a in 1:accidents) {\n"+
        "      for(d in 2:developments) {\n"+
        "        prev = triangle[a,d-1];\n"+
        "        if(!is.na(prev))\n"+
        "          triangle[a, d] = triangle[a, d] + prev;\n"+
        "      }\n"+
        "    }\n"+
        "  }\n"+
        "  triangle;"+
        "}";
    
    final static String NAME = "cummulate";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_RCummulateFunction_Description();
    }

    @Override
    public Map<String, String> getParameters() {
        return new LinkedHashMap<String, String>(PARAMS);
    }

    @Override
    public String getReturn() {
        return Bundle.LBL_RCummulateFunction_Return_Description();
    }

    @Override
    public String getSource() {
        return String.format("%s <- %s", NAME, DECLARATION);
    }

    @Override
    public Set<String> getFunctionDependendencies() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getLibraryDependendencies() {
        return Collections.EMPTY_SET;
    }
}
