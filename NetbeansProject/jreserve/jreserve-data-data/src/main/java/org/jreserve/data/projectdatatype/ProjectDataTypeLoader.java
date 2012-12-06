package org.jreserve.data.projectdatatype;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(100)
public class ProjectDataTypeLoader extends AbstractProjectElementFactory<ProjectDataType>{

    private final static String SQL = 
        "select t from ProjectDataType t " +
        "where t.claimType.id = :claimTypeId " +
        "order by t.dbId";
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    protected List<ProjectDataType> getChildValues(Object value) {
        Session session = SessionFactory.getCurrentSession();
        Query query = session.createQuery(SQL);
        query.setParameter("claimTypeId", ((ClaimType) value).getId());
        return query.list();
    }

    @Override
    protected ProjectElement createProjectElement(ProjectDataType dataType) {
        return new ProjectDatTypeProjectElement(dataType);
    }
}
