/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Arthur
 */
public class GLTexture implements GLObject {
	public GLTexture(int w, int h) {
		this.img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
	}
	public GLTexture(BufferedImage img) {
		this.img = img;
	}
	public GLTexture(File file) {
		this.file = file;
	}
	private File file;
	private BufferedImage img;
	private boolean save_on_unload = false;
	
	@Override
	public void load() throws ClientException {
		if(file != null) {
			try {
				img = ImageIO.read(file);
			} catch (IOException ex) { throw new ClientException(ex); }
		}
	}
	@Override
	public void unload() throws ClientException {
		if(file != null) {
			if(save_on_unload) try {
				ImageIO.write(img, "png", file);
			} catch (IOException ex) { throw new ClientException(ex); }
			img = null;
		}
	}

	private int tex_ref = 0;
	@Override
	public void create() throws GLException {
		if(tex_ref != 0) {
			tex_ref = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ref);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
		}
	}
	@Override
	public void destroy() {
		
	}
}