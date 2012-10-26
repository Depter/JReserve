package org.jreserve.triangle.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleFactory extends SessionTask<ProjectElement<Triangle>> {

    private final static Logger logger = Logger.getLogger(TriangleFactory.class.getName());
    
    private final Project project;
    private final ProjectDataType dataType;
    private final String name;
    private final TriangleGeometry geometry;
    
    private String description;
    
    public TriangleFactory(Project project, ProjectDataType dataType, String name,  TriangleGeometry geometry, boolean openSession) {
        super(openSession);
        this.project = project;
        this.dataType = dataType;
        this.name = name;
        this.geometry = geometry.copy();
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    protected ProjectElement<Triangle> doTask() throws Exception {
        Triangle triangle = createTriangle();
        session.persist(triangle);
        logger.log(Level.INFO, "Triangle created: \"{0}\"", triangle);
        return new TriangleProjectElement(triangle);
    }
    
    private Triangle createTriangle() {
        Triangle triangle = new Triangle(project, dataType, name);
        triangle.setGeometry(geometry);
        triangle.setDescription(description);
        return triangle;
    }

}
