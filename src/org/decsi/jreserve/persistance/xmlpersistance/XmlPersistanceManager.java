package org.decsi.jreserve.persistance.xmlpersistance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.decsi.jreserve.data.LoB;
import org.decsi.jreserve.persistance.PersistanceManager;
import org.decsi.jreserve.settings.Settings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XmlPersistanceManager implements PersistanceManager {
    
    private final static String HOME_KEY = "persistance.home";
    private final static String LOB_XML = "lobs.xml";
    private File home;
    
    private List<LoB> lobs;
    
    public XmlPersistanceManager() {
        home = new File(Settings.get(HOME_KEY));
    }
    
    @Override
    public List<LoB> getLoBs() {
        if(lobs == null)
            loadLoBs();
        return new ArrayList<LoB>(lobs);
    }
    
    private void loadLoBs() {
        LoBsParser parser = new LoBsParser();
        new DocumentParser(parser).parser(new File(home, LOB_XML));
        lobs = parser.getValue();
    }    
}
