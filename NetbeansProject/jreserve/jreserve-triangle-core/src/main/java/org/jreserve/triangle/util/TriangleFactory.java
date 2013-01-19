package org.jreserve.triangle.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.TriangleModification;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.project.TriangleProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleFactory extends SessionTask.AbstractTask<ProjectElement<Triangle>> {

    private final static Logger logger = Logger.getLogger(TriangleFactory.class.getName());
    
    private final Project project;
    private final ProjectDataType dataType;
    private final String name;
    private final TriangleGeometry geometry;
    private final boolean isTriangle;
    
    private final List<TriangleModification> modifications = new ArrayList<TriangleModification>();
    private final List<TriangleComment> comments = new ArrayList<TriangleComment>();
    
    private String description;
    
    public TriangleFactory(Project project, ProjectDataType dataType, String name, TriangleGeometry geometry, boolean isTriangle) {
        this.project = project;
        this.dataType = dataType;
        this.name = name;
        this.isTriangle = isTriangle;
        this.geometry = geometry.copy();
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addModifications(TriangleModification... modifications) {
        this.modifications.addAll(Arrays.asList(modifications));
    }
    
    public void addComments(TriangleComment... comments) {
        this.comments.addAll(Arrays.asList(comments));
    }

    @Override
    public void doWork(Session session) throws Exception {
        Triangle triangle = createTriangle();
        session.persist(triangle);
        logger.log(Level.INFO, "Triangle created: \"{0}\"", triangle);
        result = new TriangleProjectElement(triangle);
    }
    
    private Triangle createTriangle() {
        Triangle triangle = new Triangle(project, dataType, name, isTriangle);
        triangle.setGeometry(geometry);
        triangle.setDescription(description);
        addModifications(triangle);
        addComments(triangle);
        return triangle;
    }
    
    private void addModifications(Triangle triangle) {
        for(TriangleModification mod : modifications)
            triangle.addModification(mod);
    }
    
    private void addComments(Triangle triangle) {
        for(TriangleComment comment : comments)
            triangle.addComment(comment);
    }
}
