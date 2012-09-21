package org.jreserve.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark persistence entities with this annotation. The processor will 
 * automaticly register the entity into the layer file.
 * 
 * <p>The {@link javax.persistence.Entity Entity} annotation must be present
 * otherwise the processor will complain.</p>
 * 
 * <p>The program uses the table strategy to generate id's. The name of the id 
 * table is 'JRESERVE.JRESERVE_IDS' The key in the table is the qualified name 
 * of the class, and the id value must be of type <b>long</b>. The annotation 
 * processor does not check wether the JPA annotations are present or not. 
 * When creating a new database, the id table will be automaticly populated if 
 * needed wit an initial value of 1.</p>
 * 
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface EntityRegistration {
    
    /**
     * The catalog of the id table.
     */
    public final static String CATALOG = "";
    /**
     * The schema of the id table.
     */
    public final static String SCHEMA = "JRESERVE";
    /**
     * The name of the id table.
     */
    public final static String TABLE = "JRESERVE_IDS";
    /**
     * The name of the primary key column in the id table.
     */
    public final static String ID_COLUMN = "CLASS_NAME";
    /**
     * The name of the value column in the id table.
     */
    public final static String VALUE_COLUMN = "NEXT_ID";
    
    public final static int ALLOCATION_SIZE = 1;
    
    public final static int INITIAL_VALUE = 1;
    
    /**
     * If true, a record will be created for the annotated class in the id 
     * table.
     */
    public boolean generateId() default false;
}
