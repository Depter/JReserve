package org.jreserve.data;

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
public interface DataImportWizard {

    public final static String PROJECT_PROPERTY = "SELECTED_PROJECT";
    
    public List<WizardDescriptor.Panel> getPanels();
    
    public String getDescription();
    
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        public String displayName();
        public String iconBase();
        public int position() default Integer.MAX_VALUE;
    }    
}