package org.jreserve.triangle.smoothing.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.jreserve.triangle.smoothing.Smoother;
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
@SupportedAnnotationTypes("org.jreserve.triangle.smoothing.Smoother.Registration")
public class SmootherRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Smoothing";
    final static String POSITION = "position";
    final static String DISPLAY_NAME = "displayName";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";
    
    private final static Class<Smoother.Registration> ANNOTATION = Smoother.Registration.class;
    private final static Class<?> INTERFACE = Smoother.class;
    
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
        setPosition(file, element);
        setDisplayName(file, element);
        file.write();
    }

    @Override
    protected String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }

    private void setPosition(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        int id = element.getAnnotation(ANNOTATION).position();
        file.intvalue(POSITION, id);
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
