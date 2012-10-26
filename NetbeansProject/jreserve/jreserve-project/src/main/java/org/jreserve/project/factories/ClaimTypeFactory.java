package org.jreserve.project.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.claimtype.ClaimTypeElement;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTypeFactory extends SessionTask<ProjectElement<ClaimType>> {

    private final static Logger logger = Logger.getLogger(ClaimTypeFactory.class.getName());
    private final String name;
    private final LoB lob;
    
    public ClaimTypeFactory(LoB lob, String name, boolean openSession) {
        super(openSession);
        this.name = name;
        this.lob = lob;
    }

    @Override
    protected ProjectElement<ClaimType> doTask() throws Exception {
        ClaimType ct = new ClaimType(name);
        lob.addClaimType(ct);
        session.persist(ct);
        logger.log(Level.INFO, "ClaimType created: \"{0}\"", name);
        return new ClaimTypeElement(ct);
    }

}
