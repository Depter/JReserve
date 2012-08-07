package org.jreserve.project.system;

import java.io.IOException;
import org.openide.cookies.InstanceCookie;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntityCookie<T> implements InstanceCookie {
    
    private final Class<? super T> clazz;
    private final T instance;
    
    public EntityCookie(T instance) {
        this((Class<? super T>) instance.getClass(), instance);
    }
    
    public EntityCookie(Class<? super T> clazz, T instance) {
        this.clazz = clazz;
        this.instance = instance;
    }
    
    @Override
    public String instanceName() {
        return clazz.getName();
    }

    @Override
    public Class<?> instanceClass() throws IOException, ClassNotFoundException {
        return clazz;
    }

    @Override
    public Object instanceCreate() throws IOException, ClassNotFoundException {
        return instance;
    }
}
