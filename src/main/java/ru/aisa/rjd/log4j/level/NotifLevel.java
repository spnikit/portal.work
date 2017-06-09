package ru.aisa.rjd.log4j.level;

import org.apache.log4j.Level;

public final class NotifLevel extends Level {
	
	static public final int NOTIF_INT = Level.ERROR_INT + 2;
	static private final String NOTIF_STR = "NOTIF";
	static public final NotifLevel NOTIF = new NotifLevel(NOTIF_INT, NOTIF_STR, 7);
	
	protected NotifLevel(final int level, final String strLevel, final int syslogEquiv) {
		super(level, strLevel, syslogEquiv);
	}
	
	public static Level toLevel(final String sArg) {
		return (Level) toLevel(sArg, NotifLevel.NOTIF);
	}
	
	public static Level toLevel(final String sArg, final Level defaultValue) {
		if(sArg == null) {
			return defaultValue;
		}		
		final String stringVal = sArg.toUpperCase();
		if(stringVal.equals(NOTIF_STR)) {
			return NotifLevel.NOTIF;
		}
		return Level.toLevel(sArg, defaultValue);
	}
	
	public static Level toLevel(final int i) throws IllegalArgumentException {
		if(i == NOTIF_INT) {
			return NotifLevel.NOTIF;
		} else {
			return Level.toLevel(i);
		}
	}
}
