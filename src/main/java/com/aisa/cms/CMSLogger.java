package com.aisa.cms;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * created 26.05.2010 16:22:40 by @author kunina
 * last modified $Date: 2011-03-10 11:38:16 +0300 (Чт, 10 мар 2011) $ by $Author: olgakunina $
 */
public class CMSLogger extends Logger {

//logger name = class name !!!
private static final String LOGGER_NAME = "com.aisa.cms.CMSLogger";
private static final String LOGGER_RES = "com.aisa.cms.resources.message";
private static final Logger log = new CMSLogger();

private CMSLogger() {
	super(LOGGER_NAME, LOGGER_RES);
	final LogManager logmanager = LogManager.getLogManager();
	logmanager.addLogger(this);
}

public static void inf(String mes) {
	final StackTraceElement caller = getCallerStackFrame();
	log.logp(Level.INFO, caller.getClassName(), caller.getMethodName(), mes);
}

public static void warn(String mes) {
	final StackTraceElement caller = getCallerStackFrame();
	log.logp(Level.WARNING, caller.getClassName(), caller.getMethodName(), mes);
}

public static void warn(String mes, Throwable thr) {
	final StackTraceElement caller = getCallerStackFrame();
	log.logp(Level.WARNING, caller.getClassName(), caller.getMethodName(), mes,
		thr);
}

public static void sever(String mes) {
	final StackTraceElement caller = getCallerStackFrame();
	log.logp(Level.SEVERE, caller.getClassName(), caller.getMethodName(), mes);
}

public static void sever(String mes, Throwable thr) {
	final StackTraceElement caller = getCallerStackFrame();
	log.logp(Level.SEVERE, caller.getClassName(), caller.getMethodName(), mes,
		thr);
}

private static StackTraceElement getCallerStackFrame() {
	final Throwable t = new Throwable();
	final StackTraceElement[] stackTrace = t.getStackTrace();
	int index = 0;
	// skip to stackentries until this class
	while (!stackTrace[index].getClassName().equals(LOGGER_NAME)) {
		index++;
	}
	// skip the stackentries of this class
	while (stackTrace[index].getClassName().equals(LOGGER_NAME)) {
		index++;
	}
	return stackTrace[index];
}

}
