package org.jreserve.project;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=Project.class)
@Entity
public class Project implements Serializable {
    private final static long serialVersionUID = 1L;
    
    @Id
    private long id;
}
