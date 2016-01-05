/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientCore;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.threading.Synchro;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class GLCore extends Synchro {
    private static final Logger LOG = AuroraLogs.getLogger(GLCore.class);
    
    public GLCore(ClientCore core) {
        super("GL Core", core);
        this.core = core;
        this.window = new LWJGLWindow(core.getSession(), core.getProperties());
    }
    private final ClientCore core;
    private final GLWindow window;

    @Override
    protected void initialise() throws AuroraException {
        LOG.info("Initialising");
        window.create();
        LOG.info("Initialised");
    }
    @Override
    protected boolean isRunning() throws AuroraException {
        return !window.isCloseRequested();
    }
    @Override
    protected void update() throws AuroraException {
        window.update();
    }
    @Override
    protected void shutdown() {
        LOG.info("Shutting Down");
        if(window != null) window.destroy();
        LOG.info("Shut Down");
    }

    @Override
    protected void processException(AuroraException ex) {
        LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
    }
}