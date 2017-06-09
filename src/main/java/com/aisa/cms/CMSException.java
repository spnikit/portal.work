package com.aisa.cms;

/**
 * created 09.11.2009 10:51:30 by @author kunina
 * last modified $Date: 2011-03-10 11:38:16 +0300 (Чт, 10 мар 2011) $ by $Author: olgakunina $
 */
public class CMSException extends Exception {

private static final long serialVersionUID = 1L;

public CMSException() {
	super();
}

public CMSException(String msg) {
	super(msg);
}

public CMSException(String msg, Throwable cause) {
	super(msg, cause);
}

public CMSException(Throwable cause) {
	super(cause);
}

}
