package org.jreserve.factor.core.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jreserve.rutil.RFunction;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RFactorsFunction.Description=Calculates the simple link ratios for a triangle.",
    "LBL.RFactorsFunction.Triangle.Description=The triangle.",
    "LBL.RFactorsFunction.Return.Description=The simple link ratios."
})
public class RFactorsFunction implements RFunction {

    private final static Map<String, String> PARAMS = new LinkedHashMap<String, String>(4);
    static {
        PARAMS.put("triangle", Bundle.LBL_RFactorsFunction_Triangle_Description());
    }

    public static String factors(String triangle) {
        String source = "%1$s.factors = %2$s(%1$s)";
        return String.format(source, triangle, NAME);
    }
    
    private final static String DECLARATION = 
        "function(triangle) {\n"+
        "  accidents = nrow(triangle);\n"+
        "  developments = ncol(t) - 1;\n"+
        "  t = NULL;\n"+
        "\n"+
        "  if(accidents > 0 && developments > 0) {\n"+
        "    t = triangle[,1:developments];\n"+
        "    for(a in 1:accidents) {\n"+
        "      length = length(triangle[a,]);\n"+
        "      t[a,] = triangle[a, 2:length] / triangle[a, 1:(length-1)];\n"+
        "    }\n"+
        "\n"+
        "    while(sum(!is.na(t[accidents,])) == 0) {\n"+
        "      accidents = accidents - 1;\n"+
        "      t = t[1:accidents,];\n"+
        "    }\n"+
        "\n"+
        "    while(sum(!is.na(t[,developments])) == 0) {\n"+
        "      developments = developments - 1;\n"+
        "      t = t[,1:developments];\n"+
        "    }\n"+
        "  }\n"+
        "  t;\n"+
        "}";
    
    final static String NAME = "factors";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_RFactorsFunction_Description();
    }

    @Override
    public Map<String, String> getParameters() {
        return new LinkedHashMap<String, String>(PARAMS);
    }

    @Override
    public String getReturn() {
        return Bundle.LBL_RFactorsFunction_Return_Description();
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
