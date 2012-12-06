package org.jreserve.triangle.data.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class MetaData implements Serializable {
    
    private final static int NAME_SIZE = 64;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ProjectDataType dataType;
    
    @Column(name="NAME", nullable=false, length=64)
    private String name;
    
    @Column(name="DESCRIPTION")
    private String description;
    
    protected MetaData() {
    }
    
    MetaData(ProjectDataType dataType, String name) {
        initDataType(dataType);
        initName(name);
    }
    
    private void initDataType(ProjectDataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_SIZE);
        this.name = name;
    }

    public ProjectDataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
