package org.jreserve.triangle.widget.model;

import java.awt.Image;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.TriangleModel;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DevelopmentPeriodTriangleModel.ToolTip=Development periods"
})
@ServiceProvider(service=TriangleModel.class, position=100)
public class DevelopmentPeriodTriangleModel extends AbstractTriangleModel {

    private final static Image IMG = ImageUtilities.loadImage("resources/development_periods.png", false);
    private final static String TOOL_TIP = Bundle.LBL_DevelopmentPeriodTriangleModel_ToolTip();
    
    private DateRenderer renderer = new DateRenderer();
    private Map<Integer, String> dateCache = new HashMap<Integer, String>();
    
    public DevelopmentPeriodTriangleModel() {
        super(IMG, TOOL_TIP);
    }

    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        super.setTriangleGeometry(geometry);
        dateCache.clear();
    }

    @Override
    public String getRowName(int row) {
        Integer index = row;
        String date = dateCache.get(index);
        return date!=null? date : cacheDate(index);
    }
    
    private String cacheDate(Integer index) {
        Date date = GeometryUtil.getEDTInstance().getAccidentBegin(geometry, index.intValue());
        String str = renderer.toString(date);
        dateCache.put(index, str);
        return str;
    }

    @Override
    public TriangleModel createInstance() {
        return new DevelopmentPeriodTriangleModel();
    }
}
