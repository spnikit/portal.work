package com.aisa.crypto.ocsp;

/**
 * created 28.10.2009 10:35:18 by @author kunina
 * last modified $Date: 2009-10-28 18:17:07 +0300 (Ср, 28 окт 2009) $ by $Author: kunina $
 */
public class OCSPException extends Exception {

private static final long serialVersionUID = 1L;

public OCSPException() {
	super();
}

public OCSPException(String msg) {
	super(msg);
}

public OCSPException(String msg, Throwable cause) {
	super(msg, cause);
}

public OCSPException(Throwable cause) {
	super(cause);
}

}