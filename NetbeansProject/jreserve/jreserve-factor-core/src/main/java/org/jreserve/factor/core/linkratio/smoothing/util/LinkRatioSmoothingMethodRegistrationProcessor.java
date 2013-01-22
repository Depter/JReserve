package org.jreserve.factor.core.linkratio.smoothing.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingMethod;
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
@SupportedAnnotationTypes("org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingMethod.Registration")
public class LinkRatioSmoothingMethodRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Factors/LinkRatioSmoothing";
    final static String POSITION = "position";
    final static String ID = "id";
    final static String DISPLAY_NAME = "displayName";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";
    
    private final static String ERR_NEGATIVE_POSITION = 
        "Class '%s' annotated with '%s', and has a negative position %d!";
    private final static String ERR_INVALID_ID = 
        "Class '%s' annotated with '%s', and has an invalid id %s!";
    private final static String ERR_DISPLAY_NAME = 
        "Class '%s' annotated with '%s', and has an invalid display name '%s'!";
    
    private final static Class<?> INTERFACE = LinkRatioSmoothingMethod.class;
    private final static Class<LinkRatioSmoothingMethod.Registration> ANNOTATION = LinkRatioSmoothingMethod.Registration.class;

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
        initFile(file, element);
        file.write();
    }

    @Override
    protected String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
    
    private void initFile(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        file.intvalue(POSITION, getPosition(element));
        file.stringvalue(ID, getId(element));
        file.bundlevalue(DISPLAY_NAME, getDisplayName(element));
    }
    
    private int getPosition(TypeElement element) throws LayerGenerationException {
        int priority = element.getAnnotation(ANNOTATION).position();
        checkPosition(element, priority);
        return priority;
    }
    
    private void checkPosition(TypeElement element, int priority) throws LayerGenerationException {
        if(priority < 0) {
            String msg = String.format(ERR_NEGATIVE_POSITION, getClassName(element), ANNOTATION.getName(), priority);
            throw new LayerGenerationException(msg);
        }
    }
    
    private String getId(TypeElement element) throws LayerGenerationException {
        String id = element.getAnnotation(ANNOTATION).id();
        checkId(element, id);
        return id;
    }
    
    private void checkId(TypeElement element, String id) throws LayerGenerationException {
        if(id == null || id.length()==0) {
            String msg = String.format(ERR_INVALID_ID, getClassName(element), ANNOTATION.getName(), id);
            throw new LayerGenerationException(msg);
        }
    }

    private String getDisplayName(TypeElement element) throws LayerGenerationException {
        String name = element.getAnnotation(ANNOTATION).displayName();
        checkDisplayName(element, name);
        return name;
    }
    
    private void checkDisplayName(TypeElement element, String displayName) throws LayerGenerationException {
        if(displayName==null | displayName.trim().length()==0) {
            String msg = String.format(ERR_DISPLAY_NAME, getClassName(element), ANNOTATION.getName(), displayName);
            throw new LayerGenerationException(msg);
        }
    }
}
