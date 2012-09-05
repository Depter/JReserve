package org.jreserve.project.system.filesystem;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class PFSChangeTest {

    private ProjectFileSystem pfs;
    private ProjectFile root;
    private PFSChange change;
    
    public PFSChangeTest() {
    }

    @Before
    public void setUp() {
        pfs = new ProjectFileSystem();
        root = pfs.getFileForLocation("");
        change = new PFSChange(pfs);
    }

    @Test
    public void testCreateFolder() throws Exception {
        change.createFolder("bela");
        List<ProjectFile> children = root.getChildren();
        assertEquals(1, children.size());
        assertEquals("bela", children.get(0).getName());
    }

    @Test
    public void testCreateData() throws Exception {
        change.createFolder("bela");
        change.createData("bela/geza.entity");

        List<ProjectFile> children = root.getChildren();
        ProjectFile bela = children.get(0);
        children = bela.getChildren();
        assertEquals(1, children.size());
        assertEquals("geza.entity", children.get(0).getName());
    }

    @Test
    public void testRename() throws Exception {
        change.createFolder("bela");
        change.createFolder("bela/geza.entity");
        
        change.rename("bela/geza.entity", "bela/dezso.entity");
        List<ProjectFile> children = root.getChildren();
        ProjectFile bela = children.get(0);
        children = bela.getChildren();
        assertEquals(1, children.size());
        assertEquals("dezso.entity", children.get(0).getName());
        
        change.createFolder("geza");
        change.rename("bela/dezso.entity", "geza/bela.entity");
        
        children = root.getChildren();
        ProjectFile geza = children.get(1);
        children = geza.getChildren();
        assertEquals(1, children.size());
        assertEquals("bela.entity", children.get(0).getName());        
    }

    @Test
    public void testDelete() throws Exception {
        change.createFolder("bela");
        change.createFolder("bela/geza.entity");
        
        change.delete("bela/geza.entity");
        List<ProjectFile> children = root.getChildren();
        ProjectFile bela = children.get(0);
        assertTrue(bela.getChildren().isEmpty());
    }

}