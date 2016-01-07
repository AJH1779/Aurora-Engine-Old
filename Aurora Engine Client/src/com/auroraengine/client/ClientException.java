/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

import com.auroraengine.debug.AuroraException;

/**
 *
 * @author Arthur
 */
public class ClientException extends AuroraException {
	public ClientException() {}
	public ClientException(String msg) {
		super(msg);
	}
	public ClientException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public ClientException(String msg, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}
	public ClientException(Throwable cause) {
		super(cause);
	}
}
