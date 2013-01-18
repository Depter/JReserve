package org.jreserve.triangle.visual.widget.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.jreserve.triangle.visual.widget.WidgetEditor;
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
@SupportedAnnotationTypes("org.jreserve.triangle.visual.widget.WidgetEditor.Registration")
public class WidgetEditorRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String DIRECTORY = "JReserve/Widget/WidgetTableEditor";
    final static String POSITION = "position";
    private final static String LOCATION = DIRECTORY + "/%s/%s.instance";
    
    private final static String ERR_CATEGORY = 
        "Class '%s' annotated with '%s', and has an invalid category '%s'!";
    
    private final static Class<?> INTERFACE = WidgetEditor.class;
    private final static Class<WidgetEditor.Registration> ANNOTATION = WidgetEditor.Registration.class;

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
        LayerBuilder.File file = createFile(element);
        file.intvalue(POSITION, getPosition(element));
        file.write();
    }
    
    private LayerBuilder.File createFile(TypeElement element) throws LayerGenerationException {
        String name = getCategoryFileName(element);
        return layer(element).file(name);
    }
    
    private String getCategoryFileName(TypeElement element) throws LayerGenerationException {
        String category = getCategory(element);
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, category, name);
    }

    private String getCategory(TypeElement element) throws LayerGenerationException {
        String category = element.getAnnotation(ANNOTATION).category();
        checkCategory(element, category);
        return category==null? "" : category.trim();
    }
    
    private void checkCategory(TypeElement element, String category) throws LayerGenerationException {
        if(category==null | category.trim().length()==0) {
            String msg = String.format(ERR_CATEGORY, getClassName(element), ANNOTATION.getName(), category);
            throw new LayerGenerationException(msg);
        }
    }

    private int getPosition(TypeElement element) throws LayerGenerationException {
        return element.getAnnotation(ANNOTATION).position();
    }

    @Override
    protected String getFileName(TypeElement element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
