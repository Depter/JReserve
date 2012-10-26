package org.jreserve.project.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.lob.LoBElement;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoBFactory extends SessionTask<ProjectElement<LoB>> {

    private final static Logger logger = Logger.getLogger(LoBFactory.class.getName());
    private final String name;
    
    public LoBFactory(String name, boolean openSession) {
        super(openSession);
        this.name = name;
    }

    @Override
    protected ProjectElement<LoB> doTask() throws Exception {
        LoB lob = new LoB(name);
        session.persist(lob);
        logger.log(Level.INFO, "LoB created: \"{0}\"", name);
        return new LoBElement(lob);
    }
    
    
}
