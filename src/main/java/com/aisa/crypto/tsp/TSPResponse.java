package com.aisa.crypto.tsp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import ru.CryptoPro.JCP.ASN.CPPKIXCMP.PKIFreeText;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.PKIXCMP.PKIFailureInfo;
import ru.CryptoPro.JCP.ASN.PKIXCMP.PKIStatus;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TimeStampReq;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TimeStampResp;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1UTF8String;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-10-27 16:46:22 +0300 (Вт, 27 окт 2009) $ by
 * $Author: kunina $
 */
public class TSPResponse {

TimeStampResp resp;
TSPToken timeStampToken;

public TSPResponse(TimeStampResp resp) throws TSPException {
	this.resp = resp;
	if (resp.timeStampToken != null) {
		timeStampToken = new TSPToken((SignedData) resp.timeStampToken.content);
	}
}

public TSPResponse(byte[] resp) throws TSPException {
	this(new ByteArrayInputStream(resp));
}

public TSPResponse(InputStream in) throws TSPException {
	this(readTimeStampResp(in));
}

private static TimeStampResp readTimeStampResp(InputStream in)
		throws TSPException {
	try {
		final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(in);
		final TimeStampResp tsr = new TimeStampResp();
		tsr.decode(asnBuf);
		return tsr;
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public int getStatus() {
	return (int) resp.status.status.value;
}

public String getStatusString() {
	if (resp.status.statusString != null) {
		final StringBuffer sb = new StringBuffer();
		final PKIFreeText text = resp.status.statusString;
		final Asn1UTF8String[] el = text.elements;
		for (int i = 0; i < el.length; i++) {
			sb.append(el[i].value);
		}
		return sb.toString();
	} else {
		return null;
	}
}

public PKIFailureInfo getFailInfo() {
	return resp.status.failInfo;
}

public TSPToken getTimeStampToken() {
	return timeStampToken;
}

/**
 * Check this response against to see if it a well formed response for
 * the passed in request. Validation will include checking the time stamp
 * token if the response status is GRANTED or GRANTED_WITH_MODS.
 * 
 * @param request the request to be checked against
 * @throws TSPException
 */
public void validate(TimeStampReq request) throws TSPException {
	final TSPToken tok = this.getTimeStampToken();
	if (tok != null) {
		final TSPTokenInfo tstInfo = tok.getTimeStampInfo();
		if (request.nonce != null
				&& !request.nonce.value.equals(tstInfo.getNonce()))
			throw new TSPException("response contains wrong nonce value");
		if (this.getStatus() != PKIStatus.granted
				&& this.getStatus() != PKIStatus.grantedWithMods)
			throw new TSPException("time stamp token found in failed request");
		if (!MessageDigest.isEqual(request.messageImprint.hashedMessage.value,
			tstInfo.getMessageImprintDigest()))
			throw new TSPException(
				"response for different message imprint digest");
		if (!tstInfo.getMessageImprintAlg().equals(
			request.messageImprint.hashAlgorithm.algorithm))
			throw new TSPException(
				"response for different message imprint algorithm");
		tok.verifySignCert();
		if (request.reqPolicy != null
				&& !request.reqPolicy.equals(tstInfo.getPolicy()))
			throw new TSPException("TSA policy wrong for request");
	} else if (this.getStatus() == PKIStatus.granted
			|| this.getStatus() == PKIStatus.grantedWithMods) {
		throw new TSPException("no time stamp token found and one expected");
	}
}

public byte[] getEncoded() throws Asn1Exception {
	final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
	resp.encode(asnBuf, true);
	return asnBuf.getMsgCopy();
}
}