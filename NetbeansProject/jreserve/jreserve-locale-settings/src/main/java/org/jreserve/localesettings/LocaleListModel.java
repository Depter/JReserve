package org.jreserve.localesettings;

import java.util.*;
import javax.swing.DefaultListModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LocaleListModel extends DefaultListModel {
    
    private final static Set<Locale> LOCALES = new TreeSet<Locale>(new Comparator<Locale>() {
            @Override
            public int compare(Locale l1, Locale l2) {
                String n1 = l1.getDisplayLanguage(Locale.ENGLISH);
                String n2 = l2.getDisplayLanguage(Locale.ENGLISH);
                return n1.compareToIgnoreCase(n2);
            }
        });
    
    static {
        LOCALES.addAll(Arrays.asList(Locale.getAvailableLocales()));
    }

    
    private String language;

    LocaleListModel() {
        setLanguage(null);
    }
    
    
    void setLanguage(String language) {
        this.language = language==null? language : language.toLowerCase();
        clear();
        List<Locale> locales = filterLocales();
        for(Locale l : locales)
            addElement(l);
        fireAdded();
    }
    
    @Override
    public void clear() {
        int size = super.getSize();
        super.clear();
        if(size > 0)
            fireIntervalRemoved(this, 0, size-1);
    }
    
    private List<Locale> filterLocales() {
        int length = LOCALES.size();
        List<Locale> result = new ArrayList<Locale>(length);
        for(Locale locale : LOCALES)
            if(isFilteredLocale(locale))
                result.add(locale);
        return result;
    }
    
    private boolean isFilteredLocale(Locale locale) {
        String l = locale.getDisplayLanguage(Locale.ENGLISH);
        if(l==null || l.trim().length() == 0)
            return false;
        if(language == null)
            return true;
        return l.toLowerCase().startsWith(language);
    }
    
    private void fireAdded() {
        int size = getSize();;
        if(size > 0)
            fireIntervalAdded(this, 0, size-1);
    }
}
