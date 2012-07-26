package org.jreserve.resources;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileChooserBuilder.BadgeProvider;
import org.openide.filesystems.FileChooserBuilder.SelectionApprover;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL_allFileFilter=All files",
    "CTL_lookInLabel=Look in:",
    "CTL_cancelButton=Cancel",
    "CTL_cancelButtonToolTip=Cancel",
    "CTL_openButton=Open",
    "CTL_openButtonToolTip=Open",
    "CTL_filesOfTypeLabel=File types:",
    "CTL_fileNameLabel=Name:",
    "CTL_listViewButtonToolTip=Show files as list",
    "CTL_listViewButtonAccessibleName=ListView",
    "CTL_detailsViewButtonToolTip=Show details",
    "CTL_detailsViewButtonAccessibleName=DetailsView",
    "CTL_upFolderViewButtonToolTip=Show folders",
    "CTL_upFolderViewButtonAccessibleName=ShowFolders",
    "CTL_homeFolderToolTip=Home",
    "CTL_homeFolderAccesibleKey=HomeDirectory",
    "CTL_fileNameHader=Name",
    "CTL_fileSizeHeader=Size",
    "CTL_fileTypeHeader=Type",
    "CTL_fileDateHeader=Date",
    "CTL_fileAttrHeader=Attributes",
    "CTL_openDialogTitle=Open",
    "CTL_saveDialogTitle=Save"
})
public class FileChooser {

    public final static String ALL_FILE_FILTER = "FileChooser.acceptAllFileFilterText";
    public final static String LOOK_IN_LABEL = "FileChooser.lookInLabelText";
    public final static String CANCEL_BUTTON = "FileChooser.cancelButtonText";
    public final static String CANCEL_BUTTON_TOOLTIP = "FileChooser.cancelButtonToolTipText";
    public final static String OPEN_BUTTON = "FileChooser.openButtonText";
    public final static String OPEN_BUTTON_TOOLTIP = "FileChooser.openButtonToolTipText";
    public final static String FILES_OF_TYPE_LABEL = "FileChooser.filesOfTypeLabelText";
    public final static String FILE_NAME_LABEL = "FileChooser.fileNameLabelText";
    public final static String LIST_VIEW_BUTTON_TOOLTIP = "FileChooser.listViewButtonToolTipText";
    public final static String LIST_VIEW_BUTTON_ACCESSIBLE_NAME = "FileChooser.listViewButtonAccessibleName";
    public final static String DETAILS_VIEW_BUTTON_TOOLTIP = "FileChooser.detailsViewButtonToolTipText";
    public final static String DETAILS_VIEW_BUTTON_ACCESSIBLE_NAME = "FileChooser.detailsViewButtonAccessibleName";
    public final static String UP_FOLDER_TOOLTIP = "FileChooser.upFolderToolTipText";
    public final static String UP_FOLDER_ACCESSIBLE_NAME = "FileChooser.upFolderAccessibleName";
    public final static String HOME_FOLDER_TOOLTIP = "FileChooser.homeFolderToolTipText";
    public final static String HOME_FOLDER_ACCESSIBLE_NAME = "FileChooser.homeFolderAccessibleName";
    public final static String FILE_NAME_HEADER = "FileChooser.fileNameHeaderText";
    public final static String FILE_SIZE_HEADER = "FileChooser.fileSizeHeaderText";
    public final static String FILE_TYPE_HEADER = "FileChooser.fileTypeHeaderText";
    public final static String FILE_DATE_HEADER = "FileChooser.fileDateHeaderText";
    public final static String FILE_ATTR_HEADER = "FileChooser.fileAttrHeaderText";
    public final static String OPEN_DIALOG_TITLE = "FileChooser.openDialogTitleText";
    public final static String SAVE_DIALOG_TITLE = "FileChooser.saveDialogTitleText";
    public final static String READ_ONLY = "FileChooser.readOnly";
    
    private static boolean initialized = false;
    
    private static void initialize() {
        if(initialized)
            return;
        UIManager.put(ALL_FILE_FILTER, Bundle.CTL_allFileFilter());
        UIManager.put(LOOK_IN_LABEL, Bundle.CTL_lookInLabel());
        UIManager.put(CANCEL_BUTTON, Bundle.CTL_cancelButton());
        UIManager.put(CANCEL_BUTTON_TOOLTIP, Bundle.CTL_cancelButtonToolTip());
        UIManager.put(OPEN_BUTTON, Bundle.CTL_openButton());
        UIManager.put(OPEN_BUTTON_TOOLTIP, Bundle.CTL_openButtonToolTip());
        UIManager.put(FILES_OF_TYPE_LABEL, Bundle.CTL_filesOfTypeLabel());
        UIManager.put(FILE_NAME_LABEL, Bundle.CTL_fileNameLabel());
        UIManager.put(LIST_VIEW_BUTTON_TOOLTIP, Bundle.CTL_listViewButtonToolTip());
        UIManager.put(LIST_VIEW_BUTTON_ACCESSIBLE_NAME, Bundle.CTL_listViewButtonAccessibleName());
        UIManager.put(DETAILS_VIEW_BUTTON_TOOLTIP, Bundle.CTL_detailsViewButtonToolTip());
        UIManager.put(DETAILS_VIEW_BUTTON_ACCESSIBLE_NAME, Bundle.CTL_detailsViewButtonAccessibleName());
        UIManager.put(UP_FOLDER_TOOLTIP, Bundle.CTL_upFolderViewButtonToolTip());
        UIManager.put(UP_FOLDER_ACCESSIBLE_NAME, Bundle.CTL_upFolderViewButtonAccessibleName());
        UIManager.put(HOME_FOLDER_TOOLTIP, Bundle.CTL_homeFolderToolTip());
        UIManager.put(HOME_FOLDER_ACCESSIBLE_NAME, Bundle.CTL_homeFolderAccesibleKey());
        UIManager.put(FILE_NAME_HEADER, Bundle.CTL_fileNameHader());
        UIManager.put(FILE_SIZE_HEADER, Bundle.CTL_fileSizeHeader());
        UIManager.put(FILE_TYPE_HEADER, Bundle.CTL_fileTypeHeader());
        UIManager.put(FILE_DATE_HEADER, Bundle.CTL_fileDateHeader());
        UIManager.put(FILE_ATTR_HEADER, Bundle.CTL_fileAttrHeader());
        UIManager.put(OPEN_DIALOG_TITLE, Bundle.CTL_openDialogTitle());
        UIManager.put(SAVE_DIALOG_TITLE, Bundle.CTL_saveDialogTitle());
        initialized = true;
    }
    
    private FileChooserBuilder builder;
    
    public FileChooser(Class type) {
        initialize();
        builder = new FileChooserBuilder(type);
    }
    
    public FileChooser(String dirKey) {
        initialize();
        builder = new FileChooserBuilder(dirKey);
    }

    public FileChooser addFileFilter(FileFilter filter) {
        builder.addFileFilter(filter);
        return this;
    }

    public FileChooser forceUseOfDefaultWorkingDirectory(boolean val) {
        builder.forceUseOfDefaultWorkingDirectory(val);
        return this;
    }

    public FileChooser setAccessibleDescription(String aDescription) {
        builder.setAccessibleDescription(aDescription);
        return this;
    }

    public FileChooser setApproveText(String val) {
        builder.setApproveText(val);
        return this;
    }

    public FileChooser setBadgeProvider(BadgeProvider provider) {
        builder.setBadgeProvider(provider);
        return this;
    }

    public FileChooser setControlButtonsAreShown(boolean val) {
        builder.setControlButtonsAreShown(val);
        return this;
    }

    public FileChooser setDefaultWorkingDirectory(File dir) {
        builder.setDefaultWorkingDirectory(dir);
        return this;
    }

    public FileChooser setDirectoriesOnly(boolean val) {
        builder.setDirectoriesOnly(val);
        return this;
    }

    public FileChooser setFileFilter(FileFilter filter) {
        builder.setFileFilter(filter);
        return this;
    }

    public FileChooser setFileHiding(boolean fileHiding) {
        builder.setFileHiding(fileHiding);
        return this;
    }

    public FileChooser setFilesOnly(boolean val) {
        builder.setFilesOnly(val);
        return this;
    }

    public FileChooser setSelectionApprover(SelectionApprover approver) {
        builder.setSelectionApprover(approver);
        return this;
    }

    public FileChooser setTitle(String val) {
        builder.setTitle(val);
        return this;
    }

    public JFileChooser createFileChooser() {
        JFileChooser chooser = builder.createFileChooser();
        centerDialog(chooser);
        return chooser;
    }
    
    private void centerDialog(JFileChooser chooser) {
        Frame main = WindowManager.getDefault().getMainWindow();
        Point mainLocation = main.getLocation();
        Dimension mainSize = main.getSize();
        
        Dimension mySize  = chooser.getSize();
        int x = mainLocation.x + (mainSize.width - mySize.width) / 2 ;
        int y = mainLocation.y + (mainSize.height - mySize.height) / 2 ;
        chooser.setLocation(x, y);
    }
    
    public File[] showMultiOpenDialog() {
        return builder.showMultiOpenDialog();
    }

    public File showOpenDialog() {
        return builder.showOpenDialog();
    }

    public File showSaveDialog() {
        return builder.showSaveDialog();
    }
}
