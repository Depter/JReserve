package org.jreserve.smoothing.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.jreserve.smoothing.SmoothingMethod;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.smoothing.SmoothingMethod.Registration")
public class SmoothingMethodRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Smoothing";
    final static String ID = "id";
    final static String DISPLAY_NAME = "displayName";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";
    
    private final static Class<SmoothingMethod.Registration> ANNOTATION = SmoothingMethod.Registration.class;
    private final static Class<?> INTERFACE = SmoothingMethod.class;
    
    private final static String ERR_NEGATIVE_ID = 
        "Class '%s' annotated with '%s', and has a negative id %d!";
    private final static String ERR_EMPTY_DISPLAY_NAME = 
        "Class '%s' annotated with '%s', and has an empty display name!";

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
        setId(file, element);
        setDisplayName(file, element);
        file.write();
    }

    @Override
    protected String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }

    private void setId(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        int id = element.getAnnotation(ANNOTATION).id();
        checkId(element, id);
        file.intvalue(ID, id);
    }
    
    private void checkId(TypeElement element, int id) throws LayerGenerationException {
        if(id < 0) {
            String msg = String.format(ERR_NEGATIVE_ID, getClassName(element), ANNOTATION.getName(), id);
            throw new LayerGenerationException(msg);
        }
    }

    private void setDisplayName(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        String displayName = element.getAnnotation(ANNOTATION).displayName();
        checkDisplayName(element, displayName);
        file.bundlevalue(DISPLAY_NAME, displayName);
    }
    
    private void checkDisplayName(TypeElement element, String displayName) throws LayerGenerationException {
        if(displayName == null || displayName.trim().length() == 0) {
            String msg = String.format(ERR_EMPTY_DISPLAY_NAME, getClassName(element), ANNOTATION.getName());
            throw new LayerGenerationException(msg);
        }
    }
}
