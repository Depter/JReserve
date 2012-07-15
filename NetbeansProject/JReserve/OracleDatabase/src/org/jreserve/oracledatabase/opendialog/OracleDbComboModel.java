package org.jreserve.oracledatabase.opendialog;

import javax.swing.DefaultComboBoxModel;
import org.jreserve.oracledatabase.OracleDatabase;
import org.jreserve.oracledatabase.OracleHome;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OracleDbComboModel extends DefaultComboBoxModel<OracleDatabase> {
    
    OracleDbComboModel() {
        try {
            for(OracleDatabase db : OracleHome.getClosedDatabases())
                super.addElement(db);
        } catch (Exception ex) {
            super.removeAllElements();
        }
    }
}
