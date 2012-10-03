package org.jreserve.data.projectdatatype;

import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(10)
public class ProjectDataTypeLoader extends AbstractProjectElementFactory<ProjectDataType>{

    private final static String SQL = 
        "select t from ProjectDataType t " +
        "where t.project.id = :projectId " +
        "order by t.dbId";
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    protected List<ProjectDataType> getChildValues(Object value, Session session) {
        Query query = session.createQuery(SQL);
        query.setParameter("projectId", ((Project) value).getId());
        return query.getResultList();
    }

    @Override
    protected ProjectElement createProjectElement(ProjectDataType dataType) {
        return new ProjectDatTypeProjectElement(dataType);
    }
}
