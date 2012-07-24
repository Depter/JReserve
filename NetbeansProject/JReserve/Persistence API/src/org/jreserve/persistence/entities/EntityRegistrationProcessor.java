package org.jreserve.persistence.entities;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.persistence.Entity;
import org.jreserve.persistence.EntityRegistration;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EntityRegistrationProcessor extends LayerGeneratingProcessor {
    
    private final static String LOCATION = "jreserve/entities/%s.entity";
    
    private final static String ERR_NOT_ENTITY = 
        "Class '%s' is not annotated with '%s'!";
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EntityRegistration.class.getCanonicalName());
    }
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if (roundEnv.processingOver())
            return false;
        processRoundEnviroment(roundEnv);
        return true;
    }
    
    private void processRoundEnviroment(RoundEnvironment roundEnv) throws LayerGenerationException {
        for (Element e : getElements(roundEnv))
            processElement(e);
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(EntityRegistration.class);
    }
    
    private void processElement(Element element) throws LayerGenerationException {
        TypeElement classElement = getClassElement(element);
        checkEntityElement(classElement, element);
        addClass(classElement, element);
    }
    
    private TypeElement getClassElement(Element element) {
        AnnotationMirror mirror = getAnnotationMirror(element);
        if(mirror == null)
            return null;
        return getClassElement(mirror);
    }
    
    private AnnotationMirror getAnnotationMirror(Element element) {
        String clazzName = EntityRegistration.class.getName();
        for(AnnotationMirror mirror : element.getAnnotationMirrors())
            if(mirror.getAnnotationType().toString().equals(clazzName))
                return mirror;
        return null;
    }
    
    private TypeElement getClassElement(AnnotationMirror mirror) {
        AnnotationValue value = getAnnotationValue(mirror, "entityClass");
        if(value == null)
            return null;
        return getClassElement(value);
    }
    
    private AnnotationValue getAnnotationValue(AnnotationMirror mirror, String key) {
        for(Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror.getElementValues().entrySet())
            if(entry.getKey().getSimpleName().toString().equals(key))
                return entry.getValue();
        return null;
    }
    
    private TypeElement getClassElement(AnnotationValue value) {
        TypeMirror typeMirror = (TypeMirror) value.getValue();
        Types utils = this.processingEnv.getTypeUtils();
        return (TypeElement) utils.asElement(typeMirror);
    }
    
    private void checkEntityElement(TypeElement c, Element e) throws LayerGenerationException {
        if(c == null)
            throw new LayerGenerationException("Entity class is null!", e);
        if(c.getAnnotation(Entity.class) == null)
            throw notEntityException(c, e);
    }
    
    private LayerGenerationException notEntityException(TypeElement c, Element e) {
        String msg = String.format(ERR_NOT_ENTITY, 
            c.getQualifiedName().toString(), Entity.class.getName());
        return new LayerGenerationException(msg, e);
    }
    
    private void addClass(TypeElement c, Element e) {
        String cName = c.getQualifiedName().toString();
        LayerBuilder.File file = layer(e).file(getFileName(cName));
        file.stringvalue(EntityDataObject.CLASS_ATTRIBUTE, cName);
        file.write();
    }
    
    private String getFileName(String name) {
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
}
