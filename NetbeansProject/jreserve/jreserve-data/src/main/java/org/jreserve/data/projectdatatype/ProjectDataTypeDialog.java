package org.jreserve.data.projectdatatype;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.datatypesetting.DTDummy;
import org.jreserve.data.datatypesetting.DataTypePanel;
import org.jreserve.data.util.ProjectDataTypeComparator;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.Deletable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ProjectDataTypeDialog.Title=Claim Data Types",
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Deleted=Data type \"{0} ({1})\" deleted.",
    "# {0} - name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Created=Data type \"{0} ({1})\" created.",
    "# {0} - old name of data type",
    "# {1} - dbId of data type",
    "MSG.ProjectDataTypeDialog.Changelog.Changed=Data type \"{0} ({1})\" was modified."
})
class ProjectDataTypeDialog extends JPanel implements PropertyChangeListener, ActionListener {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeDialog.class.getName());
    
    private final static boolean IS_MODAL = true;
    
    static void showDialog(ProjectElement<ClaimType> element) {
        ProjectDataTypeDialog content = new ProjectDataTypeDialog(element);
        DialogDescriptor dd = createDescriptor(content);
        dd.setOptions(new Object[0]);
        content.dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.dialog.pack();
        content.dialog.setVisible(true);
    }
    
    private static DialogDescriptor createDescriptor(ProjectDataTypeDialog content) {
        return new DialogDescriptor(
            content,
            Bundle.LBL_ProjectDataTypeDialog_Title(),
            IS_MODAL,
            null, null,
            DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
    }
    
    private Dialog dialog;
    private ClaimType claimType;
    private ProjectElement<ClaimType> element;
    private DataTypePanel dtPanel;
            
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("Cancel");
    
    private List<ProjectDataType> originalTypes;
    
    private ProjectDataTypeDialog(ProjectElement<ClaimType> element) {
        this.element = element;
        this.claimType = element.getValue();
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
        originalTypes = element.getChildValues(ProjectDataType.class);
        Collections.sort(originalTypes, new ProjectDataTypeComparator());
        return originalTypes;
    }
    
    private DTDummy getDummy(ProjectDataType dt) {
        int dbId = dt.getDbId();
        String name = dt.getName();
        boolean isTriangle = dt.isTriangle();
        return new DTDummy(dbId, name, isTriangle);
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
        } else if(okButton == source) {
            store();
            dialog.dispose();
        }
    }

    
    void store() {
        List<ProjectDataType> deleted = getDeleted();
        List<ProjectDataType> updated = updateList();
        if(!deleted.isEmpty() || !updated.isEmpty())
            new Persister(deleted, updated).save();
    }
    
    private List<ProjectDataType> getDeleted() {
        List<ProjectDataType> deleted = new ArrayList<ProjectDataType>();
        for(ProjectDataType dt : originalTypes)
            if(dtPanel.getDummy(dt.getDbId()) == null)
                deleted.add(dt);
        return deleted;
    }
    
    private List<ProjectDataType> updateList() {
        List<ProjectDataType> updated = new ArrayList<ProjectDataType>();
        int rowCount = dtPanel.getDummyCount();
        for(int r=0; r<rowCount; r++)
            updateRow(updated, r);
        return updated;
    }
    
    private void updateRow(List<ProjectDataType> updated, int row) {
        DTDummy dummy = dtPanel.getDummyAtRow(row);
        ProjectDataType dt = getOriginalProjectDataType(dummy.getId());
        if(dt == null) {
            updated.add(createDataType(dummy));
        } else {
            if(updateDataType(dt, dummy))
                updated.add(dt);
        }
    }
    
    private ProjectDataType getOriginalProjectDataType(int dbId) {
        for(ProjectDataType dt : originalTypes)
            if(dt.getDbId() == dbId)
                return dt;
        return null;
    }
    
    private ProjectDataType createDataType(DTDummy dummy) {
        int id = dummy.getId();
        String name = dummy.getName();
        boolean isTriangle = dummy.isTriangle();
        return new ProjectDataType(claimType, id, name, isTriangle);
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
    
    private class Persister {
        
        private List<ProjectDataType> deleted;
        private List<ProjectDataType> updated;
        private List<Project> projects;
        private Session session;
        
        private Persister(List<ProjectDataType> deleted, List<ProjectDataType> updated) {
            this.deleted = deleted;
            this.updated = updated;
        }
        
        private void save() {
            try {
                initialize();
                delete();
                update();
                session.comitTransaction();
                saveLogs();
            } catch (RuntimeException ex) {
                rollBack(ex);
                logger.log(Level.SEVERE, "Unable to update ProjectDataTypes for ClaimType: "+claimType.getName(), ex);
            }
        }
        
        private void initialize() {
            session = SessionFactory.beginTransaction();
            ClaimType ct = session.find(ClaimType.class, claimType.getId());
            projects = ct.getProjects();
        }
        
        private void delete() {
            for(ProjectDataType dt : deleted)
                deleteDataType(dt);
        }
        
        private void deleteDataType(ProjectDataType dt) {
            ProjectElement e = element.getChild(dt);
            Deletable d = e.getLookup().lookup(Deletable.class);
            d.delete(session);
            logDeletion(dt);
        }
        
        private void logDeletion(ProjectDataType dt) {
            String name = dt.getName();
            int dbId = dt.getDbId();
            String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Deleted(name, dbId);
            makeProjectLog(msg);
        }
        
        private void makeProjectLog(String msg) {
            ChangeLogUtil util = ChangeLogUtil.getDefault();
            for(Project project : projects)
                util.addChange(project, ChangeLog.Type.PROJECT, msg);
        }
        
        private void update() {
            for(ProjectDataType dt : updated) {
                if(dt.getVersion() == null) {
                    create(dt);
                } else {
                    update(dt);
                }
            }
        }
        
        private void create(ProjectDataType dt) {
            session.persist(dt);
            ProjectElement e = new ProjectDatTypeProjectElement(dt);
            element.addChild(e);
            logCreation(dt);
        }

        private void logCreation(ProjectDataType dt) {
            String name = dt.getName();
            int dbId = dt.getDbId();
            String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Created(name, dbId);
            makeProjectLog(msg);
        }
        
        private void update(ProjectDataType dt) {
            session.update(dt);
            logChange(dt);
        }
        
        private void logChange(ProjectDataType dt) {
            String oldName = dt.getName();
            int dbId = dt.getDbId();
            String msg = Bundle.MSG_ProjectDataTypeDialog_Changelog_Changed(oldName, dbId);
            makeProjectLog(msg);
        }

        private void saveLogs() {
            try {
                ChangeLogUtil util = ChangeLogUtil.getDefault();
                for(Project project : projects)
                    util.saveValues(project);
            } catch (RuntimeException ex) {
                logger.log(Level.SEVERE, "Unable to save project logs!", ex);
                Exceptions.printStackTrace(ex);
            }
        }
        
        private void rollBack(Exception ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to update ProjectDataTypes in the database!", ex);
            Exceptions.printStackTrace(ex);
        }
    }
}
