package org.jreserve.smoothing.exponential;

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
    "LBL.RExponentialSmoothing.Description=Smoothes the values, by <ul><li>s(1) = t(1)</li><li>s(n) = alpha * s(n-1) + (1-alpha) * t(n), where n &gt; 1</li></ul>.",
    "LBL.RExponentialSmoothing.Triangle.Description=The triangle to smooth.",
    "LBL.RExponentialSmoothing.Rows.Description=The row indices of the cells to use for the smoothing.",
    "LBL.RExponentialSmoothing.Columns.Description=The column indices of the cells to use for the smoothing. Must be the same length as rows.",
    "LBL.RExponentialSmoothing.Smooth.Description=Booleans indicating whether the given cell should be smoothed. Must be the same length as rows.",
    "LBL.RExponentialSmoothing.Alpha.Description=The alpha to use for the smoothing.",
    "LBL.RExponentialSmoothing.Return.Description=The smoothed triangle."
})
@ServiceProvider(service=RFunction.class)
public class RExponentialSmoothing implements RFunction {

    private final static Map<String, String> PARAMS = new LinkedHashMap<String, String>(5);
    static {
        PARAMS.put("triangle", Bundle.LBL_RExponentialSmoothing_Triangle_Description());
        PARAMS.put("rows", Bundle.LBL_RExponentialSmoothing_Rows_Description());
        PARAMS.put("columns", Bundle.LBL_RExponentialSmoothing_Columns_Description());
        PARAMS.put("smooth", Bundle.LBL_RExponentialSmoothing_Smooth_Description());
        PARAMS.put("alpha", Bundle.LBL_RExponentialSmoothing_Alpha_Description());
    }
    private final static String SOURCE = 
        "smoothing.exponential = function(triangle, rows, columns, smooth, alpha) {\n" + 
        "  length = length(rows);\n" + 
        "  smoothed = 1:length;\n" + 
        "  smoothed[1] = triangle[rows[1], columns[1]];\n" + 
        "  for(i in 2:length) smoothed[i] = alpha * smoothed[i-1] + (1 - alpha) * triangle[rows[i], columns[i]];\n" + 
        "\n" + 
        "  for(i in 1:length)\n" + 
        "    if(smooth[i])\n" + 
        "      triangle[rows[i], columns[i]] = smoothed[i];\n" + 
        "\n" + 
        "  triangle;\n" + 
        "}";

    @Override
    public String getName() {
        return "smoothing.exponential";
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_RExponentialSmoothing_Description();
    }

    @Override
    public Map<String, String> getParameters() {
        return new LinkedHashMap<String, String>(PARAMS);
    }

    @Override
    public String getReturn() {
        return Bundle.LBL_RExponentialSmoothing_Return_Description();
    }

    @Override
    public String getSource() {
        return SOURCE;
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
