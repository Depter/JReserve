package org.jreserve.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import org.junit.*;
import static org.junit.Assert.*;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class PropertyReaderTest {
    
    private static Properties properties;

    public PropertyReaderTest() {
    }

    @Before
    public void setUp() throws IOException {
        FileObject file = Util.createTextFile("PropertyReaderTest");
        PrintWriter writer = new PrintWriter(file.getOutputStream());
        
        for(int i=1; i<5; i++) 
            writer.printf("prop%1$d=%1$d%n", i);
        
        writer.flush();
        writer.close();
    }

    @After
    public void tearDown() throws IOException {
        Util.deleteTextFile("PropertyReaderTest");
    }

    /**
     * Test of readProperties method, of class PropertyReader.
     */
    @Test
    public void testReadProperties() throws Exception {
        FileObject file = Util.getTextFile("PropertyReaderTest");
        PropertyReader reader = new PropertyReader(file);
        
        Properties found = reader.readProperties();
        assertEquals(4, found.size());
        
        for(int i=1; i<5; i++) {
            String key = String.format("prop%1d", i);
            assertEquals(found.get(key), ""+i);
        }
    }

}