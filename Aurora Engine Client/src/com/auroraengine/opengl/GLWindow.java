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
public interface GLWindow {
	public void create() throws AuroraException;
	public boolean isCloseRequested() throws AuroraException;
	public void update() throws AuroraException;
	public GLOptions getGLOptions();
	public void setGLOptions(GLOptions new_ops);
	public void destroy();
	
	// TODO: Tie this to the cameras rather than having it as a separate thing.
	public static void updateGL() throws AuroraException {
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.5f);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}