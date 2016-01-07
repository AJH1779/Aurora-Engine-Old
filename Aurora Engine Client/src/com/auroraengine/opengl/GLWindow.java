/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.debug.AuroraException;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Arthur
 */
public interface GLWindow extends GLObject {
	/**
	 * Creates the window. Should be called first before other methods and should
	 * not be called unless destroy() has been called since.
	 * @throws GLException 
	 */
	public void create() throws GLException;
	/**
	 * Returns true if the window has been requested to close, such as through the
	 * hotkey Alt-F4, pressing the kill window button, or by the system OS.
	 * @return True if the window has been asked to close.
	 * @throws GLException
	 */
	public boolean isCloseRequested() throws GLException;
	/**
	 * Must be called for every frame, updating the position and shape of the
	 * window and context.
	 * @throws GLException 
	 */
	public void update() throws GLException;
	/**
	 * Returns a copy of the current settings used for the display.
	 * @return 
	 */
	public GLOptions getGLOptions();
	/**
	 * Sets new settings for the display, implemented on the next call to update().
	 * @param new_ops 
	 */
	public void setGLOptions(GLOptions new_ops);
	/**
	 * Destroys the display. Should be called after create() and must be called
	 * before quitting the program.
	 */
	public void destroy();
	
	// TODO: Tie this to the cameras rather than having it as a separate thing.
	/**
	 * Currently serves as a simple test for implementing some GL settings.
	 * @throws GLException 
	 */
	public static void updateGL() throws GLException {
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.5f);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}