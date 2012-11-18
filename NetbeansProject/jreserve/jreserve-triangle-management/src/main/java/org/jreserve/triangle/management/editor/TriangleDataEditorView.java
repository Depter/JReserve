package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.management.TriangleProjectElement;

/**
 *
 * @author Peter Decsi
 */
class TriangleDataEditorView extends DataEditorView<Triangle> {
    
    private final static String POPUP_PATH = "JReserve/Popup/TriangleDataEditor";

    TriangleDataEditorView(ProjectElement<Triangle> element) {
        super(element, Editor.TRIANGLE_IMG);
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
            geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInDevelopment());
        }
    }
    
    private boolean isMonthsSymmetric(TriangleGeometry geometry) {
        int aMonth = geometry.getAccidentMonths();
        int dMonth = geometry.getMonthInDevelopment();
        return aMonth == dMonth;
    }

    @Override
    protected List<TriangleCorrection> getCorrectionData() {
        return element.getValue().getCorrections();
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
    protected void updateCorrections(List<TriangleCorrection> corrections) {
        element.setProperty(TriangleProjectElement.CORRECTION_PROPERTY, corrections);
    }

    @Override
    protected List<TriangleComment> getComments() {
        return element.getValue().getComments();
    }

    @Override
    protected List<Smoothing> getSmoothings() {
        return element.getValue().getSmoothings();
    }
}
