/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.debug;

/**
 *
 * @author Arthur
 */
public class AuroraException extends Exception {
    public AuroraException() {}
    public AuroraException(String msg) {
        super(msg);
    }
    public AuroraException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public AuroraException(String msg, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(msg, cause, enableSuppression, writableStackTrace);
    }
    public AuroraException(Throwable cause) {
        super(cause);
    }
}
