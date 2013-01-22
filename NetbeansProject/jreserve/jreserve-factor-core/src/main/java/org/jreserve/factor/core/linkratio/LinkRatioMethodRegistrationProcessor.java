package org.jreserve.factor.core.linkratio;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.factor.core.LinkRatioMethod;
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
@SupportedAnnotationTypes("org.jreserve.factor.core.LinkRatioMethod.Registration")
public class LinkRatioMethodRegistrationProcessor extends AbstractRegistrationProcessor  {
    final static String DIRECTORY = "JReserve/Factors/LinkRatioMethods";
    final static String POSITION = "position";
    final static String DISPLAY_NAME = "displayName";
    private final static String LOCATION = DIRECTORY + "/%s/%s.instance";
    
    private final static String ERR_NEGATIVE_POSITION = 
        "Class '%s' annotated with '%s', and has a negative position %d!";
    private final static String ERR_DISPLAY_NAME = 
        "Class '%s' annotated with '%s', and has an invalid display name '%s'!";
    
    private final static Class<?> INTERFACE = LinkRatioMethod.class;
    private final static Class<LinkRatioMethod.Registration> ANNOTATION = LinkRatioMethod.Registration.class;


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
