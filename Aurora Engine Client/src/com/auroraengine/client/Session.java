/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

import com.auroraengine.data.Properties;
import java.io.File;

/**
 * This is a client-side
 * @author Arthur
 */
public final class Session {
    public Session(String name, Properties properties) {
        this.directory = new File(properties.getProgramDirectory(), "users/" + name);
    }
    private final File directory;
    
    public File getDirectory() {
        return directory;
    }
}