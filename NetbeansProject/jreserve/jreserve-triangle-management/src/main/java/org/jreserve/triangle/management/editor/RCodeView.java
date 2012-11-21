package org.jreserve.triangle.management.editor;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.rutil.NavigableRCodePanel;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RUtil;
import org.jreserve.smoothing.RSmoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidgetListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RCodeView.Title=R - Code"
})
public abstract class RCodeView extends NavigableRCodePanel implements TriangleWidgetListener {

    public static RCodeView getTriangleView(ProjectElement element, TriangleWidget widget) {
        return new RCodeView(element, widget) {
            @Override
            protected List<Smoothing> getSmoothings() {
                return (List<Smoothing>) element.getProperty(Triangle.SMOOTHING_PROPERTY);
            }
        };
    }

    public static RCodeView getVectorView(ProjectElement element, TriangleWidget widget) {
        return new RCodeView(element, widget) {
            @Override
            protected List<Smoothing> getSmoothings() {
                return (List<Smoothing>) element.getProperty(Vector.SMOOTHING_PROPERTY);
            }
        };
    }
    
    private DateRenderer renderer = new DateRenderer();
    private TriangleWidget widget;
    protected ProjectElement element = null;
    private RCode code;
    private String dataName;
    
    private RCodeView(ProjectElement element, TriangleWidget widget) {
        super(Bundle.LBL_RCodeView_Title());
        this.element = element;
        this.widget = widget;
        widget.addTriangleWidgetListener(this);
        this.code = super.getRCode();
    }
    
    private void setCode() {
        dataName = getElementName();
        code.clear();
        addDataToCode();
        addCorrectionsToCode();
        addSmoothings();
        printResult();
        code.fireChangeEvent();
    }
    
    private String getElementName() {
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        if(name == null)
            return "";
        return name.replace(' ', '.');
    }
    
    private void addDataToCode() {
        double[][] data = widget.flattenLayer(TriangleCell.VALUE_LAYER);
        code.addSource(new StringBuilder(dataName).append(" = ").append(RUtil.createArray(data)).append("\n").toString());
        setRowNames(data);
        setColumnNames(data);
    }
    
    private void setRowNames(double[][] data) {
        if(data == null || data.length==0) return;
        GeometryUtil util = GeometryUtil.getEDTInstance();
        TriangleGeometry geometry = widget.getTriangleGeometry();
        String[] names = new String[data.length];
        for(int r=0,size=data.length; r<size; r++)
            names[r] = renderer.toString(util.getAccidentBegin(geometry, r));
        code.addSource(new StringBuilder("rownames(").append(dataName).append(") = ").append(RUtil.createVector(names)).append("\n").toString());
    }
    
    private void setColumnNames(double[][] data) {
        int count = getColumnCount(data);
        if(count < 1) return;
        String[] names = new String[count];
        for(int i=0; i<count; i++)
            names[i] = ""+(i+1);
        code.addSource(new StringBuilder("colnames(").append(dataName).append(") = ").append(RUtil.createVector(names)).append("\n").toString());
    }
    
    private int getColumnCount(double[][] data) {
        if(data == null || data.length==0)
            return 0;
        int count = 0;
        for(double[] row : data)
            if(row != null && count < row.length)
                count = row.length;
        return count;
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
    
    private void addSmoothings() {
        List<Smoothing> smoothings = getSmoothings();
        if(smoothings == null || smoothings.isEmpty()) 
            return;
        RSmoother smoother = new RSmoother(dataName, widget);
        for(Smoothing smoothing : smoothings) {
            String str = dataName + " = "+ smoother.getRSmoothing(smoothing)+"\n";
            code.addSource(str);
            code.addFunction(smoothing.getRFunction());
        }
    }
    
    protected abstract List<Smoothing> getSmoothings();
    
    private void printResult() {
        code.addSource("\n\n").addSource("round(")
            .addSource(dataName).addSource(", "+widget.getVisibleDigits())
            .addSource(")\n");
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
