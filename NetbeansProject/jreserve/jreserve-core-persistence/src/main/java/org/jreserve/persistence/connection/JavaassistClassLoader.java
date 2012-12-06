package org.jreserve.persistence.connection;

import javassist.util.proxy.ProxyFactory;
import org.openide.util.Lookup;

/**
 * Utility class for hibernate. Hibernate uses Javasist, which 
 * wants to use the same ClassLoader, which were used to load the 
 * entity classes. The entities are loaded with 
 * {@link Lookup#lookup(java.lang.Class) Lookup.lookup(ClasSLoader.class)}, 
 * so this utility sets this classloader for Javassist.
 * 
 * 
 * @author Peter Decsi
 * @version 1.0
 */
class JavaassistClassLoader {
    
    private static boolean initialized = false;
    
    static void initialize() {
        if(initialized)
            return;
        initialized = true;
        setJavaAssistClassLoader();
    }
    
    private static void setJavaAssistClassLoader() {
        final ClassLoader loader = Lookup.getDefault().lookup(ClassLoader.class);
        ProxyFactory.classLoaderProvider = new ProxyFactory.ClassLoaderProvider() {
            @Override
            public ClassLoader get(ProxyFactory pf) {
                return loader;
            }
        };
    }
}
