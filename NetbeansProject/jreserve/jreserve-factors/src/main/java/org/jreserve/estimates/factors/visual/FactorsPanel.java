package org.jreserve.estimates.factors.visual;

import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 */
public class FactorsPanel extends NavigablePanel {

    private Triangle triangle;
    private TriangleWidget widget;
    
    public FactorsPanel(Triangle triangle) {
        this.triangle = triangle;
        initComponents();
    }
    
    private void initComponents() {
        widget = new TriangleWidget();
        widget.setTriangleGeometry(createGeoemtry());
        
        setContent(widget);
    }
    
    private TriangleGeometry createGeoemtry() {
        TriangleGeometry tg = triangle.getGeometry();
        return new TriangleGeometry(tg.getStartDate(), 
                tg.getAccidentPeriods(), tg.getAccidentMonths(), 
                tg.getDevelopmentPeriods(), tg.getMonthInDevelopment());
    }
}
