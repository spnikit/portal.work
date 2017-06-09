package com.aisa.crypto.ocsp;

import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.AlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.SubjectPublicKeyInfo;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.CertID;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.OCSPRequest;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.ReqCert;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.Request;
import ru.CryptoPro.JCP.ASN.PKIXOCSP.TBSRequest;
import ru.CryptoPro.JCP.ASN.PKIXOCSP._SeqOfRequest;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OctetString;

/**
 * created 28.10.2009 10:27:33 by @author kunina
 * last modified $Date: 2009-10-29 15:14:38 +0300 (Р§С‚, 29 РѕРєС‚ 2009) $ by
 * $Author: kunina $
 */
//Упрощенная схема (без расширения nonce [1.3.6.1.5.5.7.48.1.2])

public class OCSPReq {

// Serial number of the certificates to be checked for revocation
private final CertificateSerialNumber serialNumber;
// CertId of the certificate to be checked
private CertID certId = null;
// hash algorithm identifier
private final String algOID;

public OCSPReq(X509Certificate userCert, X509Certificate issuerCert,
	String alg, String provider) throws OCSPException {
	if (issuerCert == null)
		throw new OCSPException("Null issuer certificate");
	if (userCert == null)
		throw new OCSPException("Null user certificate");
	if (alg == null)
		throw new OCSPException("Null hash algorithm");
	try {
		algOID = alg;

		serialNumber = new CertificateSerialNumber(userCert.getSerialNumber());

		certId = new CertID();
		certId.serialNumber = serialNumber;
		certId.hashAlgorithm = new AlgorithmIdentifier(new OID(algOID).value);
		MessageDigest digest = null;
		if (provider != null)
			try {
				digest = MessageDigest.getInstance(algOID, provider);
			} catch (final NoSuchProviderException e) {
				//TODO ignore?
			}
		if (digest == null)
			digest = MessageDigest.getInstance(algOID);
		final SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo();
		final Asn1BerDecodeBuffer dbuff =
				new Asn1BerDecodeBuffer(issuerCert.getPublicKey().getEncoded());
		spki.decode(dbuff);
		certId.issuerKeyHash =
				new Asn1OctetString(digest.digest(spki.subjectPublicKey.value));
		final X500Principal issuerName = issuerCert.getSubjectX500Principal();
		certId.issuerNameHash =
				new Asn1OctetString(digest.digest(issuerName.getEncoded()));
	} catch (final Exception e) {
		final OCSPException ex = new OCSPException();
		ex.initCause(e);
		throw ex;
	}
}

byte[] getReq() throws OCSPException {
	final ReqCert rc = new ReqCert();
	rc.set_certID(certId);
	final _SeqOfRequest rlist =
			new _SeqOfRequest(new Request[]{new Request(rc)});

	final TBSRequest tr = new TBSRequest();
	tr.requestList = rlist;

	final OCSPRequest or = new OCSPRequest(tr);

	final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
	try {
		or.encode(asnBuf, true);
	} catch (final Asn1Exception e) {
		final OCSPException ex = new OCSPException();
		ex.initCause(e);
		throw ex;
	}
	return asnBuf.getMsgCopy();
}

CertID getCertId() {
	return certId;
}

}
