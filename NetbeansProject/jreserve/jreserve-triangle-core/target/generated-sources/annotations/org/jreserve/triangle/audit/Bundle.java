package org.jreserve.triangle.audit;
/** Localizable strings for {@link org.jreserve.triangle.audit}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Comment</i>
     * @see CommentAuditor
     */
    static String LBL_CommentAuditor_TypeName() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CommentAuditor.TypeName");
    }
    /**
     * @param user user
     * @param accident accident
     * @param development development
     * @param date date
     * @param message message
     * @return <i>ADDED [</i>{@code user}<i>; </i>{@code accident}<i>/</i>{@code development}<i>] {3, date, long}: </i>{@code message}
     * @see CommentAuditor
     */
    static String MSG_CommentAuditor_Added(Object user, Object accident, Object development, Object date, Object message) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CommentAuditor.Added", user, accident, development, date, message);
    }
    /**
     * @param user user
     * @param accident accident
     * @param development development
     * @param date date
     * @param message message
     * @return <i>DELETED [</i>{@code user}<i>; </i>{@code accident}<i>/</i>{@code development}<i>] {3, date, long}: </i>{@code message}
     * @see CommentAuditor
     */
    static String MSG_CommentAuditor_Deleted(Object user, Object accident, Object development, Object date, Object message) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CommentAuditor.Deleted", user, accident, development, date, message);
    }
    /**
     * @param name name
     * @param change change
     * @return <i>Triange "</i>{@code name}<i>": </i>{@code change}
     * @see ProjectTriangleAuditor
     */
    static String MSG_ProjectTriangleAuditor_Change(Object name, Object change) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ProjectTriangleAuditor.Change", name, change);
    }
    /**
     * @param name name
     * @return <i>Created triangle "</i>{@code name}<i>"</i>
     * @see ProjectTriangleAuditor
     */
    static String MSG_ProjectTriangleAuditor_Created(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ProjectTriangleAuditor.Created", name);
    }
    /**
     * @param name name
     * @return <i>Deleted triangle "</i>{@code name}<i>"</i>
     * @see ProjectTriangleAuditor
     */
    static String MSG_ProjectTriangleAuditor_Deleted(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ProjectTriangleAuditor.Deleted", name);
    }
    /**
     * @return <i>Created</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_Created() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.Created");
    }
    /**
     * @return <i>Deleted</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_Deleted() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.Deleted");
    }
    /**
     * @return <i>Description changed</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_DescriptionChange() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.DescriptionChange");
    }
    /**
     * @param old_geometry old geometry
     * @param new_geometry new geometry
     * @return <i>Geometry changed "</i>{@code old_geometry}<i>" => "</i>{@code new_geometry}<i>".</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_GeometryChange(Object old_geometry, Object new_geometry) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.GeometryChange", old_geometry, new_geometry);
    }
    /**
     * @param old_name old name
     * @param new_name new name
     * @return <i>Name changed "</i>{@code old_name}<i>" => "</i>{@code new_name}<i>".</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_NameChange(Object old_name, Object new_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.NameChange", old_name, new_name);
    }
    /**
     * @return <i>Saved. </i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_Saved() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.Saved");
    }
    /**
     * @return <i>Triangle</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_TypeName_Triangle() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.TypeName.Triangle");
    }
    /**
     * @return <i>Vector</i>
     * @see TriangleAuditor
     */
    static String MSG_TriangleAuditor_TypeName_Vector() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleAuditor.TypeName.Vector");
    }
    private void Bundle() {}
}
