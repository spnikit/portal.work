package com.aisa.crypto.ocsp;

import ru.CryptoPro.JCP.ASN.PKIXOCSP.CertStatus;

/**
 * created 28.10.2009 15:49:27 by @author kunina
 * last modified $Date: 2009-10-28 18:17:07 +0300 (Ср, 28 окт 2009) $ by $Author: kunina $
 */
public class OCSPCertStatus {

public static final byte GOOD = 1;
public static final byte REVOKED = 2;
public static final byte UNKNOWN = 3;

private final int status;
private final String statusStr;

public OCSPCertStatus(CertStatus certStatus) {
	status = certStatus.getChoiceID();
	statusStr = certStatus.getElemName();
}

/**
 * (1) good
 * (2) revoked
 * (3) unknown
 */
public int getCertStatus() {
	return status;
}

public String getCertStatusSTR() {
	return statusStr;
}

}
