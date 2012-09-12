package org.jreserve.project.system.management;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MultiPersistentSavable extends PersistentSavable {
    
    public static enum Operation {
        DELETE {
            @Override
            protected void execute(Entity entity, Session session) {
                session.delete(entity.entity);
            }
        },
        SAVE {
            @Override
            protected void execute(Entity entity, Session session) {
                session.persist(entity.entity);
            }
        },
        UPDATE {
            @Override
            protected void execute(Entity entity, Session session) {
                session.update(entity.entity);
            }
        };
        
        protected abstract void execute(Entity entity, Session session);
    };
   
    private List<Entity> entities = new ArrayList<Entity>();
    
    public MultiPersistentSavable(ProjectElement element) {
        super(element);
    }
    
    public void addEntity(Object entity, Operation operation) {
        entities.add(new Entity(entity, operation));
    }
    
    @Override
    protected void saveEntity() {
        for(Entity e : new ArrayList<Entity>(entities)) {
            e.operation.execute(e, session);
            entities.remove(0);
        }
    }
    
    private class Entity {
        private final Object entity;
        private final Operation operation;
        
        private Entity(Object entity, Operation operation) {
            this.entity = entity;
            this.operation = operation;
        }
    }
}
