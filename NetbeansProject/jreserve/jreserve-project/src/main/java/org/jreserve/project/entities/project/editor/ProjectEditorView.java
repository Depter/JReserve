package org.jreserve.project.entities.project.editor;

import java.awt.BorderLayout;
import javax.swing.*;
import org.jreserve.project.entities.project.ProjectElement;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
        
/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ProjectEditorView.name=Name:",
    "LBL.ProjectEditorView.description=Description:"
})
class ProjectEditorView extends JPanel implements MultiViewElement {
    
    private JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callBack;
    private ProjectElement element;

    private JTextField nameText;
    private JTextArea descriptionText;
    
    ProjectEditorView(ProjectElement element) {
        this.element = element;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(getNamePanel(), BorderLayout.PAGE_START);
        add(getDescriptionPanel(), BorderLayout.CENTER);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getNamePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(Bundle.LBL_ProjectEditorView_name()), BorderLayout.LINE_START);
        
        nameText = new JTextField(element.getValue().getName());
        panel.add(nameText, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel getDescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(Bundle.LBL_ProjectEditorView_description()), BorderLayout.PAGE_START);
        
        descriptionText = new JTextArea(element.getValue().getDescription());
        JScrollPane scroll = new JScrollPane(descriptionText);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        callBack = mvec;
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return Lookups.singleton(this);
    }

    @Override public void componentOpened() {}
    @Override public void componentClosed() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public CloseOperationState canCloseElement() {
        //TODO check saveable
        return CloseOperationState.STATE_OK;
    }

}
