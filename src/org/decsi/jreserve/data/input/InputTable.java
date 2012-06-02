package org.decsi.jreserve.data.input;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.decsi.jreserve.data.DataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputTable implements Iterable<InputRecord> {
    
    private final int claimTypeId;
    private final DataType type;
    private Set<InputRecord> records = new HashSet<InputRecord>();
    
    public InputTable(int claimTypeId, DataType type) {
        this.claimTypeId = claimTypeId;
        this.type = type;
    }
    
    public DataType getType() {
        return type;
    }
    
    public int getClaimTypeId() {
        return claimTypeId;
    }
    
    public int getRecordCount() {
        return records.size();
    }
    
    public boolean addRecord(InputRecord record) {
        if(record == null)
            return false;
        return records.add(record);
    }
    
    public boolean removeRecord(InputRecord record) {
        return records.remove(record);
    }

    @Override
    public Iterator<InputRecord> iterator() {
        return records.iterator();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof InputTable)
            return equals((InputTable) o);
        return false;
    }
    
    public boolean equals(InputTable table) {
        if(table == null)
            return false;
        return claimTypeId == table.claimTypeId &&
               type == table.type;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 * claimTypeId;
        return 17 * hash + type.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("InputTable [%d; %s]",
                claimTypeId, type);
    }
}