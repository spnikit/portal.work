package com.aisa.crypto.tsp;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-10-27 15:40:41 +0300 (Вт, 27 окт 2009) $ by $Author: kunina $
 */
public class TSPException extends Exception {

private static final long serialVersionUID = 1L;

public TSPException() {
	super();
}

public TSPException(String msg) {
	super(msg);
}

public TSPException(String msg, Throwable cause) {
	super(msg, cause);
}

public TSPException(Throwable cause) {
	super(cause);
}

}