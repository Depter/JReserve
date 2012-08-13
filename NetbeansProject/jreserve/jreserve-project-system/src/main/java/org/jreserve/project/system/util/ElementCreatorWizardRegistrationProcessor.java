package org.jreserve.project.system.util;

import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.management.ElementCreatorWizard.Registration;
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
@SupportedAnnotationTypes("org.jreserve.project.system.management.ElementCreatorWizard.Registration")
public class ElementCreatorWizardRegistrationProcessor extends LayerGeneratingProcessor {
    
    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    
    private final static Class<Registration> ANNOTATION = ElementCreatorWizard.Registration.class;
    private final static Class<ElementCreatorWizard> INTERFACE = ElementCreatorWizard.class;
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> set, RoundEnvironment re) throws LayerGenerationException {
        if(re.processingOver())
            return false;
        processRoundEnvironment(re);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for(Element e : getElements(re))
            processElement((TypeElement) e);
    }

    private Set<? extends Element> getElements(RoundEnvironment re) {
        return re.getElementsAnnotatedWith(ANNOTATION);
    }

    private void processElement(TypeElement e) throws LayerGenerationException {
        checkElement(e);
        Registration registration = e.getAnnotation(ANNOTATION);
        addClass(e, registration);
    }
    
    private void checkElement(TypeElement element) throws LayerGenerationException {
        checkImplementsElementCreatorWizard(element);
        checkConstructor(element);
    }
    
    private void checkImplementsElementCreatorWizard(TypeElement element) throws LayerGenerationException {
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
    
    private void addClass(TypeElement element, Registration registration) throws LayerGenerationException {
        String name = getFileName(element, registration.category());
        LayerBuilder.File file = layer(element).file(name);
        file.bundlevalue(ElementCategoryUtil.CATEGORY_ELEMENT_NAME, registration.displayName());
        file.stringvalue(ElementCategoryUtil.CATEGORY_ELEMENT_ICON, registration.iconBase());
        file.write();
    }
    
    private String getFileName(TypeElement element, Category category) {
        String name = getClassName(element);
        name = name.replaceAll("\\.", "-")+".instance";
        return ElementCategoryUtil.getRootName(category) + "/" + name;
    }
}
