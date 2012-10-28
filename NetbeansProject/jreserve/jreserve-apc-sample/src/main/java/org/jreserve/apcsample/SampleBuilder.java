package org.jreserve.apcsample;

import java.util.List;
import javax.swing.SwingUtilities;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.data.util.ProjectDataTypeFactory;
import org.jreserve.data.util.ProjectDataTypeFactory.DataTypeDummy;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.factories.ClaimTypeFactory;
import org.jreserve.project.factories.LoBFactory;
import org.jreserve.project.factories.ProjectFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.factories.TriangleFactory;
import org.jreserve.triangle.factories.VectorFactory;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "MSG.SampleBuilder.Create.LoB=Creating LoB \"{0}\"...",
    "# {0} - name",
    "MSG.SampleBuilder.Create.ClaimType=Creating claim type \"{0}\"...",
    "# {0} - claim type",
    "MSG.SampleBuilder.Create.DataType=Creating data types for claim type \"{0}\"..."
})
public class SampleBuilder {
    
    final static String BODILY_INJURY = "BI";
    final static String MATERIAL_DAMAGE = "MD";
    
    final static int INCURRED = 100;
    final static int PAID = 200;
    final static int NUMBER = 300;
    final static int BURNING_COST = 400;
    
    private final static DataTypeDummy[] PDT_DUMMIES = {
        new DataTypeDummy(INCURRED, "Incurred", true),
        new DataTypeDummy(PAID, "Paid", true),
        new DataTypeDummy(NUMBER, "Claim count", true),
        new DataTypeDummy(BURNING_COST, "Burning cost", false)
    };
    
    private final static String PROJECT_NAME = "Base";
    
    private final String name;
    private final ApcSampleVisualPanel panel;
    private DataSource ds;
    
    public SampleBuilder(String name, ApcSampleVisualPanel panel) {
        this.name = name;
        this.panel = panel;
    }
    
    public ProjectElement<LoB> createLoB() throws Exception {
        ds = new DataSource(SessionFactory.getCurrentSession());
        ProjectElement<LoB> lobElement = doWork(new LoBFactory(name), Bundle.MSG_SampleBuilder_Create_LoB(name));
        createBodilyInjuriy(lobElement);
        createMaterialDamage(lobElement);
        return lobElement;
    }
    
    private <T> T doWork(SessionTask.Task<T> work, String msg) throws Exception {
        setProgressMsg(msg);
        return SessionTask.withCurrentSession(work);
    }
    
    private void setProgressMsg(final String msg) {
        if(msg == null)
            return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.setProgressMsg(msg);
            }
        });
    }
    
    private void createBodilyInjuriy(ProjectElement<LoB> parent) throws Exception {
        String msg = Bundle.MSG_SampleBuilder_Create_ClaimType(BODILY_INJURY);
        ProjectElement<ClaimType> child = doWork(new ClaimTypeFactory(parent.getValue(), BODILY_INJURY), msg);
        parent.addChild(child);
        createDataTypes(child);
        createProject(child);
    }
    
    private void createDataTypes(ProjectElement<ClaimType> claimType) throws Exception {
        String msg = Bundle.MSG_SampleBuilder_Create_DataType(claimType.getValue().getName());
        List<ProjectElement<ProjectDataType>> dts = doWork(new ProjectDataTypeFactory(claimType.getValue(), PDT_DUMMIES), msg);
        for(ProjectElement<ProjectDataType> element : dts) {
            claimType.addChild(element);
            ds.saveClaimData(InputData.getData(element.getValue()));
        }
    }
    
    private void createProject(ProjectElement<ClaimType> claimType) throws Exception {
        ProjectElement<Project> project = doWork(new ProjectFactory(claimType.getValue(), PROJECT_NAME), null);
        claimType.addChild(project);
        createDatas(project);
    }
    
    private void createDatas(ProjectElement<Project> project) throws Exception {
        ProjectElement<ProjectDataContainer> child = ProjectDataContainer.createContainer(project.getValue());
        project.addChild(child);
        ProjectDataContainer container = child.getValue();
        for(ProjectDataType dt : (List<ProjectDataType>) project.getParent().getChildValues(ProjectDataType.class))
            createDataFor(container, dt);
    }
    
    private void createDataFor(ProjectDataContainer container, ProjectDataType dt) throws Exception {
        if(dt.isTriangle())
            createTriangle(container, dt);
        else
            createVector(container, dt);
    }
    
    private void createTriangle(ProjectDataContainer container, ProjectDataType dt) throws Exception {
        TriangleFactory factory = new TriangleFactory(container.getProject(), dt, dt.getName(), InputData.TRIANGLE_GEOMETRY);
        container.addElement(doWork(factory, null));
    }
    
    private void createVector(ProjectDataContainer container, ProjectDataType dt) throws Exception {
        VectorFactory factory = new VectorFactory(container.getProject(), dt, dt.getName(), InputData.VECTOR_GEOMETRY);
        container.addElement(doWork(factory, null));
    }
    
    
    private void createMaterialDamage(ProjectElement<LoB> parent) throws Exception {
        String msg = Bundle.MSG_SampleBuilder_Create_ClaimType(MATERIAL_DAMAGE);
        ProjectElement<ClaimType> child = doWork(new ClaimTypeFactory(parent.getValue(), MATERIAL_DAMAGE), msg);
        parent.addChild(child);
        createDataTypes(child);
        createProject(child);
    }
}
