package org.jreserve.rutil;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.util.FunctionRegistry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RCode {

    private final static Comparator<RFunction> FUNCTION_COMPARATOR = new Comparator<RFunction>(){
        @Override
        public int compare(RFunction o1, RFunction o2) {
            if(o1 == null) return o2==null? 0 : 1;
            if(o2 == null) return -1;
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    private boolean manualEvents = false;
    private boolean clearsWorkspace = true;
    private Set<String> libraries = new TreeSet<String>();
    private Set<RFunction> functions = new TreeSet<RFunction>(FUNCTION_COMPARATOR);
    private StringBuilder source = new StringBuilder();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public RCode() {
    }
    
    public RCode(boolean manualEvents) {
        this.manualEvents = manualEvents;
    }
    
    public boolean isManualEvents() {
        return manualEvents;
    }
    
    public void setManualEvents(boolean manualEvents) {
        this.manualEvents = manualEvents;
    }
    
    public RCode addLibrary(String library) {
        this.libraries.add(library);
        fireAutomaticChangeEvent();
        return this;
    }
    
    public RCode addFunction(RFunction function) {
        if(!functions.contains(function)) {
            this.libraries.addAll(function.getLibraryDependendencies());
            for(String name : function.getFunctionDependendencies())
                addFunction(name);
            functions.add(function);
            fireAutomaticChangeEvent();
        }
        return this;
    }
    
    public RCode addFunction(String name) {
        RFunction function = FunctionRegistry.getFunction(name);
        addFunction(function);
        return this;
    }
    
    public RCode addSource(String str) {
        source.append(str);
        fireAutomaticChangeEvent();
        return this;
    }
    
    public RCode clear() {
        this.functions.clear();
        this.libraries.clear();
        this.source.setLength(0);
        fireAutomaticChangeEvent();
        return this;
    }
    
    public boolean getClearsWorkspace() {
        return clearsWorkspace;
    }
    
    public void setClearsWorkspace(boolean clearsWorkspace) {
        this.clearsWorkspace = clearsWorkspace;
        fireAutomaticChangeEvent();
    }
    
    public String toRCode() {
        StringBuilder r = new StringBuilder();
        appendLibraries(r);
        appendFunctions(r);
        appendCode(r);
        if(clearsWorkspace)
            r.insert(0, "rm(list=ls())\n\n");
        return r.toString();
    }
    
    private void appendLibraries(StringBuilder r) {
        if(!libraries.isEmpty()) {
            r.append("##################")
             .append("###  LIBRARIES ###")
             .append("##################")
             .append("\n\n");
            for(String library : libraries)
                r.append("library(").append(library).append(")\n");
        }
    }
    
    private void appendFunctions(StringBuilder r) {
        if(!functions.isEmpty()) {
            r.append("##################")
             .append("###  FUNCTIONS ###")
             .append("##################")
             .append("\n\n");
            for(RFunction function : functions)
                r.append(function.getSource()).append("\n");
        }
    }
    
    private void appendCode(StringBuilder r) {
        if(source.length() > 0) {
            if(r.length() > 0)
                r.append("##################")
                 .append("#####  CODE  #####")
                 .append("##################")
             .append("\n\n");
            r.append(source);
        }   
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireAutomaticChangeEvent() {
        if(!manualEvents)
            fireChangeEvent();
    }
    
    public void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
}
