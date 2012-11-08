package org.jreserve.navigator;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class NavigableComponentFactory {

    public static TopComponent createTopComponent(MultiViewDescription[] desc) {
        List<NavigableComponent> components = createComponents(desc);
        return new NavigableTopComponent(components);
    }
    
    private static List<NavigableComponent> createComponents(MultiViewDescription[] desc) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>(desc.length);
        for(MultiViewDescription description : desc)
            components.add(createComponent(description));
        return components;
    }

    private static NavigableComponent createComponent(MultiViewDescription description) {
        NavigablePanel panel = new NavigablePanel(description.getDisplayName(), description.getIcon());
        panel.setContent(description.createElement().getVisualRepresentation());
        return panel;
    }
}
