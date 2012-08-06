package org.jreserve.project.filesystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EntityCookieImpl<E> implements EntityCookie<E> {
    
    private final E entity;

    EntityCookieImpl(E entity) {
        this.entity = entity;
    }
    
    @Override
    public E getEntity() {
        return entity;
    }

}
