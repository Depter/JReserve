package org.jreserve.triangle.widget.util;

import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.swing.table.TableCellRenderer;
import org.jreserve.resources.annotation.AbstractRegistrationProcessor;
import org.jreserve.triangle.widget.WidgetRendererRegistration;
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
@SupportedAnnotationTypes("org.jreserve.triangle.widget.WidgetRendererRegistration")
public class WidgetRendererRegistrationProcessor extends AbstractRegistrationProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Widget/WidgetRenderer";
    final static String LAYER_ID = "layerId";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";
    
    private final static String ERR_LAYER_ID = 
        "Class '%s' annotated with '%s', and has an invalid layer id '%s'!";
    
    private final static Class<?> INTERFACE = TableCellRenderer.class;
    private final static Class<WidgetRendererRegistration> ANNOTATION = WidgetRendererRegistration.class;

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
        file.stringvalue(LAYER_ID, getLayerId(element));
    }

    private String getLayerId(TypeElement element) throws LayerGenerationException {
        String layerId = element.getAnnotation(ANNOTATION).layerId();
        checkLayerId(element, layerId);
        return layerId==null? "" : layerId.trim();
    }
    
    private void checkLayerId(TypeElement element, String layerId) throws LayerGenerationException {
        if(layerId==null | layerId.trim().length()==0) {
            String msg = String.format(ERR_LAYER_ID, getClassName(element), ANNOTATION.getName(), layerId);
            throw new LayerGenerationException(msg);
        }
    }
}