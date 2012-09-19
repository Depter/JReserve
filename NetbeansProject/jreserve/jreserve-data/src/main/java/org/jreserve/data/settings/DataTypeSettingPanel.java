package org.jreserve.data.settings;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.event.TableModelEvent;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTypeSettingPanel extends DataTypePanel {
    
    private DataTypesOptionsPanelController controller;
    private boolean isValid = true;
    
    DataTypeSettingPanel(DataTypesOptionsPanelController controller) {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.controller = controller;
    }
    
    
    void load() {
        List<DTDummy> dummies = new ArrayList<DTDummy>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            dummies.add(new DTDummy(dt));
        super.setDummies(dummies);
    }
    
    void store() {
        if(removeDeleted() | updateList())
            DataTypeUtil.save();
    }
    
    private boolean removeDeleted() {
        boolean deleted = false;
        for(DataType dt : DataTypeUtil.getDataTypes())
            if(super.getDummy(dt.getDbId()) == null) {
                deleted = true;
                DataTypeUtil.deleteDataType(dt);
            }
        return deleted;
    }
    
    private boolean updateList() {
        boolean updated = false;
        int rowCount = super.getDummyCount();
        for(int r=0; r<rowCount; r++) {
            if(updateRow(r))
                updated = true;
        }
        return updated;
    }
    
    private boolean updateRow(int row) {
        DTDummy dummy = super.getDummyAtRow(row);
        DataType dt = DataTypeUtil.getDataType(dummy.getId());
        if(dt == null) {
            createDataType(dummy);
            return true;
        } else {
            return updateDataType(dt, dummy);
        }
    }
    
    private void createDataType(DTDummy dummy) {
        int id = dummy.getId();
        String name = dummy.getName();
        boolean isTriangle = dummy.isTriangle();
        DataTypeUtil.createDataType(id, name, isTriangle);
    }
    
    private boolean updateDataType(DataType dt, DTDummy dummy) {
        boolean updated = false;
        if(!dt.getName().equals(dummy.getName())) {
            updated = true;
            DataTypeUtil.setName(dt, dummy.getName());
        }
        if(dt.isTriangle() != dummy.isTriangle()) {
            updated = true;
            DataTypeUtil.setTriangle(dt, dummy.isTriangle());
        }
        return updated;
    }
    
    @Override
    public boolean valid() {
        return super.valid();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        controller.changed();
    }
}
