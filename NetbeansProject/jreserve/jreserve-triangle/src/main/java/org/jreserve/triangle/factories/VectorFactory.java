package org.jreserve.triangle.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.VectorProjectElement;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorFactory  extends SessionTask<ProjectElement<Vector>> {

    private final static Logger logger = Logger.getLogger(VectorFactory.class.getName());
    
    private final Project project;
    private final ProjectDataType dataType;
    private final String name;
    private final VectorGeometry geometry;
    
    private String description;
    
    public VectorFactory(Project project, ProjectDataType dataType, String name, VectorGeometry geometry, boolean openSession) {
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
    protected ProjectElement<Vector> doTask() throws Exception {
        Vector vector = createVector();
        session.persist(vector);
        logger.log(Level.INFO, "Vector created: \"{0}\"", vector);
        return new VectorProjectElement(vector);
    }
    
    private Vector createVector() {
        Vector vector = new Vector(project, dataType, name);
        vector.setGeometry(geometry);
        vector.setDescription(description);
        return vector;
    }
}
