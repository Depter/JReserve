package org.jreserve.apcsample;

import java.util.List;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.data.util.ProjectDataTypeFactory;
import org.jreserve.data.util.ProjectDataTypeFactory.DataTypeDummy;
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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
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
    private DataSource ds;
    
    public SampleBuilder(String name) {
        this.name = name;
    }
    
    public ProjectElement<LoB> createLoB() throws Exception {
        ProjectElement<LoB> lobElement = doWork(new LoBFactory(name));
        createBodilyInjuriy(lobElement);
        createMaterialDamage(lobElement);
        return lobElement;
    }
    
    private <T> T doWork(SessionTask.Task<T> work) throws Exception {
        return SessionTask.withCurrentSession(work);
    }
    
    private void createBodilyInjuriy(ProjectElement<LoB> parent) throws Exception {
        ProjectElement<ClaimType> child = doWork(new ClaimTypeFactory(parent.getValue(), BODILY_INJURY));
        parent.addChild(child);
        createDataTypes(child);
        createProject(child);
    }
    
    private void createDataTypes(ProjectElement<ClaimType> claimType) throws Exception {
        List<ProjectElement<ProjectDataType>> dts = doWork(new ProjectDataTypeFactory(claimType.getValue(), PDT_DUMMIES));
        for(ProjectElement<ProjectDataType> element : dts) {
            claimType.addChild(element);
            ds.saveClaimData(InputData.getData(element.getValue()));
        }
    }
    
    private void createProject(ProjectElement<ClaimType> claimType) throws Exception {
        ProjectElement<Project> project = doWork(new ProjectFactory(claimType.getValue(), PROJECT_NAME));
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
        container.addElement(doWork(factory));
    }
    
    private void createVector(ProjectDataContainer container, ProjectDataType dt) throws Exception {
        VectorFactory factory = new VectorFactory(container.getProject(), dt, dt.getName(), InputData.VECTOR_GEOMETRY);
        container.addElement(doWork(factory));
    }
    
    
    private void createMaterialDamage(ProjectElement<LoB> parent) throws Exception {
        ProjectElement<ClaimType> child = doWork(new ClaimTypeFactory(parent.getValue(), MATERIAL_DAMAGE));
        parent.addChild(child);
        createDataTypes(child);
        createProject(child);
    }
}
