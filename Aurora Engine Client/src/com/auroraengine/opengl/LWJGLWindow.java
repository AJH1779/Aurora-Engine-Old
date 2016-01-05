/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.Session;
import com.auroraengine.data.Properties;
import com.auroraengine.debug.AuroraException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Arthur
 */
public class LWJGLWindow implements GLWindow {
	/**
	 * Creates a window using the native LWJGL window package, using the specified
	 * properties and session settings.
	 * @param session
	 * @param properties 
	 */
	public LWJGLWindow(Session session, Properties properties) {
		ops = new GLOptions(session, properties);
	}
	private final GLOptions ops;
	private GLOptions next_options;

	@Override
	public void create() throws AuroraException {
		try {
			updateDisplay();
			Display.create();
		} catch (LWJGLException ex) {
			throw new AuroraException(ex);
		}
		
		// Then there is the generic GL Initialisation
		GLWindow.updateGL();
	}
	private void updateDisplay() throws LWJGLException, AuroraException {
		if(ops.getBoolean("set_display_config"))
			Display.setDisplayConfiguration(ops.getFloat("gamma"),
							ops.getFloat("brightness"), ops.getFloat("contrast"));
		Display.setResizable(ops.getBoolean("resizeable"));
		Display.setVSyncEnabled(ops.getBoolean("vsync"));

		try {
			if(ops.getBoolean("fullscreen")) {
				int width = ops.getInteger("fullscreen_width"),
								height = ops.getInteger("fullscreen_height"),
								sync = ops.getInteger("fullscreen_sync");
				if(width == -1 && height == -1 && sync == -1) {
					Display.setFullscreen(true);
				} else {
					DisplayMode[] dms = Display.getAvailableDisplayModes();
					if(width == -1)   width = Display.getDesktopDisplayMode().getWidth();
					if(height == -1) height = Display.getDesktopDisplayMode().getHeight();
					if(sync == -1)     sync = Display.getDesktopDisplayMode().getFrequency();
					for(DisplayMode dm : dms) {
						if(dm.getWidth() == width && dm.getHeight() == height
										&& dm.getFrequency() == sync) {
							Display.setDisplayModeAndFullscreen(dm);
							break;
						}
					}
					if(!Display.isFullscreen()) {
						Display.setFullscreen(true);
					}
				}
			}
		} catch (LWJGLException ex) { ops.set("fullscreen", false); }
		if(!ops.getBoolean("fullscreen")) {
			Display.setFullscreen(false);
			Display.setDisplayMode(new DisplayMode(ops.getInteger("windowed_width"),
							ops.getInteger("windowed_height")));
		}
	}

	@Override
	public boolean isCloseRequested() throws AuroraException {
		return Display.isCloseRequested();
	}

	@Override
	public void update() throws AuroraException {
		Display.update();
		if(next_options != null) {
			// Perform a screen update
			ops.set(next_options);
			next_options = null;
			try { updateDisplay(); }
			catch (LWJGLException ex) { throw new AuroraException(ex); }
		}
		// Remove this and move to the camera?
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public GLOptions getGLOptions() {
		return new GLOptions(ops);
	}
	@Override
	public void setGLOptions(GLOptions new_ops) {
		next_options = new_ops;
	}
	@Override
	public void destroy() {
		Display.destroy();
	}
}