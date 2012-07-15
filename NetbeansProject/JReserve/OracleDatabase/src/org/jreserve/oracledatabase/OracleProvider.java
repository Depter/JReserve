package org.jreserve.oracledatabase;

import java.awt.Image;
import java.util.List;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.oracledatabase.createdialog.CreateDatabaseWizard;
import org.jreserve.oracledatabase.opendialog.OpenDialogWizard;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=DatabaseProvider.class)
public class OracleProvider implements DatabaseProvider {
    
    public final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final static String IMG_PATH = "org/jreserve/oracledatabase/oracle.jpg";
    private static Image img;
    
    static Image getImage() {
        if(img == null)
            img = ImageUtilities.loadImage(IMG_PATH);
        return img;
    }
    
    public OracleProvider() {
    }
    
    @Override
    public String getName() {
        return "Oracle";
    }
    
    @Override
    public List<OracleDatabase> getDatabases() throws Exception {
        return OracleHome.getDatabases();
    }

    @Override
    public boolean createDatabase() throws Exception {
        CreateDatabaseWizard wizard = new CreateDatabaseWizard();
        return addDatabase(wizard.createDatabase());
    }

    @Override
    public boolean openDatabase() throws Exception {
        OpenDialogWizard wizard = new OpenDialogWizard();
        return OracleHome.openDatabase(wizard.openDatabase());
    }

    private boolean addDatabase(OracleDatabase db) throws Exception {
        if(db != null)
            OracleHome.addDatabase(db);
        return db!=null;
    }

    @Override
    public Image getIcon() {
        return getImage();
    }

}
