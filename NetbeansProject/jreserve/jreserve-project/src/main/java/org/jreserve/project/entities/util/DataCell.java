package org.jreserve.project.entities.util;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCell {

    private final Date accident;
    private final Date beginDevelopment;
    private final Date endDevelopment;
    
    DataCell(Date accident, Date beginDevelopemnt, Date endDevelopment) {
        this.accident = accident;
        this.beginDevelopment = beginDevelopemnt;
        this.endDevelopment = endDevelopment;
    }
}
