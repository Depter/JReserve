package org.jreserve.project;

import org.jreserve.project.filesystem.ClaimTypeLoader;
import org.jreserve.project.filesystem.LoBLoader;
import org.openide.modules.ModuleInstall;

/**
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        LoBLoader.getDefault();
        ClaimTypeLoader.getDefault();
    }
}
