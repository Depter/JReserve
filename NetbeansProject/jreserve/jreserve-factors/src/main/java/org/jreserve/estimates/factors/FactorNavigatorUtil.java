package org.jreserve.estimates.factors;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.estimates.factors.visual.FactorsPanel;
import org.jreserve.navigator.NavigableComponent;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorNavigatorUtil {

    public static List<NavigableComponent> createComponents(Triangle triangle) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new FactorsPanel(triangle));
        //TODO add accidents chart
        //TODO add development chart
        return components;
    }
}
