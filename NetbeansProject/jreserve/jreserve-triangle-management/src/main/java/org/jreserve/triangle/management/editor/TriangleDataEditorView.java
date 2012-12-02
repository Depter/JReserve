package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;

/**
 *
 * @author Peter Decsi
 */
class TriangleDataEditorView extends DataEditorView<Triangle> {

    TriangleDataEditorView(ProjectElement<Triangle> element) {
        super(element, Editor.TRIANGLE_IMG);
    }

    @Override
    protected void initGeometry() {
        TriangleGeometry triangleGeometry = getElementGeometry();
        initBegin(triangleGeometry);
        initPeriods(triangleGeometry);
        initMonths(triangleGeometry);
    }
    
    private void initBegin(TriangleGeometry geometry) {
        geometrySetting.setStartDate(geometry.getStartDate());
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
            geometrySetting.setAccidentMonthsPerStep(geometry.getAccidentMonths());
        } else {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getAccidentMonths());
            geometrySetting.setDevelopmentMonthsPerStep(geometry.getDevelopmentMonths());
        }
    }
    
    private boolean isMonthsSymmetric(TriangleGeometry geometry) {
        int aMonth = geometry.getAccidentMonths();
        int dMonth = geometry.getDevelopmentMonths();
        return aMonth == dMonth;
    }

    @Override
    protected List<TriangleCorrection> getCorrectionData() {
        return element.getValue().getCorrections();
    }

    @Override
    protected void setElementGeometry(TriangleGeometry geometry) {
        element.setProperty(Triangle.GEOMETRY_PROPERTY, geometry);
    }

    @Override
    protected TriangleGeometry getElementGeometry() {
        return element.getValue().getGeometry();
    }

    @Override
    protected boolean isGeometryChanged(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        return Triangle.GEOMETRY_PROPERTY.equals(property);
    }

    @Override
    protected void updateCorrections(List<TriangleCorrection> corrections) {
        element.setProperty(Triangle.CORRECTION_PROPERTY, corrections);
    }
}