package org.jreserve.resources;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/**
 * The main purpose of this class is to provide similar functionality as of
 * <i>org.openide.util.Utilities.actionsForPath()</i> but with multiple path
 * elements.
 * 
 * <p> The user can provide more path elements and the actions are loaded
 * from the given directories and sub directories. This class does not log
 * warning in the casees where the found FileObject does not represent an 
 * action or a JSeparator.
 * </p>
 * 
 * @author Peter Decsi
 */
public class ActionUtil {

    private final static Logger logger = Logger.getLogger(ActionUtil.class.getName());
    private final static Comparator<FileObject> FILE_COMPARATOR = new FileComparator();
    
    public static List<? extends Action> actionsForPath(String... pathes) {
        return actionsForPath(Arrays.asList(pathes));
    }
    
    public static List<? extends Action> actionsForPath(Collection<String> pathes) {
        List<Action> actions = new ArrayList<Action>();
        for (DataObject obj : getObjects(pathes))
            addAction(actions, obj.getLookup());
        return actions;
    }
    
    private static List<DataObject> getObjects(Collection<String> pathes) {
        List<FileObject> files = getFiles(pathes);
        return getDataObjects(files);
    }

    private static List<FileObject> getFiles(Collection<String> pathes) {
        List<FileObject> files = new ArrayList<FileObject>();
        for(String path : pathes)
            addFiles(files, FileUtil.getConfigFile(path));
        Collections.sort(files, FILE_COMPARATOR);
        return files;
    }
    
    private static void addFiles(List<FileObject> files, FileObject root) {
        if(root == null) {
        } else if(root.isFolder()) {
            for(FileObject file : root.getChildren())
                addFiles(files, file);
        } else {
            files.add(root);
        }
    }
    
    private static List<DataObject> getDataObjects(List<FileObject> files) {
        List<DataObject> datas = new ArrayList<DataObject>(files.size());
        for(FileObject file : files) {
            try {datas.add(DataObject.find(file));}
            catch (DataObjectNotFoundException ex) {}
        }
        return datas;
    }
    
    private static void addAction(List<Action> actions, Lookup lkp) {
        InstanceCookie cookie = lkp.lookup(InstanceCookie.class);
        if(cookie == null)
            return;
        addAction(actions, cookie);
    }
    
    private static void addAction(List<Action> actions, InstanceCookie cookie) {
        try {
            Object o = cookie.instanceCreate();
            if(o instanceof Action) { 
                actions.add((Action) o);
            } else if (o instanceof JSeparator) {
                actions.add(null);
            }
        } catch (Exception ex) {
            String msg = String.format("Unable to instantiate '%s'!", cookie.instanceName());
            logger.log(Level.WARNING, msg, ex);
        }
    }
    
    public static JPopupMenu createPopupForPath(String... path) {
        List<? extends Action> actions = actionsForPath(path);
        if(actions.isEmpty())
            return null;
        return createPopup(actions);
    }
    
    private static JPopupMenu createPopup(List<? extends Action> actions) {
        JPopupMenu popUp = new JPopupMenu();
        for(Action action : actions)
            addAction(popUp, action);
        return popUp;
    }
    
    private static void addAction(JPopupMenu menu, Action action) {
        if(action == null) {
            if(menu.getComponentCount() > 0)
                menu.add(new JSeparator());
        } else if(action instanceof Presenter.Popup) {
            menu.add(((Presenter.Popup)action).getPopupPresenter());
        } else {
            menu.add(action);
        }
    }
    
    private static class FileComparator implements Comparator<FileObject> {

        @Override
        public int compare(FileObject o1, FileObject o2) {
            Integer p1 = (Integer) o1.getAttribute("position");
            Integer p2 = (Integer) o2.getAttribute("position");
            int dif = compare(p1, p2);
            return dif != 0? dif : o1.getName().compareTo(o2.getName());
        }
    
        private int compare(Integer p1, Integer p2) {
            if(p1 == null)
                return p2==null? 0 : 1;
            return p2 == null? -1 : p1 - p2;
        }
    }
}
