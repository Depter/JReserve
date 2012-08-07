/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.project.system.util;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        FactoryUtil.loadFactories();
    }
}
