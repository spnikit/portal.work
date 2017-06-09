package com.aisa.cms;

import java.util.HashMap;
import java.util.Map;

import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Attribute;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;

/**
 * created 09.11.2009 11:03:59 by @author kunina
 * last modified $Date: 2011-03-10 11:38:16 +0300 (Чт, 10 мар 2011) $ by
 * $Author: olgakunina $
 */
public class CMSSignerInfo {

private final SignerInfo info;
private Map<String, Attribute> signAttrs;

public CMSSignerInfo(SignerInfo signerInfo) throws CMSException {
	if (signerInfo == null)
		throw new CMSException("SignerInfo is null");
	info = signerInfo;
}

public byte[] getSignatureValue() {
	return info.signature.value;
}

public Map<String, Attribute> getSignedAttrs() {
	if (signAttrs == null) {
		if (info.signedAttrs != null && info.signedAttrs.elements != null) {
			signAttrs = new HashMap<String, Attribute>(0);
			for (int i = 0; i < info.signedAttrs.elements.length; i++) {
				signAttrs.put(new OID(info.signedAttrs.elements[i].type.value)
					.toString(), info.signedAttrs.elements[i]);
			}
		} else
			signAttrs = null;
	}
	return signAttrs;
}

public byte[] getEncodedSignedAttrs() throws CMSException {
	final Asn1BerEncodeBuffer encBufSignedAttr = new Asn1BerEncodeBuffer();
	try {
		info.signedAttrs.encode(encBufSignedAttr);
	} catch (final Asn1Exception e) {
		final CMSException ex = new CMSException();
		ex.initCause(e);
		throw ex;
	}
	return encBufSignedAttr.getMsgCopy();
}

public SignerInfo getSignerInfo() {
	return info;
}

public OID getSignAlgorithm() {
	return new OID(info.signatureAlgorithm.algorithm.value);
}

public OID getDigestAlgorithm() {
	return new OID(info.digestAlgorithm.algorithm.value);
}

public SignerIdentifier getSignerID() {
	return info.sid;
}
}
