package org.jreserve.dataimport.util;

import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.jreserve.dataimport.DataImportWizard;
import org.jreserve.dataimport.DataImportWizard.Registration;
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
@SupportedAnnotationTypes("org.jreserve.dataimport.DataImportWizard.Registration")
public class DataImportWizardRegistrationProcessor extends LayerGeneratingProcessor {

    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    private final static String FILE_NAME = "JReserve/ImportWizard/%s.instance";
    private final static String DISPLAY_NAME = "displayName";
    private final static String ICON_BASE = "iconBase";
    private final static String POSITION = "position";
    
    private final static Class<Registration> ANNOTATION = DataImportWizard.Registration.class;
    private final static Class<DataImportWizard> INTERFACE = DataImportWizard.class;
    
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
        String name = getFileName(element);
        LayerBuilder.File file = layer(element).file(name);
        file.bundlevalue(DISPLAY_NAME, registration.displayName());
        setIconBase(file, registration);
        file.intvalue(POSITION, registration.position());
        file.write();
    }
    
    private String getFileName(TypeElement element) {
        String className = getClassName(element);
        className = className.replace('.', '-');
        return String.format(FILE_NAME, className);
    }
    
    private void setIconBase(LayerBuilder.File file, Registration registration) {
        String iconBase = registration.iconBase();
        if(iconBase.length() > 0)
            file.stringvalue(ICON_BASE, iconBase);
    }
}
