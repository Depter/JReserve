package org.jreserve.audit.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.audit.Auditor;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.audit.Auditor.Registration")
public class AuditorRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Audit/AuditFactories";
    final static String PRIORITY = "priority";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";
    
    private final static String ERR_NEGATIVE_PRORITY = 
        "Class '%s' annotated with '%s', and has a negative priority %d!";
    
    private final static Class<Auditor.Registration> ANNOTATION = Auditor.Registration.class;
    private final static Class<?> INTERFACE = Auditor.class;

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return ANNOTATION;
    }

    @Override
    protected Class<?> getInterface() {
        return INTERFACE;
    }
    
    @Override
    protected void registerElement(TypeElement element) throws LayerGenerationException {
        LayerBuilder.File file = getFile(element);
        int priority = getPriority(element);
        file.intvalue(PRIORITY, priority);
        file.write();
    }
    
    @Override
    protected String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
    
    private int getPriority(TypeElement element) throws LayerGenerationException {
        int priority = element.getAnnotation(ANNOTATION).value();
        checkPriority(element, priority);
        return priority;
    }
    
    private void checkPriority(TypeElement element, int priority) throws LayerGenerationException {
        if(priority < 0) {
            String msg = String.format(ERR_NEGATIVE_PRORITY, getClassName(element), ANNOTATION.getName(), priority);
            throw new LayerGenerationException(msg);
        }
    }
}
