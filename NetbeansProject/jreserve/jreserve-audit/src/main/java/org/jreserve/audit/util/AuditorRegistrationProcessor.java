package org.jreserve.audit.util;

import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;
import org.jreserve.audit.Auditor;
import org.openide.filesystems.annotations.LayerBuilder;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.audit.Auditor.Registration")
public class AuditorRegistrationProcessor extends LayerGeneratingProcessor {
    final static String ENTITY_DIRECTORY = "JReserve/Audit/AuditFactories";
    final static String PRIORITY = "priority";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.instance";

    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    private final static String ERR_NEGATIVE_PRORITY = 
        "Class '%s' annotated with '%s', and has a negative priority %d!";
    
    private final static Class<Auditor.Registration> ANNOTATION = Auditor.Registration.class;
    private final static Class<?> INTERFACE = Auditor.class;
    
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
        return roundEnv.getElementsAnnotatedWith(ANNOTATION);
    }
    
    private void processElement(TypeElement element) throws LayerGenerationException {
        checkImplementsAuditor(element);
        checkConstructor(element);
        int priority = getPriority(element);
        addClass(element, priority);
    }
    
    private void checkImplementsAuditor(TypeElement element) throws LayerGenerationException {
        TypeMirror mirror = getProjectElementFactoryMirror();
        if(processingEnv.getTypeUtils().isAssignable(element.asType(), mirror))
            return;
        String msg = String.format(ERR_NOT_IMPLEMENTS_INTERFACE, getClassName(element), ANNOTATION.getName(), INTERFACE.getName());
        throw new LayerGenerationException(msg);
    }
    
    private TypeMirror getProjectElementFactoryMirror() {
        Elements utils = processingEnv.getElementUtils();
        String name = INTERFACE.getName();
        return utils.getTypeElement(name).asType();
    }
    
    private String getClassName(TypeElement element) {
        return element.getQualifiedName().toString();
    }
    
    private void checkConstructor(TypeElement element) throws LayerGenerationException {
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
                getClassName(element), ANNOTATION.getName());
        return new LayerGenerationException(msg);
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
    
    private void addClass(TypeElement element, int priority) {
        String name = getFileName(element);
        LayerBuilder.File file = layer(element).file(name);
        file.intvalue(PRIORITY, priority);
        file.write();
    }
    
    private String getFileName(TypeElement element) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
}
