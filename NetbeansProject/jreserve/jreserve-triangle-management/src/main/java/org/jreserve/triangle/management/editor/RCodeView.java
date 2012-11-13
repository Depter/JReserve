package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.rutil.NavigableRCodePanel;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidgetListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RCodeView.Title=R - Code"
})
public class RCodeView extends NavigableRCodePanel implements TriangleWidgetListener {

    private TriangleWidget widget;
    private ProjectElement element = null;
    private RCode code;
    private String dataName;
    
    public RCodeView(ProjectElement element, TriangleWidget widget) {
        super(Bundle.LBL_RCodeView_Title());
        this.element = element;
        this.widget = widget;
        widget.addTriangleWidgetListener(this);
        this.code = super.getRCode();
    }
    
    private void setCode() {
        dataName = getElementName();
        code.clear();
        code.addSource("rm(list=ls())\n\n");
        addDataToCode();
        addCorrectionsToCode();
    }
    
    private void addDataToCode() {
        double[][] data = widget.flattenLayer(TriangleCell.VALUE_LAYER);
        int columns = getColumnCount(data);
        
        StringBuilder sb = new StringBuilder(dataName).append(" = rbind(\n");
        for(int r=0, size=data.length; r<size; r++) {
            sb.append("  ");
            appendRow(sb, data[r], columns);
            if(r<(size-1)) sb.append(",");
            sb.append("\n");
        }
        sb.append(")\n");
        code.addSource(sb.toString());
    }
    
    private int getColumnCount(double[][] data) {
        int count = 0;
        for(double[] row : data) {
            if(row == null) continue;
            if(count < row.length) count = row.length;
        }
        return count;
    }
    
    private String getElementName() {
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        if(name == null)
            return "";
        return name.replace(' ', '.');
    }
    
    private void appendRow(StringBuilder sb, double[] row, int columnCount) {
        sb.append("c(");
        for(int c=0; c<columnCount; c++) {
            if(c > 0) sb.append(", ");
            if(c >= row.length || Double.isNaN(row[c])) {
                sb.append("NA");
            } else {
                sb.append(row[c]);
            }
        }
        sb.append(")");
    } 
    
    private void addCorrectionsToCode() {
        List<String> corrections = getCorrections();
        if(!corrections.isEmpty()) {
            code.addSource("\n");
            for(String correction : corrections)
                code.addSource(correction+"\n");
        }
    }
    
    private List<String> getCorrections() {
        double[][] data = widget.flattenLayer(TriangleCell.CORRECTION_LAYER);
        List<String> result = new ArrayList<String>();

        if(data == null) 
            return result;
        
        for(int r=0, rCount=data.length; r<rCount; r++) {
            double[] row = data[r];
            if(row == null || row.length==0) 
                continue;
            
            for(int c=0, cCount=row.length; c<cCount; c++) {
                double d = data[r][c];
                if(!Double.isNaN(d))
                    result.add(getCorrection(r, c, d));
            }
        }
        
        return result;
    }
    
    private String getCorrection(int r, int c, double value) {
        return dataName+"["+(r+1)+", "+(c+1)+"] = "+value;
    }

    @Override
    public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
        setCode();
    }

    @Override
    public void commentsChanged() {
    }

    @Override
    public void valuesChanged() {
        setCode();
    }

    @Override
    public void structureChanged() {
        setCode();
    }
}
