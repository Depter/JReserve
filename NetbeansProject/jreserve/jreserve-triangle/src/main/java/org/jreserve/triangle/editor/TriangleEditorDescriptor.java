package org.jreserve.triangle.editor;

import java.awt.Image;
import java.io.Serializable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.VectorProjectElement;
import org.jreserve.triangle.entities.AbstractData;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.TriangleEditorDescriptor.Title=Triangle",
    "LBL.VectorEditorDescriptor.Title=Vector"
})
class TriangleEditorDescriptor implements MultiViewDescription, Serializable {
    
    private final static String TRIANGLE_PREFERRED_ID = "TriangleEditorDescriptor";
    private final static String VECTOR_PREFERRED_ID = "VectorEditorDescriptor";
    
    public static TriangleEditorDescriptor getTriangle(TriangleProjectElement element) {
        TriangleEditorDescriptor descriptor = new TriangleEditorDescriptor(element);
        descriptor.prefferedId = TRIANGLE_PREFERRED_ID;
        descriptor.img = TriangleDataEditorDescriptor.IMG;
        descriptor.name = Bundle.LBL_TriangleEditorDescriptor_Title();
        return descriptor;
    }
    
    public static TriangleEditorDescriptor getVector(VectorProjectElement element) {
        TriangleEditorDescriptor descriptor = new TriangleEditorDescriptor(element);
        descriptor.prefferedId = VECTOR_PREFERRED_ID;
        descriptor.img = VectorDataEditorDescriptor.IMG;
        descriptor.name = Bundle.LBL_VectorEditorDescriptor_Title();
        return descriptor;
    }

    private ProjectElement<? extends AbstractData> element;
    private String name;
    private Image img;
    private String prefferedId;
    
    TriangleEditorDescriptor(ProjectElement<? extends AbstractData> element) {
        this.element = element;
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public Image getIcon() {
        return img;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String preferredID() {
        return prefferedId;
    }

    @Override
    public MultiViewElement createElement() {
        return new TriangleEditorView(element);
    }

}
