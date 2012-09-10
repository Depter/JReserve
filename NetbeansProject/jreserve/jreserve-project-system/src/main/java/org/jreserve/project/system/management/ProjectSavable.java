package org.jreserve.project.system.management;

import org.jreserve.project.system.ProjectElement;
import org.netbeans.api.actions.Savable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectSavable extends Savable {

    public ProjectElement getProjectElement();
}
