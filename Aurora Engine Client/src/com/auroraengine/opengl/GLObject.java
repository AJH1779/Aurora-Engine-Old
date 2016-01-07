/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;

/**
 *
 * @author Arthur
 */
public interface GLObject {
	/**
	 * Must be called before create() can be used, unless otherwise specified.
	 * Loads any resources connected to the object, can be called by any thread.
	 * @throws ClientException 
	 */
	public void load() throws ClientException;
	/**
	 * Must be called after destroy() has been used, unless otherwise specified.
	 * Unloads any resources connected to the object, can be called by any thread.
	 * @throws ClientException 
	 */
	public void unload() throws ClientException;
	
	/**
	 * Must be called to make this object usable by the GL thread.
	 * @throws GLException 
	 */
	public void create() throws GLException;
	/**
	 * Must always be called by a created object on shutdown by the GL thread.
	 */
	public void destroy();
}
