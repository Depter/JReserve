package org.jreserve.localesettings;

import static org.jreserve.resources.textfieldfilters.CharacterTextField.EMPTY_CHAR;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.LocaleSettingPanel.InvalidLocale=Language is not selected!",
    "MSG.LocaleSettingPanel.InvalidDateFormat=Date format is invalid!",
    "MSG.LocaleSettingPanel.InvalidNumberFormat=Number format is invalid!",
    "MSG.LocaleSettingPanel.InvalidDecimalSeparator=Decimal separator is invalid!",
    "MSG.LocaleSettingPanel.InvalidThousandSeparator=Thousand separatoris invalid!",
    "MSG.LocaleSettingPanel.InvalidNaN=NaN value is invalid!!"
})
class LocaleSettingValidator {

    private LocaleSettingPanel panel;
    
    LocaleSettingValidator(LocaleSettingPanel panel) {
        this.panel = panel;
    }
    
    boolean validatePanel() {
        boolean isValid = checkValid();
        if(isValid)
            panel.showError(null);
        return isValid;
    }    
    
    private boolean checkValid() {
        return isLocaleValid() &&
               isDateFormatValid() && 
               isNumberFormatValid() && 
               isDecimalSeparatorValid() && 
               isGroupingSeparatorValid();
    }
    
    private boolean isLocaleValid() {
        if(panel.getSelectedLocale() == null) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidLocale());
            return false;
        }
        return true;
    }
    
    private boolean isDateFormatValid() {
        if(panel.getDateFormat() == null) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidDateFormat());
            return false;
        }
        return true;
    }
    
    boolean isNumberValid() {
        return isNumberFormatValid() &&
               isDecimalSeparatorValid() &&
               isGroupingSeparatorValid();
    }
    
    private boolean isNumberFormatValid() {
        if(panel.getDecimalFormat() == null) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidNumberFormat());
            return false;
        }
        return true;
    }
    
    private boolean isDecimalSeparatorValid() {
        char c = panel.getDecimalSeparator();
        if(EMPTY_CHAR == c) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidDecimalSeparator());
            return false;
        }
        return true;
    }
    
    private boolean isGroupingSeparatorValid() {
        char c = panel.getGroupingSeparator();
        if(EMPTY_CHAR == c) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidThousandSeparator());
            return false;
        }
        return true;
    }
    
    boolean isNaNValid() {
        String nan = panel.getNaN();
        if(nan == null || nan.trim().length() == 0) {
            panel.showError(Bundle.MSG_LocaleSettingPanel_InvalidNaN());
            return false;
        }
        return true;
    }
    
}
