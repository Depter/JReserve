/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.data.dataexplorer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import org.jreserve.data.Data;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.util.DateTableCellRenderer;
import org.jreserve.data.util.DoubleTableCellRenderer;
import org.jreserve.data.util.ProjectDataTypeComboRenderer;
import org.jreserve.data.util.ProjectDataTypeComparator;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementAdapter;
import org.jreserve.project.system.ProjectElementListener;
import org.jreserve.resources.ToolBarButton;
import org.jreserve.resources.textfieldfilters.IntegerFilter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.CopyAction;
import org.openide.actions.DeleteAction;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.data.dataexplorer//DataExplorer//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "DataExplorerTopComponent",
    iconBase = "resources/database.png", 
    persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(
    mode = "explorer", 
    openAtStartup = false
)
@Messages({
    "LBL.DataExplorerTopComponent.NumberOfRows=rows per page:",
    "# {0} - number of rows.",
    "LBL.DataExplorerTopComponent.MaxNumberOfRows={0} records.",
    "# {0} - number of rows",
    "MSG.DataExplorerTopComponent.DeleteQuestion=Delete the selected {0} rows?",
    "# {0} - number of rows",
    "# {1} - id of data type",
    "# {2} - name of data type",
    "MSG.DataExplorerTopComponent.DeleteLogMessage=Deleted {0} datapoints from \"{1} - {2}\"."
})
public final class DataExplorerTopComponent extends TopComponent implements ActionListener {
    
    private final static Logger logger = Logger.getLogger(DataExplorerTopComponent.class.getName());
    
    private final static String DELETE_ACTION_KEY = "delete";
    private final static String DELETE_KEY = "DELETE";
    private final static String FIRST_PAGE = "resources/resultset_first.png";
    private final static String PREVIOUS_PAGE = "resources/resultset_previous.png";
    private final static String NEXT_PAGE = "resources/resultset_next.png";
    private final static String LAST_PAGE = "resources/resultset_last.png";
    private final static int NUMBER_OF_ROWS = 25;

    private final static String DATA_TYPE_ACTION = "DATA_TYPE_ACTION";
    private final static String FIRST_PAGE_ACTION = "FIRST_PAGE_ACTION";
    private final static String PREVIOUS_PAGE_ACTION = "PREVIOUS_PAGE_ACTION";
    private final static String NEXT_PAGE_ACTION = "NEXT_PAGE_ACTION";
    private final static String LAST_PAGE_ACTION = "LAST_PAGE_ACTION";
    private final static String ROW_PER_PAGE_ACTION = "ROW_PER_PAGE_ACTION";
    
    private Project project;
    private ProjectElement element;
    private DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
    private DataExplorerTabelModel tableModel;
    private DateTableCellRenderer dateRenderer;
    private DoubleTableCellRenderer doubleRenderer;
    private ProjectElementListener projectListener;
    
    public DataExplorerTopComponent() {
    }
    
    public DataExplorerTopComponent(ProjectElement<Project> element) {
        this.element = element;
        this.project = element.getValue();
        this.tableModel = new DataExplorerTabelModel(project, NUMBER_OF_ROWS);
        loadDataTypes();
        initComponents();
        setTableRenderers();
        setName(project.getName());
        registerActions();
        initProjectListener();
    }
    
    private void loadDataTypes() {
        List<ProjectDataType> dts = element.getChildValues(ProjectDataType.class);
        Collections.sort(dts, new ProjectDataTypeComparator());
        comboModel.removeAllElements();
        for(ProjectDataType dt : dts)
            comboModel.addElement(dt);
        comboModel.setSelectedItem(null);
    }

    private void setTableRenderers() {
        dateRenderer = new DateTableCellRenderer();
        doubleRenderer = new DoubleTableCellRenderer();
        table.setDefaultRenderer(Date.class, dateRenderer);
        table.setDefaultRenderer(Double.class, doubleRenderer);
    }
    
    private void registerActions() {
        registerCopyAction();
        registerDeleteAction();
    }
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke("control C");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        table.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyDataAction());
    }
    
    private void registerDeleteAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke(DELETE_KEY);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DELETE_ACTION_KEY);
        table.getInputMap().put(stroke, DELETE_ACTION_KEY);
        getActionMap().put(DELETE_ACTION_KEY, new DeleteDataAction());
    }
    
    private void initProjectListener() {
        projectListener = new ProjectListener();
        Class c = ProjectElementListener.class;
        ProjectElementListener weak = WeakListeners.create(c, projectListener, element);
        element.addProjectElementListener(weak);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        dataTypeCombo = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        firstPageButton = new ToolBarButton(ImageUtilities.loadImageIcon(FIRST_PAGE, false));
        prevPageButton = new ToolBarButton(ImageUtilities.loadImageIcon(PREVIOUS_PAGE, false));
        nextPageButton = new ToolBarButton(ImageUtilities.loadImageIcon(NEXT_PAGE, false));
        lastPageButton = new ToolBarButton(ImageUtilities.loadImageIcon(LAST_PAGE, false));
        rowPerPAgeLabel = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        rowPerPageText = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        totalRowsLabel = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator2 = new javax.swing.JToolBar.Separator();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        copyButton = new ToolBarButton(SystemAction.get(CopyAction.class));
        deleteButton = new ToolBarButton(SystemAction.get(DeleteAction.class));
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        dataTypeCombo.setModel(comboModel);
        dataTypeCombo.setSelectedItem(null);
        dataTypeCombo.setActionCommand(DATA_TYPE_ACTION);
        dataTypeCombo.setMaximumSize(new java.awt.Dimension(150, 18));
        dataTypeCombo.setMinimumSize(new java.awt.Dimension(150, 18));
        dataTypeCombo.setPreferredSize(new java.awt.Dimension(150, 18));
        dataTypeCombo.setRenderer(new ProjectDataTypeComboRenderer());
        dataTypeCombo.addActionListener(this);
        jToolBar1.add(dataTypeCombo);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(firstPageButton, null);
        firstPageButton.setActionCommand(FIRST_PAGE_ACTION);
        firstPageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        firstPageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        firstPageButton.addActionListener(this);
        jToolBar1.add(firstPageButton);

        org.openide.awt.Mnemonics.setLocalizedText(prevPageButton, null);
        prevPageButton.setActionCommand(PREVIOUS_PAGE_ACTION);
        prevPageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        prevPageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        prevPageButton.addActionListener(this);
        jToolBar1.add(prevPageButton);

        org.openide.awt.Mnemonics.setLocalizedText(nextPageButton, null);
        nextPageButton.setActionCommand(NEXT_PAGE_ACTION);
        nextPageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextPageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextPageButton.addActionListener(this);
        jToolBar1.add(nextPageButton);

        org.openide.awt.Mnemonics.setLocalizedText(lastPageButton, null);
        lastPageButton.setActionCommand(LAST_PAGE_ACTION);
        lastPageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lastPageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lastPageButton.addActionListener(this);
        jToolBar1.add(lastPageButton);

        org.openide.awt.Mnemonics.setLocalizedText(rowPerPAgeLabel, Bundle.LBL_DataExplorerTopComponent_NumberOfRows());
        jToolBar1.add(rowPerPAgeLabel);
        jToolBar1.add(filler2);

        rowPerPageText.setColumns(3);
        rowPerPageText.setDocument(new IntegerFilter());
        rowPerPageText.setText(""+NUMBER_OF_ROWS);
        rowPerPageText.setMaximumSize(new java.awt.Dimension(30, 18));
        rowPerPageText.setMinimumSize(new java.awt.Dimension(30, 18));
        rowPerPageText.setPreferredSize(new java.awt.Dimension(30, 18));
        rowPerPageText.setActionCommand(ROW_PER_PAGE_ACTION);
        rowPerPageText.addActionListener(this);
        jToolBar1.add(rowPerPageText);
        jToolBar1.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(totalRowsLabel, Bundle.LBL_DataExplorerTopComponent_MaxNumberOfRows(0));
        jToolBar1.add(totalRowsLabel);
        jToolBar1.add(filler3);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(filler4);

        org.openide.awt.Mnemonics.setLocalizedText(copyButton, null);
        copyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(copyButton);

        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, null);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(deleteButton);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        table.setModel(tableModel);
        tableScroll.setViewportView(table);

        add(tableScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jreserve.resources.ToolBarButton copyButton;
    private javax.swing.JComboBox dataTypeCombo;
    private org.jreserve.resources.ToolBarButton deleteButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private org.jreserve.resources.ToolBarButton firstPageButton;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private org.jreserve.resources.ToolBarButton lastPageButton;
    private org.jreserve.resources.ToolBarButton nextPageButton;
    private org.jreserve.resources.ToolBarButton prevPageButton;
    private javax.swing.JLabel rowPerPAgeLabel;
    private javax.swing.JTextField rowPerPageText;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel totalRowsLabel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        DataExplorerRegistry.removeComponent(project);
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        if(DATA_TYPE_ACTION.equals(command)) {
            tableModel.setDataType((ProjectDataType) dataTypeCombo.getSelectedItem());
            tableModel.setRowsPerPage(getRowPerPage());
            int rowCount = tableModel.getTotalRowCount();
            totalRowsLabel.setText(Bundle.LBL_DataExplorerTopComponent_MaxNumberOfRows(rowCount));
        } else if(FIRST_PAGE_ACTION.equals(command)) {
            tableModel.setPageIndex(0);
        } else if(LAST_PAGE_ACTION.equals(command)) {
            tableModel.setPageIndex(tableModel.getPageCount());
        } else if(NEXT_PAGE_ACTION.equals(command)) {
            int pageIndex = tableModel.getPageIndex();
            tableModel.setPageIndex(pageIndex + 1);
        } else if(PREVIOUS_PAGE_ACTION.equals(command)) {
            int pageIndex = tableModel.getPageIndex();
            tableModel.setPageIndex(pageIndex - 1);
        } else if(ROW_PER_PAGE_ACTION.equals(command)) {
            tableModel.setRowsPerPage(getRowPerPage());
        }
    }
    
    private int getRowPerPage() {
        String str = rowPerPageText.getText();
        if(str==null || str.length()==0)
            return NUMBER_OF_ROWS;
        return Integer.parseInt(str);
    }
    
    
    private List<Data> getSelectedTableData() {
        int rows[] = table.getSelectedRows();
        if(rows.length > 0)
            return tableModel.getSelectedData(rows);
        return tableModel.getAllDataOnPage();
    }
    
    private class CopyDataAction extends AbstractAction {

        private StringBuilder sb = new StringBuilder();
        private DateFormat dateFormat;
        private char decimalSep;
        private boolean isTriangle;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            initState();
            StringSelection text = new StringSelection(getText());
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
    
        private void initState() {
            dateFormat = dateRenderer.getFormat();
            decimalSep = doubleRenderer.getFormat().getDecimalFormatSymbols().getDecimalSeparator();
            sb.setLength(0);
            ProjectDataType dt = (ProjectDataType) dataTypeCombo.getSelectedItem();
            isTriangle = (dt==null)? false : dt.isTriangle();
        }
        
        private String getText() {
            for(Data data : getSelectedTableData())
                appendData(data);
            return sb.toString();
        }
        
        private void appendData(Data data) {
            if(sb.length() != 0)
                sb.append("\n");
            sb.append(dateFormat.format(data.getAccidentDate())).append("\t");
            if(isTriangle)
                sb.append(dateFormat.format(data.getDevelopmentDate())).append("\t");
            sb.append(getValue(data, decimalSep));
        }
        
        private String getValue(Data data, char decimalSep) {
            String str = ""+data.getValue();
            return str.replace('.', decimalSep);
        }
    }

    private class DeleteDataAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Data> datas = getSelectedTableData();
            if(!datas.isEmpty() && confirmUser(datas.size()))
                deleteData(datas);
        }
        
        private boolean confirmUser(int count) {
            String msg = Bundle.MSG_DataExplorerTopComponent_DeleteQuestion(count);
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.OK_CANCEL_OPTION);
            return DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION;
        }
        
        private void deleteData(List<Data> datas) {
            DataSource ds = new DataSource();
            ProjectDataType dt = (ProjectDataType) dataTypeCombo.getSelectedItem();
            try {
                ds.open();
                ds.deleteData(datas);
                logDelete(datas.size(), dt);
                ds.commit();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, String.format("Unable to delete data for '%s'/'%s'", project, dt), ex);
                ds.rollBack();
            }
            tableModel.setDataType(dt);
        }
        
        private void logDelete(int count, ProjectDataType dt) {
            String msg = Bundle.MSG_DataExplorerTopComponent_DeleteLogMessage(count, dt.getDbId(), dt.getName());
            ChangeLogUtil.getDefault().addChange(project, ChangeLog.Type.PROJECT, msg);
            ChangeLogUtil.getDefault().saveValues(project);
        }
    }

    private class ProjectListener extends ProjectElementAdapter {
        @Override
        public void removedFromParent(ProjectElement parent) {
            DataExplorerTopComponent.this.close();
        }

        @Override
        public void childRemoved(ProjectElement child) {
            Object value = child.getValue();
            if(value instanceof ProjectDataType) {
                ProjectDataType dt = (ProjectDataType) child.getValue();
                ProjectDataType selected = getSelectedDataType();
                comboModel.removeElement(value);
                if(dt == selected)
                    comboModel.setSelectedItem(null);
            }
        }

        private ProjectDataType getSelectedDataType() {
            return (ProjectDataType) dataTypeCombo.getSelectedItem();
        }
        
        @Override
        public void childAdded(ProjectElement child) {
            Object value = child.getValue();
            if(value instanceof ProjectDataType)
                reloadDataTypes();
        }
        
        private void reloadDataTypes() {
            ProjectDataType selected = (ProjectDataType) dataTypeCombo.getSelectedItem();
            loadDataTypes();
            dataTypeCombo.setSelectedItem(selected);
        }
    }
}
