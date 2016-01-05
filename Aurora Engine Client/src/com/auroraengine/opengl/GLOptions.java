/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.Session;
import com.auroraengine.data.Properties;
import com.auroraengine.debug.AuroraException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Arthur
 */
public final class GLOptions {
	private GLOptions() {}
	public GLOptions(Session session, Properties properties) {
		
	}
	public GLOptions(GLOptions ops) {
		set(ops);
	}
	private final HashMap<String, Object> data = new HashMap<String, Object>(){{
		put("title", "GL Window");
		put("windowed_width", 640); put("windowed_height", 480);
		put("fullscreen", false);
		put("fullscreen_width", -1); put("fullscreen_height", -1);
		put("fullscreen_sync", -1);
		put("resizeable", true);
		put("vsync", false);
		
		put("set_display_config", false);
		put("gamma", 0.5f);
		put("brightness", 0.0f);
		put("contrast", 0.5f);
	}};
	
	public String getString(String key) throws AuroraException {
		try {
			return (String) data.get(key);
		} catch (ClassCastException ex) {
			throw new AuroraException(ex);
		}
	}
	public Boolean getBoolean(String key) {
		try { return (Boolean) data.get(key); }
		catch (ClassCastException ex) { return false; }
	}
	public Integer getInteger(String key) {
		try { return (Integer) data.get(key); }
		catch (ClassCastException ex) { return 0; }
	}
	public Float getFloat(String key) {
		try { return (Float) data.get(key); }
		catch (ClassCastException ex) { return 1.0f; }
	}
	public void set(String key, Object val) {
		data.put(key, val);
	}
	public void set(GLOptions ops) {
		data.clear();
		data.putAll(ops.data);
	}
	
	public void saveTo(File f) throws AuroraException {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(f))) {
			for(Entry<String,Object> ent : data.entrySet()) {
				out.write(ent.getKey() + " : " + ent.getValue().toString()
								.replace(System.lineSeparator(), "\\n"));
				out.newLine();
			}
		} catch (IOException ex) {
			throw new AuroraException(ex);
		}
	}
	public void loadFrom(File f) throws AuroraException {
		data.clear();
		try (BufferedReader in = new BufferedReader(new FileReader(f))) {
			for(String line = in.readLine(); line != null; line = in.readLine()) {
				String key, val;
				if(line.contains(" : ")) {
					key = line.substring(0, line.indexOf(" "));
					val = line.substring(line.indexOf(" : ") + 3);
					if(val.matches("-?[0-9]+")) {
						data.put(key, Integer.valueOf(val));
					} else if(val.matches("-?[0-9]*.[0-9]*(E[0-9]+)?")) {
						data.put(key, Float.valueOf(val));
					} else if(val.matches("(true|false)")) {
						data.put(key, val.matches("true"));
					} else {
						data.put(key, val.replace("\\n", System.lineSeparator()));
					}
				}
			}
		} catch (IOException ex) {
			throw new AuroraException(ex);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("GLOptions:").append(System.lineSeparator());
		data.forEach((k,v) -> {
			b.append(k).append(" : ").append("(").append(v.getClass().getSimpleName()).append(") ").append(v.toString().replace(System.lineSeparator(), "\\n")).append(System.lineSeparator());
		});
		
		return b.toString();
	}
	
	public static void main(String[] args) throws AuroraException {
		GLOptions ops = new GLOptions();
		ops.saveTo(new File("test.txt"));
		ops.loadFrom(new File("test.txt"));
		System.out.println(ops);
	}
}