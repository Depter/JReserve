package org.jreserve.persistence.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.*;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import org.jreserve.persistence.EntityRegistration;
import static org.jreserve.persistence.EntityRegistration.*;
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
    
    final static String ENTITY_DIRECTORY = "jreserve/entities";
    final static String CLASS_ATRIBUTE = "className";
    final static String GENERATED_ID_ATTRIBUTE = "generatedId";
    private final static String LOCATION = ENTITY_DIRECTORY + "/%s.entity";

    private final static String ERR_NOT_ENTITY = 
        "Class '%s' is not annotated with '%s'!";
    private final static String ERR_NO_TABLE_GENERATOR = 
        "Class '%s' should have a generated id, but it is not annotated with javax.persistence.TableGenerator!";
    private final static String ERR_TG_WRONG_CATALOG = 
        "Class '%s' should have a generated id, but it defines a table generator in catalog '%s' instead of '%s'!";
    private final static String ERR_TG_WRONG_SCHEMA = 
        "Class '%s' should have a generated id, but it defines a table generator in schema '%s' instead of '%s'!";
    private final static String ERR_TG_WRONG_TABLE = 
        "Class '%s' should have a generated id, but it defines a table generator for table '%s' instead of '%s'!";
    private final static String ERR_TG_WRONG_PK = 
        "Class '%s' should have a generated id, but it defines a primary key column '%s' instead of '%s'!";
    private final static String ERR_TG_WRONG_VALUE = 
        "Class '%s' should have a generated id, but it defines a value column '%s' instead of '%s'!";
    private final static String ERR_TG_WRONG_PK_VALUE = 
        "Class '%s' should have a generated id, but it defines a pk value '%s' instead of '%s'!";
    private final static String ERR_TOO_MANY_IDS = 
        "Class '%s' should have a generated id, but it has more than one id field!";
    private final static String ERR_NO_GENERATED_VALUE = 
        "Class '%s' should have a generated id, but it's id member '%s' is not annotated with javax.persistence.GeneratedValue!";
    private final static String ERR_WRONG_STRATEGY = 
        "Class '%s' should have a generated id, but it's id member '%s' is not "
      + "using javax.persistence.GeneratedValue.TABLE strategy!";
    private final static String ERR_WRONG_GENERATOR = 
        "Class '%s' should have a generated id, but it's id member '%s' is  "
      + "using generator '%s' isntead of '%s'!";
    private final static String ERR_WRONG_ID_TYPE = 
        "Class '%s' should have a generated id, but the type ('%s') of it's id "
      + "member '%s' is not 'long' or 'java.lang.Long'!";
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EntityRegistration.class.getCanonicalName());
    }
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if(roundEnv.processingOver())
            return false;
        processRoundEnviroment(roundEnv);
        return false;
    }
    
    private void processRoundEnviroment(RoundEnvironment roundEnv) throws LayerGenerationException {
        Messager messager = processingEnv.getMessager();
        for (Element e : getElements(roundEnv)) {
            TypeElement type = (TypeElement) e;
            messager.printMessage(Kind.WARNING, "Processing entity registration: "+getClassName(type));
            processElement(type);
        }
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(EntityRegistration.class);
    }
    
    private void processElement(TypeElement element) throws LayerGenerationException {
        checkEntityElement(element);
        checkGeneratedId(element);
        String cName = element.getQualifiedName().toString();
        addClass(cName, element);
    }
    
    private void checkEntityElement(TypeElement c) throws LayerGenerationException {
        if(c.getAnnotation(Entity.class) == null)
            throw notEntityException(c);
    }
    
    private LayerGenerationException notEntityException(TypeElement c) {
        String msg = String.format(ERR_NOT_ENTITY, 
            getClassName(c), Entity.class.getName());
        return new LayerGenerationException(msg, c);
    }
    
    private String getClassName(TypeElement element) {
        return element.getQualifiedName().toString();
    }
    
    private void checkGeneratedId(TypeElement element) throws LayerGenerationException {
        if(!getRegistration(element).generateId()) return;
        String tgName = checkTableGenerator(element);
        List<Element> ids = getIdMembers(element);
        if(ids.size() > 1) throw tooManyIds(element);
        checkIdElement(ids.get(0), tgName);
    }
    
    private EntityRegistration getRegistration(TypeElement element) {
        return element.getAnnotation(EntityRegistration.class);
    }
    
    private String checkTableGenerator(TypeElement element) throws LayerGenerationException {
        TableGenerator tg = element.getAnnotation(TableGenerator.class);
        if(tg == null) throw noTableGenerator(element);
        checkTableGenerator(element, tg);
        return tg.name();
    }
    
    private LayerGenerationException noTableGenerator(TypeElement element) {
        String msg = String.format(ERR_NO_TABLE_GENERATOR, getClassName(element));
        return new LayerGenerationException(msg, element);
    }
    
    private void checkTableGenerator(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        checkTableGeneratorCatalog(element, tg);
        checkTableGeneratorSchema(element, tg);
        checkTableGeneratorTable(element, tg);
        checkTableGeneratorPk(element, tg);
        checkTableGeneratorValue(element, tg);
        checkTableGeneratorPkValue(element, tg);
    }
    
    
    private void checkTableGeneratorCatalog(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        if(!CATALOG.equalsIgnoreCase(tg.catalog())) {
            String msg = String.format(ERR_TG_WRONG_CATALOG, getClassName(element), tg.catalog(), CATALOG);
            throw new LayerGenerationException(msg, element);
        }
    }

    private void checkTableGeneratorSchema(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        if(!SCHEMA.equalsIgnoreCase(tg.schema())) {
            String msg = String.format(ERR_TG_WRONG_SCHEMA, getClassName(element), tg.schema(), SCHEMA);
            throw new LayerGenerationException(msg, element);
        }
    }
    
    private void checkTableGeneratorTable(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        if(!TABLE.equalsIgnoreCase(tg.table())) {
            String msg = String.format(ERR_TG_WRONG_TABLE, getClassName(element), tg.table(), TABLE);
            throw new LayerGenerationException(msg, element);
        }
    }
    
    private void checkTableGeneratorPk(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        if(!ID_COLUMN.equalsIgnoreCase(tg.pkColumnName())) {
            String msg = String.format(ERR_TG_WRONG_PK, getClassName(element), tg.pkColumnName(), ID_COLUMN);
            throw new LayerGenerationException(msg, element);
        }
    }
    
    private void checkTableGeneratorValue(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        if(!VALUE_COLUMN.equalsIgnoreCase(tg.valueColumnName())) {
            String msg = String.format(ERR_TG_WRONG_VALUE, getClassName(element), tg.valueColumnName(), VALUE_COLUMN);
            throw new LayerGenerationException(msg, element);
        }
    }
    
    private void checkTableGeneratorPkValue(TypeElement element, TableGenerator tg) throws LayerGenerationException {
        String name = getClassName(element);
        if(!name.equalsIgnoreCase(tg.pkColumnValue())) {
            String msg = String.format(ERR_TG_WRONG_PK_VALUE, name, tg.pkColumnValue(), name);
            throw new LayerGenerationException(msg, element);
        }
    }
    
    private List<Element> getIdMembers(TypeElement element) {
        List<Element> result = new ArrayList<Element>();
        for(Element e : element.getEnclosedElements())
            if(e.getAnnotation(Id.class) != null)
                result.add(e);
        return result;
    }
    
    private LayerGenerationException tooManyIds(TypeElement element) {
        String msg = String.format(ERR_TOO_MANY_IDS, 
            element.getQualifiedName().toString());
        return new LayerGenerationException(msg, element);
    }
    
    private void checkIdElement(Element element, String tgName) throws LayerGenerationException {
        checkGeneratedValue(element, tgName);
        if(element instanceof VariableElement)
            checkIdField((VariableElement) element);
        else
            checkIdMethod((ExecutableElement) element);
    }
    
    private void checkGeneratedValue(Element element, String tgName) throws LayerGenerationException {
        GeneratedValue gv = element.getAnnotation(GeneratedValue.class);
        if(gv == null) throw noGeneratorValue(element);
        if(GenerationType.TABLE != gv.strategy()) throw notTableStrategy(element);
        if(!tgName.equals(gv.generator())) throw wrongGenerator(element, gv.generator(), tgName);
    }
    
    private LayerGenerationException noGeneratorValue(Element element) {
        TypeElement p = (TypeElement) element.getEnclosingElement();
        String msg = String.format(ERR_NO_GENERATED_VALUE, 
            getClassName(p), element.getSimpleName());
        return new LayerGenerationException(msg, element);
    }
    
    private LayerGenerationException notTableStrategy(Element element) {
        TypeElement p = (TypeElement) element.getEnclosingElement();
        String msg = String.format(ERR_WRONG_STRATEGY, 
            getClassName(p), element.getSimpleName());
        return new LayerGenerationException(msg, element);
    }
    
    private LayerGenerationException wrongGenerator(Element element, String found, String expected) {
        TypeElement p = (TypeElement) element.getEnclosingElement();
        String msg = String.format(ERR_WRONG_GENERATOR, 
            getClassName(p), element.getSimpleName(), found, expected);
        return new LayerGenerationException(msg, element);
    }
    
    private void checkIdField(VariableElement field) throws LayerGenerationException {
        checkIdType(field, field.asType());
    }
    
    private void checkIdType(Element idElement, TypeMirror mirror) throws LayerGenerationException {
        TypeElement type = (TypeElement) processingEnv.getTypeUtils().asElement(mirror);
        String cName = type==null? mirror.toString() : getClassName(type);
        if(!cName.equalsIgnoreCase("long") && !cName.equalsIgnoreCase(Long.class.getName()))
            throw notLongException(idElement, cName);
    }
    
    private LayerGenerationException notLongException(Element element, String cName) {
        TypeElement p = (TypeElement) element.getEnclosingElement();
        String msg = String.format(ERR_WRONG_ID_TYPE, 
            getClassName(p), cName, element.getSimpleName());
        return new LayerGenerationException(msg, element);
    }
    
    private void checkIdMethod(ExecutableElement method) throws LayerGenerationException {
        checkIdType(method, method.getReturnType());
    }
    
    private void addClass(String cName, TypeElement e) {
        LayerBuilder.File file = layer(e).file(getFileName(cName));
        file.stringvalue(CLASS_ATRIBUTE, cName);
        buildFile(file, getRegistration(e));
    }
    
    private String getFileName(String name) {
        name = name.replaceAll("\\.", "-");
        return String.format(LOCATION, name);
    }
    
    private void buildFile(LayerBuilder.File file, EntityRegistration registration) {
        file.boolvalue(GENERATED_ID_ATTRIBUTE, registration.generateId());
        file.write();
    }
}
