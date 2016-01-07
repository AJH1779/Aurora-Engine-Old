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

/**
 *
 * @author Arthur
 */
public class ClientCore extends Synchro {
    private static final Logger LOG = AuroraLogs.getLogger(ClientCore.class);
    
		/**
		 * Creates a client core with the specified program properties and the
		 * provided user session.
		 * @param properties The program properties.
		 * @param session The user session.
		 * @throws AuroraException 
		 */
    public ClientCore(Properties properties, Session session) throws AuroraException {
        super("Client Core");
        this.properties = properties;
        this.session = session;
        this.glcore = new GLCore(this);
    }
    private final Properties properties;
    private final Session session;
    private final GLCore glcore;
    
		/**
		 * Returns the program properties.
		 * @return The program properties.
		 */
    public final Properties getProperties() { return properties; }
		/**
		 * Returns the user session.
		 * @return The user session.
		 */
    public final Session getSession() { return session; }
    
    @Override
    protected void initialise() throws ClientException {
        LOG.info("Initialising");
        waitForStart(glcore, 8);
        LOG.info("Initialised");
    }
    @Override
    protected boolean isRunning() throws ClientException {
        return glcore.getThreading();
    }
    @Override
    protected void update() throws ClientException {
        
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
