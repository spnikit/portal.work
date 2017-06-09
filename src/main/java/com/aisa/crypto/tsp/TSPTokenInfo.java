package com.aisa.crypto.tsp;

import java.math.BigInteger;
import java.util.Date;

import ru.CryptoPro.JCP.ASN.CertificateExtensions.GeneralName;
import ru.CryptoPro.JCP.ASN.PKIXTSP.Accuracy;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TSTInfo;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-10-27 17:04:51 +0300 (Вт, 27 окт 2009) $ by
 * $Author: kunina $
 */
public class TSPTokenInfo {

TSTInfo tstInfo;
Date genTime;

TSPTokenInfo(TSTInfo tstInfo) throws TSPException {
	this.tstInfo = tstInfo;
	try {
		this.genTime = tstInfo.genTime.getTime().getTime();
	} catch (final Asn1Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public boolean isOrdered() {
	return tstInfo.ordering.value;
}

public Accuracy getAccuracy() {
	return tstInfo.accuracy;
}

public Date getGenTime() {
	return genTime;
}

public String getPolicy() {
	return tstInfo.policy.toString();
}

public BigInteger getSerialNumber() {
	return tstInfo.serialNumber.value;
}

public GeneralName getTsa() {
	return tstInfo.tsa;
}

public BigInteger getNonce() {
	return tstInfo.nonce.value;
}

public Asn1ObjectIdentifier getMessageImprintAlg() {
	return tstInfo.messageImprint.hashAlgorithm.algorithm;
}

public byte[] getMessageImprintDigest() {
	return tstInfo.messageImprint.hashedMessage.value;
}

public byte[] getEncoded() throws Asn1Exception {
	final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
	tstInfo.encode(asnBuf, true);
	return asnBuf.getMsgCopy();
}

public TSTInfo toTSTInfo() {
	return tstInfo;
}
}
