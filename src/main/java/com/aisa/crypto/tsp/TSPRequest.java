package com.aisa.crypto.tsp;

import java.security.MessageDigest;
import java.security.NoSuchProviderException;

import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.AlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.PKIXTSP.MessageImprint;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TimeStampReq;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TimeStampReq_version;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-10-27 16:46:22 +0300 (Вт, 27 окт 2009) $ by
 * $Author: kunina $
 */
public class TSPRequest {

private final String algOID;
private final byte[] message;
private final String provider;

public TSPRequest(String algOID, byte[] message, String provider)
	throws TSPException {
	if (algOID == null)
		throw new TSPException("null algorithm");
	this.algOID = algOID;
	this.message = message;
	this.provider = provider;
}

private byte[] getDigest(byte[] mes) throws TSPException {
	try {
		MessageDigest digest = null;
		if (provider != null)
			try {
				digest = MessageDigest.getInstance(algOID, provider);
			} catch (final NoSuchProviderException e) {
				//TODO ignore?
			}
		if (digest == null)
			digest = MessageDigest.getInstance(algOID);
		digest.update(mes);
		final byte[] result = digest.digest();
		return result;
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public TimeStampReq getTSReq() throws TSPException {
	try {
		final byte[] digest = getDigest(message);
		final TimeStampReq tsr = new TimeStampReq();
		tsr.version =
				new TimeStampReq_version(String
					.valueOf(TimeStampReq_version.v1));
		final MessageImprint mes = new MessageImprint();
		mes.hashAlgorithm = new AlgorithmIdentifier(new OID(algOID).value);
		mes.hashedMessage = new Asn1OctetString(digest);
		tsr.messageImprint = mes;
		return tsr;
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public byte[] getReq() throws TSPException {
	try {
		final TimeStampReq tsr = getTSReq();
		if (tsr == null)
			return null;
		final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
		tsr.encode(asnBuf, true);
		return asnBuf.getMsgCopy();
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}
}
