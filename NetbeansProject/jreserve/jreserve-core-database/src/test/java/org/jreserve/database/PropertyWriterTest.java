package org.jreserve.database;

import java.io.IOException;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class PropertyWriterTest {
    
    private static Properties properties;
    
    public PropertyWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        properties = new Properties();
        properties.put("prop1", "1");
        properties.put("prop2", "2");
        properties.put("prop3", "3");
        properties.put("prop4", "4");
    }
    
    @After
    public void tearDown() throws IOException {
        Util.deleteTextFile("PropertyWriterTest");
    }

    @Test
    public void testWriteProperties() throws Exception {
        FileObject file = Util.createTextFile("PropertyWriterTest");
        PropertyWriter writer = new PropertyWriter(file);
        writer.writeProperties(properties);
        
        Properties found = new Properties();
        found.load(file.getInputStream());
        assertEquals(properties.size(), found.size());
        
        for(String key : properties.stringPropertyNames()) {
            assertTrue(found.containsKey(key));
            assertEquals(properties.get(key), found.get(key));
        }
    }
}
