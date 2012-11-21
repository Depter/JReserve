package org.jreserve.estimates.chainladder.visual;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.estimates.chainladder.ChainLadderEstimateProjectElement;
import org.jreserve.estimates.factors.FactorNavigatorUtil;
import org.jreserve.navigator.NavigableComponent;
import org.jreserve.navigator.NavigableTopComponent;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectTreeQuery;
import org.jreserve.project.system.visual.ProjectElementCloseHandler;
import org.jreserve.project.system.visual.TabNameAdapter;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class Editor extends NavigableTopComponent implements UndoRedo.Provider {

    public static TopComponent createEditor(ChainLadderEstimateProjectElement element) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        ProjectElement triangle = ProjectTreeQuery.findElement(element.getValue().getTriangle());
        components.addAll(FactorNavigatorUtil.createComponents(triangle, element));
        return new Editor(components, element);
    }
    
    private final static Image IMG = ImageUtilities.loadImage("resources/estimate.png", false);
    private final static int SCROLL_INCREMENT = 40;
    
    private ProjectElement element;
    
    private ProjectElementCloseHandler closeHandler;
    
    private Editor(List<NavigableComponent> components, ProjectElement element) {
        super(components);
        this.element = element;
        this.closeHandler = new ProjectElementCloseHandler(element);
        initTopComponent();
    }
    
    private void initTopComponent() {
        initTab();
        TabNameAdapter.createAdapter(this, element);
        scroller.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scroller.getVerticalScrollBar().setBlockIncrement(SCROLL_INCREMENT);
    }
    
    private void initTab() {
        setIcon(IMG);
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name);
    }
    
    private void initTriangleElement(ProjectElement trangleElement) {
        
    }
    
    @Override
    public UndoRedo getUndoRedo() {
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        return ur!=null? ur : UndoRedo.NONE;
    }
    
    @Override
    public boolean canClose() {
        return closeHandler.canClose();
    }

}
