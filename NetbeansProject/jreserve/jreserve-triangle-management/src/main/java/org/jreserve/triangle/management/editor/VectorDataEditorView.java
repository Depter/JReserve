package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.*;
import org.jreserve.triangle.management.VectorProjectElement;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 */
public class VectorDataEditorView extends DataEditorView<Vector> {
    
    private final static String POPUP_PATH = "JReserve/Popup/VectorDataEditor";

    VectorDataEditorView(ProjectElement<Vector> element) {
        super(element, Editor.VECTOR_IMG);
        super.triangle.setPopUpActionPath(POPUP_PATH);
    }

    @Override
    protected void initGeometry() {
        initSymmetry();
        VectorGeometry vectorGeometry = element.getValue().getGeometry();
        initBegin(vectorGeometry);
        initPeriods(vectorGeometry);
        initMonths(vectorGeometry);
    }
    
    private void initSymmetry() {
        geometrySetting.setSymmetricPeriods(false);
        geometrySetting.setSymmetricMonths(false);
        geometrySetting.setSymmetricEnabled(false);
    }
    
    private void initBegin(VectorGeometry geometry) {
        geometrySetting.setStartDate(geometry.getStartDate());
    }
    
    private void initPeriods(VectorGeometry geometry) {
        geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        geometrySetting.setDevelopmentPeriodCount(1);
    }
    
    private void initMonths(VectorGeometry geometry) {
        geometrySetting.setAccidentMonthsPerStep(geometry.getAccidentMonths());
        geometrySetting.setDevelopmentMonthsPerStep(Integer.MAX_VALUE);
    }

    @Override
    protected List<Data<Vector, Double>> getCorrectionData() {
        List<Data<Vector, Double>> datas = new ArrayList<Data<Vector, Double>>();
        for(VectorCorrection tc : element.getValue().getCorrections())
            datas.add(tc.toData());
        return datas;
    }

    @Override
    protected void setElementGeometry(TriangleGeometry geometry) {
        VectorGeometry vg = getVectorGeometry(geometry);
        element.setProperty(VectorProjectElement.GEOMETRY_PROPERTY, vg);
    }
    
    private VectorGeometry getVectorGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            return null;
        Date start = geometry.getStartDate();
        int periods = geometry.getAccidentPeriods();
        int months = geometry.getAccidentMonths();
        return new VectorGeometry(start, periods, months);
    }

    @Override
    protected TriangleGeometry getElementGeometry() {
        VectorGeometry geometry = element.getValue().getGeometry();
        if(geometry == null)
            return null;
        return new TriangleDummy(geometry);
    }

    @Override
    protected boolean isGeometryChanged(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        return VectorProjectElement.GEOMETRY_PROPERTY.equals(property);
    }

    @Override
    protected void updateCorrections(List<Data<Vector, Double>> datas) {
        List<VectorCorrection> corrections = getCorrections(datas);
        element.setProperty(VectorProjectElement.CORRECTION_PROPERTY, corrections);
    }
    
    private List<VectorCorrection> getCorrections(List<Data<Vector, Double>> datas) {
        List<VectorCorrection> corrections = new ArrayList<VectorCorrection>(datas.size());
        for(Data<Vector, Double> data : datas)
            corrections.add(getCorrection(data));
        return corrections;
    }
    
    private VectorCorrection getCorrection(Data<Vector, Double> data) {
        VectorCorrection tc = new VectorCorrection(data.getOwner(), data.getAccidentDate());
        tc.setCorrection(data.getValue());
        return tc;
    }

    @Override
    protected List<WidgetData<Comment>> getComments() {
        List<WidgetData<Comment>> comments = new ArrayList<WidgetData<Comment>>();
        for(VectorComment comment : element.getValue().getComments())
            comments.add(new WidgetData<Comment>(comment.getAccidentDate(), comment.getAccidentDate(), comment));
        return comments;
    }

    @Override
    protected List<Smoothing> getSmoothings() {
        return element.getValue().getSmoothings();
    }
    
    private static class TriangleDummy extends TriangleGeometry {
    
        private VectorGeometry geometry;
        
        TriangleDummy(VectorGeometry geometry) {
            this.geometry = geometry;
        }

        @Override
        public TriangleGeometry copy() {
            return this;
        }

        @Override
        public int getDevelopmentPeriods() {
            return 1;
        }

        @Override
        public int getMonthInDevelopment() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isEqualGeometry(TriangleGeometry g) {
            if(this == g) return true;
            if(g instanceof TriangleDummy)
                return geometry.isEqualGeometry(((TriangleDummy)g).geometry);
            return false;
        }

        @Override
        public int getAccidentPeriods() {
            return geometry.getAccidentPeriods();
        }

        @Override
        public Date getStartDate() {
            return geometry.getStartDate();
        }

        @Override
        public int getAccidentMonths() {
            return geometry.getAccidentMonths();
        }

        @Override
        public boolean isEqualGeometry(VectorGeometry g) {
            return geometry.isEqualGeometry(g);
        }
    }
}
