package org.jreserve.factor.core.linkratio.smoothing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="LINK_RATIO_SMOOTHING", schema="JRESERVE")
public class LinkRatioSmoothing extends AbstractPersistentObject implements Comparable<LinkRatioSmoothing> {
    private final static long serialVersionUID = 1L;
    
    @Column(name="DEVELOPMENT", nullable=false)
    private int development;
    
    @Column(name="METHOD_ID", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String methodId;
    
    protected LinkRatioSmoothing() {
    }
    
    protected LinkRatioSmoothing(int development, String methodId) {
        initMethodId(methodId);
        this.development = development;
    }

    private void initMethodId(String methodId) {
        if(methodId == null)
            throw new NullPointerException("MethodId can not be null!");
        this.methodId = methodId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        initMethodId(methodId);
    }
    
    public int getDevelopment() {
        return development;
    }
    
    @Override
    public int compareTo(LinkRatioSmoothing o) {
        if(o == null)
            return -1;
        return development - o.development;
    }
    
    @Override
    public String toString() {
        String msg = "LinkRatioSmoothing [dev=%d; method=%s]";
        return String.format(msg, development, methodId);
    }
}
