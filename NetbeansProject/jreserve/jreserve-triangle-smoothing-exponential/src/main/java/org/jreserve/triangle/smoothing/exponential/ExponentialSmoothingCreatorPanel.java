package org.jreserve.triangle.smoothing.exponential;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.resources.textfieldfilters.DoubleFilter;
import org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExponentialSmoothingCreateDialog.Alpha=Alpha:"
})
class ExponentialSmoothingCreatorPanel extends SmoothingCreatorPanel{
    
    final static String SMOOTHING_ALPHA_PROPARTY = "ALPHA";
    
    private ExponentialSmoothing smoothing;
    private char decimalSeparator;
    private JTextField alphaText;
    
    ExponentialSmoothingCreatorPanel(int visibleDigits, double[] input) {
        super(visibleDigits, input);
    }

    @Override
    protected void addUserInputs(JPanel panel, PropertyChangeListener listener) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.insets = new Insets(15, 0, 0, 5);
        panel.add(new JLabel(Bundle.LBL_ExponentialSmoothingCreateDialog_Alpha()), gc);
        
        initAlphaText();
        gc.gridx=1; gc.weightx=1d;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(15, 0, 0, 0);
        panel.add(alphaText, gc);
    }
    
    private void initAlphaText() {
        decimalSeparator = LocaleSettings.getDecimalSeparator();
        alphaText = new JTextField();
        alphaText.setDocument(new DoubleFilter(decimalSeparator));
        alphaText.setText("0");
        alphaText.getDocument().addDocumentListener(new AlphaListener());
    }

    @Override
    protected double[] getSmoothedValues(double[] input) {
        if(smoothing == null)
            smoothing = new ExponentialSmoothing();
        return smoothing.smooth(input);
    }
    
    Double getAlpha() {
        String str = alphaText.getText();
        int length = (str==null)? 0 :str.length();
        if(length == 0)
            return null;
        if(decimalSeparator == str.charAt(length-1))
            str = str.substring(0, length - 1);
        return new Double(str.replace(decimalSeparator, '.'));
    }
    
    private class AlphaListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAlpha();
        }
        
        private void updateAlpha() {
            Double alpha = getAlpha();
            smoothing.setAlpha(getSmoothingAlpha(alpha));
            updateSmoothedValues();
            putClientProperty(SMOOTHING_ALPHA_PROPARTY, getAlpha());
        }

        private double getSmoothingAlpha(Double alpha) {
            if(alpha == null)
                return 0d;
            double a = alpha.doubleValue();
            return a<0d? 0d : (a > 1d? 1d : a);
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAlpha();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
}
