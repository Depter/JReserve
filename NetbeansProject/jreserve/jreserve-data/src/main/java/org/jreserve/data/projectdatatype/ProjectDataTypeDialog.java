package org.jreserve.data.projectdatatype;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.data.settings.DTDummy;
import org.jreserve.data.settings.DataTypePanel;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Deleted=Data type \"{0} ({1})\" deleted.",
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Created=Data type \"{0} ({1})\" created.",
    "# {0} - old name of data type",
    "# {1} - dbId of data type",
    "# {2} - new of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Renamed=Data type \"{0} ({1})\" renamed to \"{2}\".",
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.IsTriangle=Data type \"{0} ({1})\" set to triangle.",
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.IsVector=Data type \"{0} ({1})\" set to vector."
})
class ProjectDataTypeDialog extends JPanel implements PropertyChangeListener, ActionListener {
    
    private final static boolean IS_MODAL = true;
    
    static void showDialog(Project project) {
        ProjectDataTypeDialog content = new ProjectDataTypeDialog(project);
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }
    
    private static DialogDescriptor createDescriptor(ProjectDataTypeDialog content) {
        return new DialogDescriptor(
            content,
            "Title",
            IS_MODAL,
            null, null,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private Dialog dialog;
    private Project project;
    private DataTypePanel dtPanel;
            
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("Cancel");
    
    private List<ProjectDataType> originalTypes;
    
    private ProjectDataTypeDialog(Project project) {
        this.project = project;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        dtPanel = new DataTypePanel(false);
        dtPanel.addPropertyChangeListener(this);
        add(dtPanel, BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0d; gc.weighty = 0d;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(dtPanel.getButtonPanel(), gc);
        
        gc.gridx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1d;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(Box.createHorizontalGlue(), gc);

        gc.gridx = 2;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0d;
        panel.add(getDialogButtons(), gc);
        return panel;
    }
    
    private JPanel getDialogButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        panel.add(okButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(cancelButton);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        return panel;
    }
    
    private void loadData() {
        List<DTDummy> dummies = new ArrayList<DTDummy>();
        for(ProjectDataType dt : getDataTypes())
            dummies.add(getDummy(dt));
        dtPanel.setDummies(dummies);
    }
    
    private List<ProjectDataType> getDataTypes() {
        originalTypes = ProjectDataTypeUtil.getDefault().getValues(project);
        return originalTypes;
    }
    
    private DTDummy getDummy(ProjectDataType dt) {
        return new DTDummy(
                dt.getDbId(), dt.getName(), dt.isTriangle()
                );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() != dtPanel || !evt.getPropertyName().equals(DataTypePanel.PROP_IS_VALID))
            return;
        boolean isValid = (Boolean) evt.getNewValue();
        okButton.setEnabled(isValid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(cancelButton == source) {
            dialog.dispose();
        } else {
            store();
            dialog.dispose();
        }
    }

    
    void store() {
        if(removeDeleted() | updateList()) {
            ProjectDataTypeUtil.getDefault().saveValues(project);
            ChangeLogUtil.getDefault().saveValues(project);
        }
    }
    
    private boolean removeDeleted() {
        boolean deleted = false;
        for(ProjectDataType dt : originalTypes)
            if(dtPanel.getDummy(dt.getDbId()) == null) {
                logDeletion(project, dt);
                ProjectDataTypeUtil util = ProjectDataTypeUtil.getDefault();
                util.deleteValue(project, dt);
                deleted = true;
            }
        return deleted;
    }
    
    private void logDeletion(Project project, ProjectDataType dt) {
        String name = dt.getName();
        int dbId = dt.getDbId();
        String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Deleted(name, dbId);
        ChangeLogUtil.getDefault().addChange(project, ChangeLog.Type.PROJECT, msg);
    }
    
    private boolean updateList() {
        boolean updated = false;
        int rowCount = dtPanel.getDummyCount();
        for(int r=0; r<rowCount; r++) {
            if(updateRow(r))
                updated = true;
        }
        return updated;
    }
    
    private boolean updateRow(int row) {
        DTDummy dummy = dtPanel.getDummyAtRow(row);
        ProjectDataType dt = getOriginalProjectDataType(dummy.getId());
        if(dt == null) {
            createDataType(dummy);
            return true;
        } else {
            return updateDataType(dt, dummy);
        }
    }
    
    private ProjectDataType getOriginalProjectDataType(int dbId) {
        for(ProjectDataType dt : originalTypes)
            if(dt.getDbId() == dbId)
                return dt;
        return null;
    }
    
    private void createDataType(DTDummy dummy) {
        int id = dummy.getId();
        String name = dummy.getName();
        boolean isTriangle = dummy.isTriangle();
        ProjectDataType dt = new ProjectDataType(project, id, name, isTriangle);
        logCreation(dt);
        ProjectDataTypeUtil.getDefault().addValue(project, dt);
    }
    
    private void logCreation(ProjectDataType dt) {
        String name = dt.getName();
        int dbId = dt.getDbId();
        String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Created(name, dbId);
        ChangeLogUtil.getDefault().addChange(project, ChangeLog.Type.PROJECT, msg);
    }
    
    private boolean updateDataType(ProjectDataType dt, DTDummy dummy) {
        boolean updated = false;
        if(!dt.getName().equals(dummy.getName())) {
            updated = true;
            logNameChange(dt, dummy.getName());
            dt.setName(dummy.getName());
        }
        if(dt.isTriangle() != dummy.isTriangle()) {
            updated = true;
            logTriangleChange(dt);
            dt.setTriangle(dummy.isTriangle());
        }
        return updated;
    }

    private void logNameChange(ProjectDataType dt, String newName) {
        String oldName = dt.getName();
        int dbId = dt.getDbId();
        String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Renamed(oldName, dbId, newName);
        ChangeLogUtil.getDefault().addChange(project, ChangeLog.Type.PROJECT, msg);
    }

    private void logTriangleChange(ProjectDataType dt) {
        String name = dt.getName();
        int dbId = dt.getDbId();
        String msg = dt.isTriangle()?
                Bundle.MSG_ProjectDataTypeDialog_Changelog_IsVector(name, dbId) :
                Bundle.MSG_ProjectDataTypeDialog_Changelog_IsTriangle(name, dbId);
        ChangeLogUtil.getDefault().addChange(project, ChangeLog.Type.PROJECT, msg);
    }
    
}
