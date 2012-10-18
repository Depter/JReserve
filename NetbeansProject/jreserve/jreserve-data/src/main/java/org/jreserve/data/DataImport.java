package org.jreserve.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataImport.AddNew=Add only new data",
    "LBL.DataImport.DeleteOld=Overwrite old data"
})
public abstract class DataImport {
    
    private final static Logger logger = Logger.getLogger(DataImport.class.getName());
    
    public static void importTable(DataTable table, ImportType type) {
        DataImport impl = getImplementation(table, type);
        impl.importTable();
    }
    
    private static DataImport getImplementation(DataTable table, ImportType type) {
        switch(type) {
            case DELETE_OLD:
                return new DeleteOldDataImport(table);
            case ADD_NEW:
                return new AddNewDataImport(table);
            default:
                throw new IllegalArgumentException("Unknown import type: "+type);
        }
    }
    
    protected DataSource ds;
    protected DataTable table;
    
    private DataImport(DataTable table) {
        ds = new DataSource();
        this.table = table;
    }
    
    private void importTable() {
        logger.log(Level.INFO, "Importing data table \"{0}\"", table);
        ds.open();
        try {
            mergeValues();
            processTable();
            ds.commit();
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to import data table '%s'. Session is rolled back.", table), ex);
            ds.rollBack();
        }
    }
    
    private void mergeValues() {
        ds.getSession().merge(table.getClaimType(), table.getDataType());
    }
    
    protected abstract void processTable();
        
    protected List<Data<Double>> getTableAsList() {
        List<Data<Double>> result = new ArrayList<Data<Double>>();
        for(Date accidentDate : table.getAccidentDates())
            result.addAll(table.getDatas(accidentDate));
        return result;
    }
    
    public static enum ImportType {
        ADD_NEW(Bundle.LBL_DataImport_AddNew()),
        DELETE_OLD(Bundle.LBL_DataImport_DeleteOld());
        
        private final String userName;
        
        private ImportType(String userName) {
            this.userName = userName;
        }
        
        public String getUserName() {
            return userName;
        }
    }
    
    private static class DeleteOldDataImport extends DataImport {
        
        private DeleteOldDataImport(DataTable table) {
            super(table);
        }

        @Override
        protected void processTable() {
            deleteOldValues();
            List<Data<Double>> datas = super.getTableAsList();
            ds.saveClaimData(datas);
        }
        
        private void deleteOldValues() {
            Criteria delete = createDeleteCriteria();
            ds.clearData(delete);
        }
        
        private Criteria createDeleteCriteria() {
            return new Criteria(table.getClaimType())
                    .setDataType(table.getDataType())
                    .setFromAccidentDate(table.getFirstAccidnetDate())
                    .setToAccidentDate(table.getLastAccidentDate());
        }
    }
    
    private static class AddNewDataImport extends DataImport {
    
        private AddNewDataImport(DataTable table) {
            super(table);
        }

        @Override
        protected void processTable() {
            List<Data<Double>> persisted = getPersistedData();
            List<Data<Double>> newData = getNewData(persisted);
            ds.saveClaimData(newData);
        }
        
        private List<Data<Double>> getPersistedData() {
            Criteria c = createCriteria();
            return ds.getClaimData(c);
        }
        
        private Criteria createCriteria() {
            return new Criteria(table.getClaimType())
                    .setDataType(table.getDataType())
                    .setFromAccidentDate(table.getFirstAccidnetDate())
                    .setToAccidentDate(table.getLastAccidentDate());
        }
        
        private List<Data<Double>> getNewData(List<Data<Double>> persisted) {
            List<Data<Double>> newData = getTableAsList();
            for(Iterator<Data<Double>> it = newData.iterator(); it.hasNext();)
                if(persisted.contains(it.next()))
                    it.remove();
            return newData;
        }
    }
}
