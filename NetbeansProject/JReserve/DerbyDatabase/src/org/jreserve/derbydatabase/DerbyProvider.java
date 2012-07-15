package org.jreserve.derbydatabase;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import org.jreserve.database.api.Database;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.derbydatabase.createdialog.CreateDatabaseWizard;
import org.jreserve.derbydatabase.opendialog.OpenDerbyDbWizard;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=DatabaseProvider.class)
public class DerbyProvider implements DatabaseProvider {
    
    public final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String IMG_PATH = "org/jreserve/derbydatabase/derby.png";
    private static Image img;
    
    static Image getDerbyImage() {
        if(img == null)
            img = ImageUtilities.loadImage(IMG_PATH);
        return img;
    }
    
    public DerbyProvider() {
    }
    
    @Override
    public String getName() {
        return "Derby";
    }
    
    @Override
    public List<? extends Database> getDatabases() throws IOException {
        return DerbyHome.getDatabases();
    }

    @Override
    public boolean createDatabase() throws Exception {
        CreateDatabaseWizard wizard = new CreateDatabaseWizard();
        return addDatabase(wizard.createDatabase());
    }

    @Override
    public boolean openDatabase() throws Exception {
        OpenDerbyDbWizard wizard = new OpenDerbyDbWizard();
        return addDatabase(wizard.openDerbyDb());
    }

    private boolean addDatabase(DerbyDatabase db) throws Exception {
        if(db != null)
            DerbyHome.addDatabase(db);
        return db!=null;
    }

    @Override
    public Image getIcon() {
        return getDerbyImage();
    }
}
