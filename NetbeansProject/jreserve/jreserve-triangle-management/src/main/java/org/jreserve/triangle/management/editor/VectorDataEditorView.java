package org.jreserve.triangle.management.editor;

import java.beans.PropertyChangeEvent;
import java.util.Date;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;

/**
 *
 * @author Peter Decsi
 */
public class VectorDataEditorView extends DataEditorView<Vector> {

    VectorDataEditorView(ProjectElement<Vector> element) {
        super(element, Editor.VECTOR_IMG);
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
    protected void setElementGeometry(TriangleGeometry geometry) {
        VectorGeometry vg = getVectorGeometry(geometry);
        element.setProperty(Vector.GEOMETRY_PROPERTY, vg);
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
        return Vector.GEOMETRY_PROPERTY.equals(property);
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
        public int getDevelopmentMonths() {
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
