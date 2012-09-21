package org.jreserve.data.projectdatatype;

import java.util.Comparator;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.management.AbstractElementCache;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataTypeUtil extends AbstractElementCache<Project, ProjectDataType> {
    
    private static ProjectDataTypeUtil INSTANCE = null;
    
    public static ProjectDataTypeUtil getDefault() {
        if(INSTANCE == null)
            INSTANCE = new ProjectDataTypeUtil();
        return INSTANCE;
    }
    
    private final static String ERR_PROJECT_NOT_PERSISTED = 
        "Project '%s' must be peristed first, before log can be added to it!";
    private final static String QUERY = 
        "SELECT dt FROM ProjectDataType dt WHERE dt.project.id = :projectId";

    private final static Comparator<ProjectDataType> COMPARATOR = new Comparator<ProjectDataType>() {
        @Override
        public int compare(ProjectDataType o1, ProjectDataType o2) {
            int dif = compareNull(o1, o2);
            if(dif != 0) return dif;
            
            dif = compareProject(o1.getProject(), o2.getProject());
            if(dif != 0) return dif;
            return o1.getDbId() - o2.getDbId();
        }
        
        private int compareNull(Object o1, Object o2) {
            if(o1 == null)
                return o2==null? 0 : 1;
            return o2==null? -1 : 0;
        }
        
        private int compareProject(Project p1, Project p2) {
            int dif = compareNull(p1, p2);
            if(dif != 0) return dif;
            if(p1.getId() < p2.getId())
                return -1;
            else if(p1.getId() > p2.getId())
                return 1;
            return 0;
        }
    };
    
    @Override
    protected String getName() {
        return "ProjectDataTypeUtil";
    }

    @Override
    protected void checkKey(Project project) {
        if(project.getId() > 0)
            return;
        String msg = String.format(ERR_PROJECT_NOT_PERSISTED, project.getName());
        throw new IllegalArgumentException(msg);
    }

    @Override
    protected Query createQuery(Session session, Project project) {
        Query query = session.createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query;
    }

    @Override
    protected Comparator<ProjectDataType> getComparator() {
        return COMPARATOR;
    }

    @Override
    protected long getId(Project project) {
        return project.getId();
    }
    
    @Override
    public synchronized void addValue(Project project, ProjectDataType dt) {
        super.addValue(project, dt);
    }
    
    @Override
    public synchronized void deleteValue(Project project, ProjectDataType dt) {
        super.deleteValue(project, dt);
    }

    @Override
    protected boolean isNew(ProjectDataType entity) {
        return entity.getId() == 0;
    }
}
