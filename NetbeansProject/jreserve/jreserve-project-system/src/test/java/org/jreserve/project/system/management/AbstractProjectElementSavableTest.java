package org.jreserve.project.system.management;

import java.io.IOException;
import org.jreserve.project.system.ProjectElement;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.api.actions.Savable;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractProjectElementSavableTest {
    
    private String value = "Bela";
    private ProjectElement<String> element;
    private SavableImpl savable;
    
    public AbstractProjectElementSavableTest() {
    }
    
    @Before
    public void setUp() {
        createElement();
        savable = new SavableImpl(element);
    }
    
    private void createElement() {
        element = new ProjectElement<String>(value);
        element.setProperty(ProjectElement.NAME_PROPERTY, value);
    }
    
    @After
    public void tearDown() {
        if(isSavableRegistered())
            savable.unregisterSavable();
    }
    
    @Test
    public void testRegisterSavable() {
        assertFalse(isSavableRegistered(Savable.REGISTRY));
        assertFalse(isSavableRegistered(element.getLookup()));
        
        savable.registerSavable();
        
        assertTrue(isSavableRegistered(Savable.REGISTRY));
        assertTrue(isSavableRegistered(element.getLookup()));
    }

    private boolean isSavableRegistered() {
        return isSavableRegistered(Savable.REGISTRY);
    }
    
    private boolean isSavableRegistered(Lookup lookup) {
        for(SavableImpl s : lookup.lookupAll(SavableImpl.class))
            if(s == savable)
                return true;
        return false;
    }
    
    @Test
    public void testUnregisterSavable() {
        savable.registerSavable();
        assertTrue(isSavableRegistered(Savable.REGISTRY));
        assertTrue(isSavableRegistered(element.getLookup()));
        
        savable.unregisterSavable();
    
        assertFalse(isSavableRegistered(Savable.REGISTRY));
        assertFalse(isSavableRegistered(element.getLookup()));
    }
    
    @Test
    public void testRegisteredAfterChange() {
        assertFalse(isSavableRegistered());
        element.setProperty(ProjectElement.NAME_PROPERTY, value+value);
        assertTrue(isSavableRegistered());
    }
    
    @Test
    public void testNotRegisteredIfNotChanged() {
        assertFalse(isSavableRegistered());
        
        Object name = value;
        element.setProperty(ProjectElement.NAME_PROPERTY, name);
        
        assertFalse(isSavableRegistered());
    }
    
    @Test
    public void testChanged_O1O2() {
        assertFalse(savable.isChanged("bela", "bela"));
        assertFalse(savable.isChanged(null, null));
        assertTrue(savable.isChanged("bela", null));
        assertTrue(savable.isChanged(null, "bela"));
        assertTrue(savable.isChanged(new Object(), new Object()));
    }
    
    @Test
    public void testUnregisteredRegisteredIfNotChanged() {
        assertFalse(isSavableRegistered());
        
        element.setProperty(ProjectElement.NAME_PROPERTY, value+value);
        assertTrue(isSavableRegistered());
        
        element.setProperty(ProjectElement.NAME_PROPERTY, value);
        assertFalse(isSavableRegistered());
        
        element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, value);
        assertTrue(isSavableRegistered());
        
        element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, null);
        assertFalse(isSavableRegistered());
    }
    
    @Test
    public void setOriginalSetAfterSave() throws IOException {
        assertEquals(value, savable.getOriginalProperty(ProjectElement.NAME_PROPERTY));
        assertEquals(null, savable.getOriginalProperty(ProjectElement.DESCRIPTION_PROPERTY));
        
        String newName = "Geza";
        String newDescription = "meaningful text";
        element.setProperty(ProjectElement.NAME_PROPERTY, newName);
        element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, newDescription);
        
        assertEquals(value, savable.getOriginalProperty(ProjectElement.NAME_PROPERTY));
        assertEquals(null, savable.getOriginalProperty(ProjectElement.DESCRIPTION_PROPERTY));
        
        savable.save();
        assertEquals(newName, savable.getOriginalProperty(ProjectElement.NAME_PROPERTY));
        assertEquals(newDescription, savable.getOriginalProperty(ProjectElement.DESCRIPTION_PROPERTY));
    }
    
    @Test
    public void testUnregistedAfterSave() throws IOException {
        assertFalse(isSavableRegistered());
        
        element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, value);
        assertTrue(isSavableRegistered());
        
        savable.save();
        assertFalse(isSavableRegistered());
    }
    
    private static class SavableImpl extends AbstractProjectElementSavable<String> {

        public SavableImpl(ProjectElement<String> element) {
            super(element);
        }

        @Override
        public void initOriginalProperties() {
            initOriginalProperty(ProjectElement.NAME_PROPERTY);
            initOriginalProperty(ProjectElement.DESCRIPTION_PROPERTY);
        }
        
        private void initOriginalProperty(String property) {
            Object value = element.getProperty(property);
            originalProperties.put(property, value);
        }

        @Override
        public void saveElement() throws IOException {
        }
        
        Object getOriginalProperty(String property) {
            return originalProperties.get(property);
        }
    }
}
