package org.jreserve.triangle.widget.model2;

import java.awt.Image;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.TriangleModel;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=TriangleModel.class, position=100)
public class DevelopmentPeriodTriangleModel extends AbstractTriangleModel {

    private final static Image IMG = ImageUtilities.loadImage("resources/triangle.png", false);
    private final static String TOOL_TIP = "Development periods";
    
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
}
