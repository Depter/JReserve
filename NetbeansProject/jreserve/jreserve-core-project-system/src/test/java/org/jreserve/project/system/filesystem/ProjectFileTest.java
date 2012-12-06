package org.jreserve.project.system.filesystem;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectFileTest {

    private final static String NAME = "bela";
    private ProjectFile root;
    private ProjectFile child;
    
    public ProjectFileTest() {
    }

    @Before
    public void setUp() throws Exception {
        root = ProjectFile.createRoot();
        child = root.createChild(NAME);
    }

    @Test
    public void testCreateRoot() {
        assertEquals(null, root.getName());
    }

    @Test
    public void testGetName() throws IOException {
        assertEquals("bela", child.getName());
    }

    @Test
    public void testSetName() {
        child.setName("geza");
        assertEquals("geza", child.getName());
        
        child.setName("");
        assertEquals("", child.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_NullName() {
        child.setName(null);
    }

    @Test
    public void testIsFolder() {
        assertTrue(root.isFolder());
        assertTrue(child.isFolder());
    }

    @Test
    public void testGetParent() {
        assertEquals(root, child.getParent());
    }

    @Test
    public void testGetChildren() {
        List<ProjectFile> children = root.getChildren();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test
    public void testSetAttribute() {
        assertEquals(null, child.getAttribute("attribute"));
        child.setAttribute("attribute", "value");
        assertEquals("value", child.getAttribute("attribute"));
    }

    @Test
    public void testGetAttributeNames() {
        child.setAttribute("a1", "value");
        child.setAttribute("a2", "value");
        child.setAttribute("a3", "value");
        
        List<String> names = child.getAttributeNames();
        assertEquals(3, names.size());
        assertTrue(names.remove("a1"));
        assertTrue(names.remove("a2"));
        assertTrue(names.remove("a3"));
        assertEquals(0, names.size());
    }

    @Test
    public void testClearAttributes() {
        child.setAttribute("a1", "value");
        child.setAttribute("a2", "value");
        child.setAttribute("a3", "value");
        
        List<String> names = child.getAttributeNames();
        assertEquals(3, names.size());
        
        child.clearAttributes();
        names = child.getAttributeNames();
        assertEquals(0, names.size());
    }

    @Test(expected=IOException.class)
    public void testCreateFile_NameNull() throws IOException{
        root.createChild(null);
    }
    
    @Test
    public void testAddChild() throws IOException {
        ProjectFile child2 = root.createChild("bela2");
        ProjectFile geza = child2.createChild("geza");
        ProjectFile bela = child2.createChild("bela");
        geza.delete();
        bela.delete();
        
        child.addChild(geza);
        child.addChild(bela);
        List<ProjectFile> children = child.getChildren();
        assertEquals(2, children.size());
        assertEquals(bela, children.get(0));
        assertEquals(geza, children.get(1));
    }

    @Test
    public void testDelete() throws IOException {
        child.delete();
        assertEquals(null, child.getParent());
        assertTrue(root.getChildren().isEmpty());
    }

    @Test
    public void testGetPath() throws IOException {
        assertEquals("", root.getPath());
        assertEquals(NAME, child.getPath());
        
        ProjectFile file = child.createChild("geza");
        String expected = NAME + ProjectFileSystem.PATH_SEPARATOR + "geza";
        assertEquals(expected, file.getPath());
    }
}