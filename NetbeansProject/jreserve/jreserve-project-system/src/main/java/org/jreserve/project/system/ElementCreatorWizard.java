package org.jreserve.project.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.openide.WizardDescriptor;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ElementCreatorWizard {

    public static enum Category {
        PROJECT,
        DATA,
        ESTIMATE,
        BOOTSTRAP
    };
    
    public void setWizardLookup(Lookup lookup);
    
    public List<WizardDescriptor.Panel> getPanels();
    
    
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        public Category category();
        public String displayName();
        public String iconBase();
    }
    
}
