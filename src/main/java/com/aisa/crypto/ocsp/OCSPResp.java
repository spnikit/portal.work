package com.aisa.crypto.ocsp;

import ru.CryptoPro.JCP.ASN.PKIXOCSP.BasicOCSPResponse;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.CertStatus;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.OCSPResponse;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.OCSPResponseStatus;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.ResponseBytes;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * created 28.10.2009 12:58:10 by @author kunina
 * last modified $Date: 2009-10-29 15:14:38 +0300 (Р§С‚, 29 РѕРєС‚ 2009) $ by
 * $Author: kunina $
 */
//Упрощенная схема (без расширения nonce [1.3.6.1.5.5.7.48.1.2])

public class OCSPResp {

public final String BASIC_OCSP_RESPONSE_OID = "1.3.6.1.5.5.7.48.1.1";

public static final int SUCCESSFUL = OCSPResponseStatus.successful;
public static final int MALFORMED_REQUEST = OCSPResponseStatus.malformedRequest;
public static final int INTERNAL_ERROR = OCSPResponseStatus.internalError;
public static final int TRY_LATER = OCSPResponseStatus.tryLater;
public static final int SIG_REQUIRED = OCSPResponseStatus.sigRequired;
public static final int UNAUTHORIZED = OCSPResponseStatus.unauthorized;
public static final int BAD_CRL = OCSPResponseStatus.badCRL;

public static final String SUCCESSFUL_STR =
		"(0) successful [Response has valid confirmations]";
public static final String MALFORMED_REQUEST_STR =
		"(1) malformedRequest [Illegal confirmation request]";
public static final String INTERNAL_ERROR_STR =
		"(2) internalError [Internal error in issuer]";
public static final String TRY_LATER_STR = "(3) tryLater [Try again later]";
public static final String SIG_REQUIRED_STR =
		"(5) sigRequired [Must sign the request]";
public static final String UNAUTHORIZED_STR =
		"(6) unauthorized [Request unauthorized]";
public static final String BAD_CRL_STR = "(8) badCRL []";
public static final String UNCKNOWN_STR = "uncknown";

private int respStatus;
private ResponseBytes bytes;

public OCSPResp(byte[] response) throws OCSPException {
	try {
		final Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(response);
		final OCSPResponse rr = new OCSPResponse();
		rr.decode(dec);
		respStatus = rr.responseStatus.value;
		bytes = rr.responseBytes;
	} catch (final Exception e) {
		final OCSPException ex = new OCSPException();
		ex.initCause(e);
		throw ex;
	}
}

/**
 * (0) successful [Response has valid confirmations]
 * (1) malformedRequest [Illegal confirmation request]
 * (2) internalError [Internal error in issuer]
 * (3) tryLater [Try again later]
 * (4) [is not used]
 * (5) sigRequired [Must sign the request]
 * (6) unauthorized [Request unauthorized]
 */
public int getResponseStatus() {
	return respStatus;
}

public String getResponseStatusStr() {
	if (respStatus == SUCCESSFUL)
		return SUCCESSFUL_STR;
	if (respStatus == MALFORMED_REQUEST)
		return MALFORMED_REQUEST_STR;
	if (respStatus == INTERNAL_ERROR)
		return INTERNAL_ERROR_STR;
	if (respStatus == TRY_LATER)
		return TRY_LATER_STR;
	if (respStatus == SIG_REQUIRED)
		return SIG_REQUIRED_STR;
	if (respStatus == UNAUTHORIZED)
		return UNAUTHORIZED_STR;
	if (respStatus == BAD_CRL)
		return BAD_CRL_STR;
	return UNCKNOWN_STR;
}

public CertStatus getCertStatus() throws OCSPException {
	if (bytes.responseType.equals(new Asn1ObjectIdentifier(new OID(
		BASIC_OCSP_RESPONSE_OID).value))) {
		final BasicOCSPResponse br = new BasicOCSPResponse();
		final Asn1DerDecodeBuffer dec =
				new Asn1DerDecodeBuffer(bytes.response.value);
		try {
			br.decode(dec);
		} catch (final Exception e) {
			final OCSPException ex = new OCSPException();
			ex.initCause(e);
			throw ex;
		}
		return br.tbsResponseData.responses.elements[0].certStatus;
	} else
		throw new OCSPException("Unknown response type");
}
}
