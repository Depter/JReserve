package org.jreserve.estimates.factors.factorestimator.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.estimates.factors.factorestimator.FactorEstimator;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.estimates.factors.factorestimator.FactorEstimator.Registration")
public class FactorEstimatorRegistrationProcessor extends AbstractRegistrationProcessor {
    public final static String DIRECTORY = "JReserve/Estimate/FactorEstimators";
    public final static String POSITION = "position";
    public final static String DISPLAY_NAME = "displayName";
    private final static String LOCATION = DIRECTORY + "/%s.instance";
    
    private final static String ERR_NEGATIVE_POSITION = 
        "Class '%s' annotated with '%s', and has a negative position %d!";
    private final static String ERR_DISPLAY_NAME = 
        "Class '%s' annotated with '%s', and does not have a displayName!";
    
    private final static Class<FactorEstimator.Registration> ANNOTATION = FactorEstimator.Registration.class;
    private final static Class<?> INTERFACE = FactorEstimator.class;

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
        file.intvalue(POSITION, getPosition(element));
        file.bundlevalue(DISPLAY_NAME, getDisplayName(element));
        file.write();
    }
    
    @Override
    protected String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
    
    private int getPosition(TypeElement element) throws LayerGenerationException {
        int priority = element.getAnnotation(ANNOTATION).position();
        checkPriority(element, priority);
        return priority;
    }
    
    private void checkPriority(TypeElement element, int priority) throws LayerGenerationException {
        if(priority < 0) {
            String msg = String.format(ERR_NEGATIVE_POSITION, getClassName(element), ANNOTATION.getName(), priority);
            throw new LayerGenerationException(msg);
        }
    }
    
    private String getDisplayName(TypeElement element) throws LayerGenerationException {
        String name = element.getAnnotation(ANNOTATION).displayName();
        checkDisplayName(element, name);
        return name;
    }
    
    private void checkDisplayName(TypeElement element, String displayName) throws LayerGenerationException {
        if(displayName == null || displayName.trim().length() == 0) {
            String msg = String.format(ERR_DISPLAY_NAME, getClassName(element), ANNOTATION.getName());
            throw new LayerGenerationException(msg);
        }
    }
}
