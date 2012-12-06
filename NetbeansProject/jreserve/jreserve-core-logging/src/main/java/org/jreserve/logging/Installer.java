package org.jreserve.logging;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        LoggingSetting.initialize();
        EDTExceptionLogger.install();
    }
}
