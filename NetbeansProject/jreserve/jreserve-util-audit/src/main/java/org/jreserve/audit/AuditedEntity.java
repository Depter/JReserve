package org.jreserve.audit;

import java.awt.Image;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AuditedEntity<T> {

    private T entity;
    private String name;
    private Image img;
    
    public AuditedEntity(T entity, String name, Image img) {
        this.entity = entity;
        this.name = name;
        this.img = img;
    }
    
    public T getEntity() {
        return entity;
    }
    
    public String getDisplayName() {
        return name;
    }
    
    public Image getImage() {
        return img;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof AuditedEntity)
            return equals((AuditedEntity) o);
        return false;
    }
    
    private boolean equals(AuditedEntity o) {
        if(this == o)
            return true;
        if(entity == null)
            return o.entity == null;
        return entity.equals(o.entity);
    }
    
    @Override
    public int hashCode() {
        if(entity == null)
            return 0;
        return entity.hashCode();
    }
    
    @Override
    public String toString() {
        return ""+entity;
    }
}
