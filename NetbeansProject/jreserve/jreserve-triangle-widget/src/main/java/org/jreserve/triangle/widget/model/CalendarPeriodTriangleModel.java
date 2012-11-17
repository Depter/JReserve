package org.jreserve.triangle.widget.model;

import java.awt.Image;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.TriangleCell;
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
    "LBL.CalendarPeriodTriangleModel.ToolTip=Calendar years"
})
@ServiceProvider(service=TriangleModel.class, position=200)
public class CalendarPeriodTriangleModel extends AbstractTriangleModel {

    private final static Image IMG = ImageUtilities.loadImage("resources/calendar_periods.png", false);
    private final static String TOOL_TIP = Bundle.LBL_CalendarPeriodTriangleModel_ToolTip();
    
    private DateRenderer renderer = new DateRenderer();
    private Map<Integer, String> accidentCache = new HashMap<Integer, String>();
    private Map<Integer, Date> developmentDateCache = new HashMap<Integer, Date>();
    private Map<Integer, String> developmentCache = new HashMap<Integer, String>();

    public CalendarPeriodTriangleModel() {
        super(IMG, TOOL_TIP);
    }

    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        super.setTriangleGeometry(geometry);
        accidentCache.clear();
        developmentCache.clear();
        developmentDateCache.clear();
    }

    @Override
    public String getColumnName(int column) {
        Integer index = column;
        String title = developmentCache.get(index);
        return title!=null? title : cacheDevelopment(column);
    }

    private String cacheDevelopment(Integer column) {
        Date date = GeometryUtil.getEDTInstance().getDevelopmentBegin(geometry, 0, column);
        developmentDateCache.put(column, date);
        String str = renderer.toString(date);
        developmentCache.put(column, str);
        return str;
    }
    
    @Override
    public String getRowName(int row) {
        Integer index = row;
        String title = accidentCache.get(index);
        if(title == null) {
            Date date = GeometryUtil.getEDTInstance().getAccidentBegin(geometry, row);
            title = renderer.toString(date);
            accidentCache.put(index, title);
        }
        return title;
    }

    @Override
    public TriangleCell getCellAt(int row, int column) {
        if(cells == null)
            return null;
        if(row >= rowCount)
            return null;
        Date developmentBegin = getDateForColumn(column);
        return getCellAt(cells[row], developmentBegin);
    }
    
    private Date getDateForColumn(int column) {
        Date date = developmentDateCache.get(column);
        if(date == null) {
            date = GeometryUtil.getEDTInstance().getDevelopmentBegin(geometry, 0, column);
            developmentDateCache.put(column, date);
        }
        return date;
            
    }

    private TriangleCell getCellAt(TriangleCell[] row, Date developmnetBegin) {
        if(row == null) return null;
        for(TriangleCell cell : row)
            if(cell.getDevelopmentBegin().equals(developmnetBegin))
                return cell;
        return null;
    }
}