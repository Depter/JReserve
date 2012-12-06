package org.jreserve.resources.annotation;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRegistrationProcessor  extends LayerGeneratingProcessor {

    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> set, RoundEnvironment re) throws LayerGenerationException {
        if(re.processingOver())
            return false;
        processRoundEnvironment(re);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for (Element e : getElements(re))
            processElement((TypeElement) e);
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(getAnnotation());
    }
    
    private void processElement(TypeElement element) throws LayerGenerationException {
        checkImplementsInterface(element);
        checkConstructor(element);
        registerElement(element);
    }
    
    protected void checkImplementsInterface(TypeElement element) throws LayerGenerationException {
        TypeMirror mirror = getInterfaceMirror();
        if(processingEnv.getTypeUtils().isAssignable(element.asType(), mirror))
            return;
        String msg = String.format(ERR_NOT_IMPLEMENTS_INTERFACE, getClassName(element), getAnnotationName(), getInterfaceName());
        throw new LayerGenerationException(msg);
    }
    
    private String getAnnotationName() {
        return getAnnotation().getName();
    }
    
    protected abstract Class<? extends Annotation> getAnnotation();
    
    private String getInterfaceName() {
        return getInterface().getName();
    }
    
    protected abstract Class<?> getInterface();
    
    private TypeMirror getInterfaceMirror() {
        Elements utils = processingEnv.getElementUtils();
        String name = getInterfaceName();
        return utils.getTypeElement(name).asType();
    }
    
    protected String getClassName(TypeElement element) {
        return element.getQualifiedName().toString();
    }
    
    protected void checkConstructor(TypeElement element) throws LayerGenerationException {
        for(Element e : element.getEnclosedElements())
            if(isConstructor(e))
                return;
        throw noAppropriateConstructor(element);
    }
    
    private boolean isConstructor(Element element) {
        if(ElementKind.CONSTRUCTOR != element.getKind())
            return false;
        ExecutableElement constructor = (ExecutableElement) element;
        return isConstructor(constructor);
    }
    
    private boolean isConstructor(ExecutableElement element) {
        if(!element.getParameters().isEmpty())
            return false;
        return element.getModifiers().contains(Modifier.PUBLIC);
    }
    
    private LayerGenerationException noAppropriateConstructor(TypeElement element) {
        String msg = String.format(ERR_NO_COSNTRUCTOR, 
                getClassName(element), getAnnotationName());
        return new LayerGenerationException(msg);
    }

    protected abstract void registerElement(TypeElement element) throws LayerGenerationException;
    
    protected LayerBuilder.File getFile(TypeElement element) {
        String name = getFileName(element);
        return layer(element).file(name);
    }
    
    protected abstract String getFileName(TypeElement element);
}
