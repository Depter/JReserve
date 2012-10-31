package org.jreserve.project.system.management;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jreserve.project.system.ProjectElement;
import org.openide.awt.UndoRedo;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - property name",
    "MSG.ProjectElementUndoRedo.Undo=Undo: {0}",
    "# {0} - property name",
    "MSG.ProjectElementUndoRedo.Redo=Redo: {0}",
    "MSG.ProjectElementUndoRedo.Change.Name=name change",
    "MSG.ProjectElementUndoRedo.Change.Description=description change"
})
public class ProjectElementUndoRedo implements PropertyChangeListener, UndoRedo {

    private ProjectElement element;
    private List<Event> events = new ArrayList<Event>();
    private int eventIndex = -1;
    private boolean myChange = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public ProjectElementUndoRedo(ProjectElement element) {
        this.element = element;
        this.element.addPropertyChangeListener(this);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(!myChange)
            addChange(evt);
    }
    
    private void addChange(PropertyChangeEvent evt) {
        clearAfterEventIndex();
        events.add(new Event(evt));
        eventIndex++;
        fireChange();
    }
    
    private void clearAfterEventIndex() {
        if(eventIndex < 0)
            return;
        for(int i=events.size()-1; i>eventIndex; i--)
            events.remove(i);
    }

    @Override
    public boolean canUndo() {
        return eventIndex >= 0;
    }

    @Override
    public boolean canRedo() {
        return eventIndex >= 0 &&
               eventIndex < events.size()-1;
    }

    @Override
    public void undo() throws CannotUndoException {
        Event evt = events.get(eventIndex);
        undo(evt);
        eventIndex--;
        fireChange();
    }
    
    private void undo(Event evt) {
        myChange = true;
        element.setProperty(evt.property, evt.oldValue);
        myChange = false;
    }

    @Override
    public void redo() throws CannotRedoException {
        Event evt = events.get(eventIndex);
        redo(evt);
        eventIndex++;        
        fireChange();
    }
    
    private void redo(Event evt) {
        myChange = true;
        element.setProperty(evt.property, evt.newValue);
        myChange = false;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }

    @Override
    public String getUndoPresentationName() {
        if(eventIndex < 0)
            return null;
        Event evt = events.get(eventIndex);
        String propName = getPropertyName(evt.property);
        return Bundle.MSG_ProjectElementUndoRedo_Undo(propName);
    }
    
    protected String getPropertyName(String property) {
        if(ProjectElement.NAME_PROPERTY.equals(property))
            return Bundle.MSG_ProjectElementUndoRedo_Change_Name();
        if(ProjectElement.DESCRIPTION_PROPERTY.equals(property))
            return Bundle.MSG_ProjectElementUndoRedo_Change_Description();
        return property;
    }

    @Override
    public String getRedoPresentationName() {
        if(eventIndex < 0)
            return null;
        Event evt = events.get(eventIndex);
        String propName = getPropertyName(evt.property);
        return Bundle.MSG_ProjectElementUndoRedo_Redo(propName);
    }

    public void clear() {
        events.clear();
        eventIndex = -1;
        fireChange();
    }
    
    private static class Event {
    
        private String property;
        private Object oldValue;
        private Object newValue;
        
        private Event(PropertyChangeEvent evt) {
            this.property = evt.getPropertyName();
            this.oldValue = evt.getOldValue();
            this.newValue = evt.getNewValue();
        }
    }
}
