package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractNamedDataBuilder<T> {
    
    protected int id;
    protected String shortName;
    protected String name;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        if(name == null || name.trim().length()==0)
            return;
        this.name = name;
    }
    
    public void setShortName(String shortName) {
        if(shortName == null || shortName.trim().length()==0)
            return;
        this.shortName = shortName;
    }
    
    protected void checkState() {
        if(name == null)
            throw new IllegalStateException("Name not set!");
        if(shortName == null)
            throw new IllegalStateException("Short name is not set!");
    }
    
    protected void clear() {
        this.id = 0;
        this.shortName = null;
        this.name = null;
    }
    
    public abstract T build();
}
