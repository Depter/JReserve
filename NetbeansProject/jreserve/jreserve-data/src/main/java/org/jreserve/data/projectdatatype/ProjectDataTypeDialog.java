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
import org.jreserve.project.entities.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectDataTypeDialog extends JPanel implements PropertyChangeListener, ActionListener {
    
    private final static boolean IS_MODAL = true;
    
    static void showDialog(Project project) {
        ProjectDataTypeDialog content = new ProjectDataTypeDialog(project);
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
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
        if(removeDeleted() | updateList())
            ProjectDataTypeUtil.getDefault().saveValues(project);
    }
    
    private boolean removeDeleted() {
        boolean deleted = false;
        for(ProjectDataType dt : originalTypes)
            if(dtPanel.getDummy(dt.getDbId()) == null) {
                deleted = true;
                ProjectDataTypeUtil.getDefault().deleteValue(project, dt);
            }
        return deleted;
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
        ProjectDataTypeUtil.getDefault().addValue(project, dt);
    }
    
    private boolean updateDataType(ProjectDataType dt, DTDummy dummy) {
        boolean updated = false;
        if(!dt.getName().equals(dummy.getName())) {
            updated = true;
            dt.setName(dummy.getName());
        }
        if(dt.isTriangle() != dummy.isTriangle()) {
            updated = true;
            dt.setTriangle(dummy.isTriangle());
        }
        return updated;
    }

}
