package org.jreserve.triangle;
/** Localizable strings for {@link org.jreserve.triangle}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @param triangle_name triangle name
     * @return <i>Triangle "</i>{@code triangle_name}<i>" deleted.</i>
     * @see TriangleProjectElement
     */
    static String LOG_TriangleProjectElement_Deleted(Object triangle_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.TriangleProjectElement.Deleted", triangle_name);
    }
    /**
     * @param name name
     * @return <i>Description of triangle "</i>{@code name}<i>" changed.</i>
     * @see TriangleProjectElement
     */
    static String LOG_TriangleProjectElement_DescriptionChange(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.TriangleProjectElement.DescriptionChange", name);
    }
    /**
     * @param name name
     * @param old_geometry old geometry
     * @param new_geometry new geometry
     * @return <i>Geometry of triangle "</i>{@code name}<i>" changed </i>{@code old_geometry}<i> => </i>{@code new_geometry}<i>.</i>
     * @see TriangleProjectElement
     */
    static String LOG_TriangleProjectElement_GeometryChange(Object name, Object old_geometry, Object new_geometry) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.TriangleProjectElement.GeometryChange", name, old_geometry, new_geometry);
    }
    /**
     * @param old_name old name
     * @param new_name new name
     * @return <i>Triangle name changed "</i>{@code old_name}<i>" => "</i>{@code new_name}<i>"</i>
     * @see TriangleProjectElement
     */
    static String LOG_TriangleProjectElement_NameChange(Object old_name, Object new_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.TriangleProjectElement.NameChange", old_name, new_name);
    }
    /**
     * @param vector_name vector name
     * @return <i>Vector "</i>{@code vector_name}<i>" deleted.</i>
     * @see VectorProjectElement
     */
    static String LOG_VectorProjectElement_Deleted(Object vector_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.VectorProjectElement.Deleted", vector_name);
    }
    /**
     * @param name name
     * @return <i>Description of vector "</i>{@code name}<i>" changed.</i>
     * @see VectorProjectElement
     */
    static String LOG_VectorProjectElement_DescriptionChange(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.VectorProjectElement.DescriptionChange", name);
    }
    /**
     * @param name name
     * @param old_geometry old geometry
     * @param new_geometry new geometry
     * @return <i>Geometry of vector "</i>{@code name}<i>" changed </i>{@code old_geometry}<i> => </i>{@code new_geometry}<i>.</i>
     * @see VectorProjectElement
     */
    static String LOG_VectorProjectElement_GeometryChange(Object name, Object old_geometry, Object new_geometry) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.VectorProjectElement.GeometryChange", name, old_geometry, new_geometry);
    }
    /**
     * @param old_name old name
     * @param new_name new name
     * @return <i>Vector name changed "</i>{@code old_name}<i>" => "</i>{@code new_name}<i>"</i>
     * @see VectorProjectElement
     */
    static String LOG_VectorProjectElement_NameChange(Object old_name, Object new_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.VectorProjectElement.NameChange", old_name, new_name);
    }
    /**
     * @return <i>correction change</i>
     * @see TriangleProjectElement
     */
    static String MSG_TriangleProjectElement_UndoRedo_Correction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleProjectElement.UndoRedo.Correction");
    }
    /**
     * @return <i>geometry change</i>
     * @see TriangleProjectElement
     */
    static String MSG_TriangleProjectElement_UndoRedo_Geometry() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleProjectElement.UndoRedo.Geometry");
    }
    private void Bundle() {}
}
