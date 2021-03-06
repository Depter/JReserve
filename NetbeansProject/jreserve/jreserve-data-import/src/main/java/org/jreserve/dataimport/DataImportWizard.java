package org.jreserve.dataimport;

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

    public final static String CLAIM_TYPE_PROPERTY = "SELECTED_CLAIM_TYPE";
    public final static String DATA_TABLE_PROPERTY = "DATA_TABLE";
    public final static String IMPORT_METHOD_PROPERTY = "IMPORT_METHOD";
    public final static String CUMMULATED_PROPERTY = "CUMMULATED";
    
    public List<WizardDescriptor.Panel> getPanels();
    
    public String getDescription();
    
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        public String displayName();
        public String iconBase() default "";
        public int position() default Integer.MAX_VALUE;
    }    
}