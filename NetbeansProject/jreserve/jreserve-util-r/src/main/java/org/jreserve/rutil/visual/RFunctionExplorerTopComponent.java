package org.jreserve.rutil.visual;

import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RFunction;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.rutil.visual//RFunctionExplorer//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "RFunctionExplorerTopComponent",
    iconBase = "resources/r.png",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED
)
@TopComponent.Registration(
    mode = "editor", 
    openAtStartup = false
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.rutil.visual.RFunctionExplorerTopComponent"
)
@ActionReference(
    path = "Menu/Window", 
    position = 333
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_RFunctionExplorerAction",
    preferredID = "RFunctionExplorerTopComponent"
)
@Messages({
    "CTL_RFunctionExplorerAction=RFunction Explorer",
    "CTL_RFunctionExplorerTopComponent=RFunction Explorer",
    "LBL.RFunctionExplorerTopComponent.Parameters=Parameters",
    "LBL.RFunctionExplorerTopComponent.Return=Return"
})
public final class RFunctionExplorerTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    private final static ExplorerManager em = new ExplorerManager();
    private RCode rCode = new RCode();
    
    private Result<RFunction> result;
    
    public RFunctionExplorerTopComponent() {
        initComponents();
        setName(Bundle.CTL_RFunctionExplorerTopComponent());
        initContext();
    }
    
    private void initContext() {
        em.setRootContext(new AbstractNode(new RFunctionChildren()));
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
    }

    @Override
    public void resultChanged(LookupEvent le) {
        RFunction function = getLookup().lookup(RFunction.class);
        rCode.clear();
        if(function == null) {
            htmlText.setText(null);
        } else {
            htmlText.setText(getHtml(function));
            setRCode(function);
        }
        scrollHtmlTextToTop();
    }
    
    private void scrollHtmlTextToTop() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                htmlText.scrollRectToVisible(new java.awt.Rectangle(1, 1));
            }
        });
    }
    
    private String getHtml(RFunction function) {
        StringBuilder html = new StringBuilder("<html><body>");
        appendTitle(html, function);
        appendDescription(html, function);
        appendParameters(html, function);
        return html.append("</body></html>").toString();
    }
    
    private void appendTitle(StringBuilder html, RFunction function) {
        html.append("<h2>")
            .append(escapeString(function.getName()))
            .append("</h2>");
    }
    
    private String escapeString(String str) {
        if(str==null || str.trim().length()==0)
            return "-";
        return str;
    }
    
    private void appendDescription(StringBuilder html, RFunction function) {
        html.append("<p>")
            .append(escapeString(function.getDescription()))
            .append("</p>");
    }
    
    private void appendParameters(StringBuilder html, RFunction function) {
        html.append("<h3>")
            .append(Bundle.LBL_RFunctionExplorerTopComponent_Parameters())
            .append("</h3><ul>");
        java.util.Map<String, String> params = function.getParameters();
        for(String name : params.keySet())
            appendParameter(html, name, params.get(name));
        appendParameter(html, Bundle.LBL_RFunctionExplorerTopComponent_Return(), function.getReturn());
        html.append("</ul>");
    }
    
    private void appendParameter(StringBuilder html, String name, String description) {
        html.append("<li>")
            .append("<b>").append(escapeString(name)).append(":</b><br/>")
            .append(escapeString(description))
            .append("</li>");
    }
    
    private void setRCode(RFunction function) {
        for(String library : function.getLibraryDependendencies())
            rCode.addLibrary(library);
        for(String fn : function.getFunctionDependendencies())
            rCode.addFunction(fn);
        rCode.addSource(function.getSource());
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPanel = new javax.swing.JSplitPane();
        functionList = new org.openide.explorer.view.ListView();
        contentSplit = new javax.swing.JSplitPane();
        textScroll = new javax.swing.JScrollPane();
        htmlText = new javax.swing.JTextPane();
        codeScroll = new javax.swing.JScrollPane();
        codeText = new RCodeTextPane(rCode);

        setLayout(new java.awt.BorderLayout());

        splitPanel.setDividerLocation(0.2);

        functionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        splitPanel.setLeftComponent(functionList);

        contentSplit.setDividerLocation(0.2);
        contentSplit.setDividerLocation(0.5d);
        contentSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        htmlText.setContentType(org.openide.util.NbBundle.getMessage(RFunctionExplorerTopComponent.class, "RFunctionExplorerTopComponent.htmlText.contentType")); // NOI18N
        textScroll.setViewportView(htmlText);

        contentSplit.setTopComponent(textScroll);

        codeScroll.setViewportView(codeText);

        contentSplit.setRightComponent(codeScroll);

        splitPanel.setRightComponent(contentSplit);

        add(splitPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane codeScroll;
    private javax.swing.JTextPane codeText;
    private javax.swing.JSplitPane contentSplit;
    private org.openide.explorer.view.ListView functionList;
    private javax.swing.JTextPane htmlText;
    private javax.swing.JSplitPane splitPanel;
    private javax.swing.JScrollPane textScroll;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        result = getLookup().lookupResult(RFunction.class);
        result.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("rfunctionexplorer.mainsplit.location", ""+splitPanel.getDividerLocation());
        p.setProperty("rfunctionexplorer.contentsplit.location", ""+contentSplit.getDividerLocation());
    }

    void readProperties(java.util.Properties p) {
        setLocation(splitPanel, p.getProperty("rfunctionexplorer.mainsplit.location", ""));
        setLocation(contentSplit, p.getProperty("rfunctionexplorer.contentsplit.location", ""));
    }
    
    private void setLocation(JSplitPane split, String str) {
        if(str == null || str.trim().length()==0) return;
        try {
            int location = new Integer(str);
            split.setDividerLocation(location);
        } catch (NumberFormatException ex) {}
    }
}
