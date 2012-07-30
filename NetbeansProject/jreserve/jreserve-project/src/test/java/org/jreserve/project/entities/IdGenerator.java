package org.jreserve.project.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name="JRESERVE_IDS", schema="JRESERVE")
public class IdGenerator implements Serializable {
    
    @Id
    @Column(name="ID_NAME", length=100)
    private String id;
    
    @Column(name="ID_VALUE", nullable=false)
    private long nextId = 1;
    
    protected IdGenerator() {
    }
    
    private IdGenerator(Class<?> clazz) {
        this.id = clazz.getName();
    }
    
    public static void generateIds(SessionFactory factory, Class<?>... entities) {
        Session session = factory.getCurrentSession();
        
        session.beginTransaction();
        for(Class<?> entity : entities)
            session.save(new IdGenerator(entity));
        session.getTransaction().commit();
    }
}
