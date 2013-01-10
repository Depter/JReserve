package org.jreserve.triangle.smoothing.arithmetic;

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
 */
@Messages({
    "LBL.RArithmeticSmoothing.Description=Smoothes the values, by replacing the values with the arithmetic average.",
    "LBL.RArithmeticSmoothing.Triangle.Description=The triangle to smooth.",
    "LBL.RArithmeticSmoothing.Rows.Description=The row indices of the cells to use for the smoothing.",
    "LBL.RArithmeticSmoothing.Columns.Description=The column indices of the cells to use for the smoothing. Must be the same length as rows.",
    "LBL.RArithmeticSmoothing.Smooth.Description=Booleans indicating whether the given cell should be smoothed. Must be the same length as rows.",
    "LBL.RArithmeticSmoothing.Return.Description=The smoothed triangle."
})
@ServiceProvider(service=RFunction.class)
public class RArithmeticSmoothing implements RFunction {

    private final static Map<String, String> PARAMS = new LinkedHashMap<String, String>(4);
    static {
        PARAMS.put("triangle", Bundle.LBL_RArithmeticSmoothing_Triangle_Description());
        PARAMS.put("rows", Bundle.LBL_RArithmeticSmoothing_Rows_Description());
        PARAMS.put("columns", Bundle.LBL_RArithmeticSmoothing_Columns_Description());
        PARAMS.put("smooth", Bundle.LBL_RArithmeticSmoothing_Smooth_Description());
    }
    
    private final static String DECLARATION = 
        "function(triangle, rows, columns, smooth) {\n"+
        "  length = length(rows);\n"+
        "  average = 1;\n"+
        "  for(i in 1:length) average = average + triangle[rows[i], columns[i]];\n"+
        "  average = average /length;\n"+
        "  \n"+
        "  for(i in 1:length)\n"+
        "    if(smooth[i])\n"+
        "       triangle[rows[i], columns[i]] = average;\n"+
        "  \n"+
        "  triangle;\n"+
        "}";
    
    private final static String NAME = "smoothing.arithmetic";
    
    public static String getSmoothing(String triangle, String x, String y, String used) {
        String source = "%s(%s, %s, %s, %s)";
        return String.format(source, NAME, triangle, x, y, used);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_RArithmeticSmoothing_Description();
    }

    @Override
    public Map<String, String> getParameters() {
        return new LinkedHashMap<String, String>(PARAMS);
    }

    @Override
    public String getReturn() {
        return Bundle.LBL_RArithmeticSmoothing_Return_Description();
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
