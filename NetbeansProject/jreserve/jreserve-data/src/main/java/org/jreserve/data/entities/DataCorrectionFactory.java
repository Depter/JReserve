package org.jreserve.data.entities;

import java.util.Date;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class DataCorrectionFactory<O extends PersistentObject> implements DataFactory<O, DataCorrection, Double> {

    private O owner;
    
    public DataCorrectionFactory(O owner) {
        this.owner = owner;
    }
    
    @Override
    public Data<O, Double> createData(DataCorrection entity) {
        Date accident = entity.getAccidentDate();
        Date development = entity.getDevelopmentDate();
        return new Data<O, Double>(owner, accident, development, entity.getCorrection());
    }
}
