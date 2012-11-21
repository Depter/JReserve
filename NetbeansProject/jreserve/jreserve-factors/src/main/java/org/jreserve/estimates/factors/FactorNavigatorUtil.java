package org.jreserve.estimates.factors;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.estimates.factors.visual.FactorsPanel;
import org.jreserve.navigator.NavigableComponent;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.widget.charts.AccidentPeriodsChartData;
import org.jreserve.triangle.widget.charts.DevelopmentPeriodsChartData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorNavigatorUtil {

    public static List<NavigableComponent> createComponents(ProjectElement<Triangle> triangle, ProjectElement estimate) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        FactorsPanel factors = new FactorsPanel(triangle, estimate);
        components.add(factors);
        components.add(AccidentPeriodsChartData.createPanel(factors.getTriangleWidget()));
        components.add(DevelopmentPeriodsChartData.createPanel(factors.getTriangleWidget()));
        return components;
    }
}
