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
	
	public static void updateGL(GLOptions ops) throws AuroraException {
		GL11.glAlphaFunc(GL11.GL_GEQUAL, ops.getFloat("alpha_threshold"));
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		if(ops.getBoolean("alpha_test")) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		} else {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
		if(ops.getBoolean("depth_test")) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		} else {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}
}