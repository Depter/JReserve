package org.jreserve.rutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.util.FunctionRegistry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RCode {

    private Set<String> libraries = new TreeSet<String>();
    private Set<RFunction> functions = new TreeSet<RFunction>();
    private StringBuilder source = new StringBuilder();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public RCode addLibrary(String library) {
        this.libraries.add(library);
        fireChangeEvent();
        return this;
    }
    
    public RCode addFunction(RFunction function) {
        if(!functions.contains(function)) {
            this.libraries.addAll(function.getLibraryDependendencies());
            for(String name : function.getFunctionDependendencies())
                addFunction(name);
            fireChangeEvent();
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
        fireChangeEvent();
        return this;
    }
    
    public RCode clear() {
        this.functions.clear();
        this.libraries.clear();
        this.source.setLength(0);
        fireChangeEvent();
        return this;
    }
    
    public String toRCode() {
        StringBuilder r = new StringBuilder();
        appendLibraries(r);
        appendFunctions(r);
        appendCode(r);
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
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
