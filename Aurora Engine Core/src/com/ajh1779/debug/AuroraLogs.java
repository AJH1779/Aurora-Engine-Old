/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.debug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public final class AuroraLogs {
    public static class AuroraLoggerFormat extends Formatter {
        private static final SimpleDateFormat DATE_FORMAT =
                new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            StringBuilder b = new StringBuilder(1000);
            b.append(DATE_FORMAT.format(new Date(record.getMillis())))
                .append(" - ")
                .append("[").append(record.getLevel()).append(": ")
                .append(record.getSourceClassName()).append(".")
                .append(record.getSourceMethodName()).append("]")
                .append(formatMessage(record)).append("\n");
            return b.toString();
        }
    }
    public static final AuroraLoggerFormat AURORA_FORMATTER = new AuroraLoggerFormat();
    
    private AuroraLogs() {}
    public static Logger getLogger(Class cls) {
        Logger log = Logger.getLogger(cls.getName());
        
        for(Handler h : log.getHandlers()) {
            h.setFormatter(AURORA_FORMATTER);
        }
        
        return log;
    }
}
