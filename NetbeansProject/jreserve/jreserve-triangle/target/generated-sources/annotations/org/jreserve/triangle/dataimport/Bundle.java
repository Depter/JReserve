package org.jreserve.triangle.dataimport;
/** Localizable strings for {@link org.jreserve.triangle.dataimport}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Create new triangle from existing data.</i>
     * @see TriangleCreatorWizard
     */
    static String LBL_TriangleImportWizard_Description() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.TriangleImportWizard.Description");
    }
    /**
     * @return <i>Triangle</i>
     * @see TriangleCreatorWizard
     */
    static String LBL_TriangleImportWizard_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.TriangleImportWizard.Name");
    }
    /**
     * @return <i>Create new vector from existing data.</i>
     * @see VectorCreatorWizard
     */
    static String LBL_VectorCreatorWizard_Description() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.VectorCreatorWizard.Description");
    }
    /**
     * @return <i>Vector</i>
     * @see VectorCreatorWizard
     */
    static String LBL_VectorCreatorWizard_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.VectorCreatorWizard.Name");
    }
    /**
     * @param triangle_name triangle name
     * @param db_id db id
     * @param db_name db name
     * @return <i>Created triangle "</i>{@code triangle_name}<i>" for data type </i>{@code db_id}<i> - </i>{@code db_name}
     * @see TriangleFormatWizard
     */
    static String LOG_TriangleFormatWizard_Created(Object triangle_name, Object db_id, Object db_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.TriangleFormatWizard.Created", triangle_name, db_id, db_name);
    }
    /**
     * @param vector_name vector name
     * @param db_id db id
     * @param db_name db name
     * @return <i>Created vector "</i>{@code vector_name}<i>" for data type </i>{@code db_id}<i> - </i>{@code db_name}
     * @see VectorFormatWizardPanel
     */
    static String LOG_VectorFormatWizardPanel_Created(Object vector_name, Object db_id, Object db_name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LOG.VectorFormatWizardPanel.Created", vector_name, db_id, db_name);
    }
    /**
     * @return <i>Unable to save triangle!</i>
     * @see TriangleFormatWizard
     */
    static String MSG_TriangleFormatWizardPanel_SaveError() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TriangleFormatWizardPanel.SaveError");
    }
    /**
     * @return <i>Unable to save vector!</i>
     * @see VectorFormatWizardPanel
     */
    static String MSG_VectorFormatWizardPanel_SaveError() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.VectorFormatWizardPanel.SaveError");
    }
    private void Bundle() {}
}
