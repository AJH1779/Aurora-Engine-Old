/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

import com.auroraengine.data.Properties;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLCore;
import com.auroraengine.threading.Synchro;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;

/**
 *
 * @author Arthur
 */
public class ClientCore extends Synchro {
    private static final Logger LOG = AuroraLogs.getLogger(ClientCore.class);
    
    public ClientCore(Properties props, Session session) throws AuroraException {
        super("Client Core");
        this.properties = props;
        this.session = session;
        this.glcore = new GLCore(this);
    }
    private final Properties properties;
    private final Session session;
    private final GLCore glcore;
    
    public Properties getProperties() { return properties; }
    public Session getSession() { return session; }
    
    @Override
    protected void initialise() throws AuroraException {
        LOG.info("Initialising");
        waitForStart(glcore, 8);
        LOG.info("Initialised");
    }
    @Override
    protected boolean isRunning() throws AuroraException {
        return glcore.getThreading();
    }
    @Override
    protected void update() throws AuroraException {
        
    }
    @Override
    protected void shutdown() {
        LOG.info("Shutting Down");
        waitForStop(glcore);
        LOG.info("Shut Down");
        System.exit(0);
    }
    
    @Override
    protected void processException(AuroraException ex) {
        LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
    }
}
