package ru.aisa.rjd.log4j.level;

import org.apache.log4j.Level;

public final class RzdLevel extends Level {
	
	static public final int RZD_TRACE_INT = Level.ERROR_INT + 1;
	static private final String RZD_TRACE_STR = "RZD_TRACE";
	static public final RzdLevel RZD_TRACE = new RzdLevel(RZD_TRACE_INT, RZD_TRACE_STR, 7);
	
	protected RzdLevel(final int level, final String strLevel, final int syslogEquiv) {
		super(level, strLevel, syslogEquiv);
	}
	
	public static Level toLevel(final String sArg) {
		return (Level) toLevel(sArg, RzdLevel.RZD_TRACE);
	}
	
	public static Level toLevel(final String sArg, final Level defaultValue) {
		if(sArg == null) {
			return defaultValue;
		}		
		final String stringVal = sArg.toUpperCase();
		if(stringVal.equals(RZD_TRACE_STR)) {
			return RzdLevel.RZD_TRACE;
		}
		return Level.toLevel(sArg, defaultValue);
	}
	
	public static Level toLevel(final int i) throws IllegalArgumentException {
		if(i == RZD_TRACE_INT) {
			return RzdLevel.RZD_TRACE;
		} else {
			return Level.toLevel(i);
		}
	}
}
