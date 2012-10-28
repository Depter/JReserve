package org.jreserve.project.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectFactory extends SessionTask.AbstractTask<ProjectElement<Project>> {

    private final static Logger logger = Logger.getLogger(ProjectFactory.class.getName());
    private final String name;
    private final ClaimType ct;
    private String description;
    
    public ProjectFactory(ClaimType claimType, String name) {
        this.name = name;
        this.ct = claimType;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void doWork(Session session) throws Exception {
        Project project = new Project(name);
        if(description != null)
            project.setDescription(description);
        
        ct.addProject(project);
        session.persist(project);
        logger.log(Level.INFO, "Project created: \"{0}\"", name);
        result = new org.jreserve.project.entities.project.ProjectElement(project);
    }
}
