package org.jreserve.triangle.visual.widget.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableCellRenderer;
import org.jreserve.resources.annotation.RegistrationRegistry;
import static org.jreserve.triangle.visual.widget.util.WidgetRendererRegistrationProcessor.ENTITY_DIRECTORY;
import static org.jreserve.triangle.visual.widget.util.WidgetRendererRegistrationProcessor.LAYER_ID;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetRendererRegistry extends RegistrationRegistry<TableCellRenderer, TableCellRenderer> {
    
    private final static Logger logger = Logger.getLogger(WidgetRendererRegistry.class.getName());
    
    private final static Comparator<FileObject> FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };

    private static WidgetRendererRegistry INSTANCE;
    
    public static TableCellRenderer getRenderer(String layerId) {
        if(INSTANCE == null) {
            INSTANCE = new WidgetRendererRegistry();
            INSTANCE.getValues();
        }
        return INSTANCE.renderers.get(layerId);
    }
    
    private Map<String, TableCellRenderer> renderers = new HashMap<String, TableCellRenderer>();
    
    @Override
    protected String getDirectory() {
        return ENTITY_DIRECTORY;
    }

    @Override
    protected Comparator<FileObject> getFileComparator() {
        return FILE_COMPARATOR;
    }

    @Override
    protected TableCellRenderer getValue(TableCellRenderer instance, FileObject file) {
        loadValue(instance, file);
        return instance;
    }
    
    private void loadValue(TableCellRenderer instance, FileObject file) {
        String layerId = (String) file.getAttribute(LAYER_ID);
        if(layerId == null || layerId.trim().length()==0) {
            logger.log(Level.SEVERE, "Unable to load TriangleCellRenderer from file \"{0}\"! Illegal layer id: {1}", new Object[]{file, layerId});
            return;
        }
        
        if(renderers.containsKey(layerId)) {
            logger.log(Level.SEVERE, "Layer id \"{0}\" used more then once! Only first occurence will be used.", layerId);
        } else {
            renderers.put(layerId, instance);
        }
    }
}
