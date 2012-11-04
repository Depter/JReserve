package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.*;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleDataEditorView extends DataEditorMultiviewElement<Triangle> {
    
    private final static String POPUP_PATH = "JReserve/Popup/TriangleDataEditor";

    TriangleDataEditorView(ProjectElement<Triangle> element) {
        super(element);
        super.triangle.setPopUpActionPath(POPUP_PATH);
    }
    
    @Override
    protected void initGeometry() {
        TriangleGeometry triangleGeometry = getElementGeometry();
        initBegin(triangleGeometry);
        initPeriods(triangleGeometry);
        initMonths(triangleGeometry);
    }
    
    private void initBegin(TriangleGeometry geometry) {
        if(isBeginSymmetric(geometry)) {
            geometrySetting.setSymmetricFromDate(true);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
        } else {
            geometrySetting.setSymmetricFromDate(false);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
            geometrySetting.setDevelopmentStartDate(geometry.getDevelopmentStart());
        }
    }
    
    private boolean isBeginSymmetric(TriangleGeometry geometry) {
        Date aBegin = geometry.getAccidentStart();
        Date dBegin = geometry.getDevelopmentStart();
        return aBegin.equals(dBegin);
    }
    
    private void initPeriods(TriangleGeometry geometry) {
        if(isPeriodSymmetric(geometry)) {
            geometrySetting.setSymmetricPeriods(true);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        } else {
            geometrySetting.setSymmetricPeriods(false);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
            geometrySetting.setDevelopmentPeriodCount(geometry.getDevelopmentPeriods());
        }
    }
    
    private boolean isPeriodSymmetric(TriangleGeometry geometry) {
        int aPeriod = geometry.getAccidentPeriods();
        int dPeriod = geometry.getDevelopmentPeriods();
        return aPeriod == dPeriod;
    }
    
    private void initMonths(TriangleGeometry geometry) {
        if(isMonthsSymmetric(geometry)) {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
        } else {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
            geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInDevelopment());
        }
    }
    
    private boolean isMonthsSymmetric(TriangleGeometry geometry) {
        int aMonth = geometry.getMonthInAccident();
        int dMonth = geometry.getMonthInDevelopment();
        return aMonth == dMonth;
    }

    @Override
    protected List<Data<Triangle, Double>> getCorrectionData() {
        List<Data<Triangle, Double>> datas = new ArrayList<Data<Triangle, Double>>();
        for(TriangleCorrection tc : element.getValue().getCorrections())
            datas.add(tc.toData());
        return datas;
    }

    @Override
    protected void setElementGeometry(TriangleGeometry geometry) {
        element.setProperty(TriangleProjectElement.GEOMETRY_PROPERTY, geometry);
    }

    @Override
    protected TriangleGeometry getElementGeometry() {
        return element.getValue().getGeometry();
    }

    @Override
    protected boolean isGeometryChanged(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        return TriangleProjectElement.GEOMETRY_PROPERTY.equals(property);
    }

    @Override
    protected void updateCorrections(List<Data<Triangle, Double>> datas) {
        List<TriangleCorrection> corrections = getCorrections(datas);
        element.setProperty(TriangleProjectElement.CORRECTION_PROPERTY, corrections);
    }
    
    private List<TriangleCorrection> getCorrections(List<Data<Triangle, Double>> datas) {
        List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>(datas.size());
        for(Data<Triangle, Double> data : datas)
            corrections.add(getCorrection(data));
        return corrections;
    }
    
    private TriangleCorrection getCorrection(Data<Triangle, Double> data) {
        TriangleCorrection tc = new TriangleCorrection(data.getOwner(), data.getAccidentDate(), data.getDevelopmentDate());
        tc.setCorrection(data.getValue());
        return tc;
    }

    @Override
    protected List<WidgetData<Comment>> getComments() {
        List<WidgetData<Comment>> comments = new ArrayList<WidgetData<Comment>>();
        for(TriangleComment comment : element.getValue().getComments())
            comments.add(new WidgetData<Comment>(comment.getAccidentDate(), comment.getDevelopmentDate(), comment));
        return comments;
    }
}
