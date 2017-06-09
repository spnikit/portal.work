package com.aisa.cms;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;

/**
 * created 09.11.2009 10:50:11 by @author kunina
 * last modified $Date: 2011-03-10 11:38:16 +0300 (Чт, 10 мар 2011) $ by $Author: olgakunina $
 */
public class CMSSignedData {

private final SignedData signData;
private OID[] digestOids;
private X509Certificate[] certificates;
private CMSSignerInfo[] infos;

public CMSSignedData(byte[] message) throws CMSException {
	if (message == null)
		throw new CMSException("No message");
	try {
		final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(message);
		final ContentInfo all = new ContentInfo();
		all.decode(asnBuf);
		// signedData
		signData = (SignedData) all.content;
	} catch (final Exception e) {
		final CMSException ex = new CMSException();
		ex.initCause(e);
		throw ex;
	}
}

public SignedData getSignedData() {
	return signData;
}

public OID getEContentTyprOID() {
	return new OID(signData.encapContentInfo.eContentType.value);
}

public X509Certificate[] getCertificates() throws CMSException {
	if (certificates == null) {
		if (signData.certificates != null) {
			try {
				final CertificateFactory cf =
						CertificateFactory.getInstance("X.509");
				certificates =
						new X509Certificate[signData.certificates.elements.length];
				for (int i = 0; i < signData.certificates.elements.length; i++) {
					final Asn1BerEncodeBuffer encBuf =
							new Asn1BerEncodeBuffer();
					signData.certificates.elements[i].encode(encBuf);
					certificates[i] =
							(X509Certificate) cf.generateCertificate(encBuf
								.getInputStream());
				}
			} catch (final Exception e) {
				final CMSException ex = new CMSException();
				ex.initCause(e);
				throw ex;
			}
		} else
			certificates = null;
	}
	return certificates;
}

public OID[] getDigestOIDs() {
	if (digestOids == null) {
		if (signData.digestAlgorithms.elements != null) {
			digestOids = new OID[signData.digestAlgorithms.elements.length];
			for (int i = 0; i < signData.digestAlgorithms.elements.length; i++) {
				digestOids[i] =
						new OID(
							signData.digestAlgorithms.elements[i].algorithm.value);
			}
		} else
			digestOids = null;
	}
	return digestOids;
}

public CMSSignerInfo[] getInfos() {
	if (infos == null) {
		if (signData.signerInfos != null) {
			infos = new CMSSignerInfo[signData.signerInfos.elements.length];
			for (int i = 0; i < infos.length; i++)
				try {
					infos[i] =
							new CMSSignerInfo(signData.signerInfos.elements[i]);
				} catch (final CMSException e) {
					infos[i] = null;
				}
		} else
			infos = null;
	}
	return infos;
}
}
