package org.jreserve.project.system.management;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.openide.WizardDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ElementCreatorWizard {

    public final static String PROP_ELEMENT_LOOKUP = "project_element_lookup";
    
    public static enum Category {
        PROJECT,
        DATA,
        ESTIMATE,
        BOOTSTRAP,
        OTHER
    };
    
    public String getDescription();
    
    public List<WizardDescriptor.Panel> getPanels();
    
    
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        public Category category();
        public String displayName();
        public String iconBase();
        public int position() default Integer.MAX_VALUE;
    }    
}
