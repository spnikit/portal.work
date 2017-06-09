package com.aisa.crypto.tsp;

import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import ru.CryptoPro.JCP.ASN.CertificateExtensions.GeneralName;
import ru.CryptoPro.JCP.ASN.CertificateExtensions.KeyPurposeId;
import ru.CryptoPro.JCP.ASN.CertificateExtensions._extKeyUsage_ExtnType;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CertificateChoices;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifiers;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedAttributes;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.UnsignedAttributes;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Attribute;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateList;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.ESSCertID;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.ESSCertIDv2;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Extension;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.IssuerSerial;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.SigningCertificate;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.SigningCertificateV2;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.TBSCertificate;
import ru.CryptoPro.JCP.ASN.PKIXTSP.TSTInfo;
import ru.CryptoPro.JCP.params.OID;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Type;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-11-03 17:47:06 +0300 (Р’С‚, 03 РЅРѕСЏ 2009) $ by
 * $Author: kunina $
 */
public class TSPToken {

public static final String CONT_TYP_ATTR_OID = "1.2.840.113549.1.9.3";
public static final String DIGEST_ATTR_OID = "1.2.840.113549.1.9.4";
public static final String SIGN_TYM_ATTR_OID = "1.2.840.113549.1.9.5";
public static final String TSP_OID = "1.2.840.113549.1.9.16.1.4";
public static final String SIGN_CERT_OID = "1.2.840.113549.1.9.16.2.12";
public static final String SIGN_CERT_V2_OID = "1.2.840.113549.1.9.16.2.47";
public static final String EXT_KEY_USAGE_OID = "2.5.29.37";
public static final String TS_USAGE_OID = "1.3.6.1.5.5.7.3.8";

SignedData tsToken;

SignerInfo tsaSignerInfo;

Date genTime;

TSPTokenInfo tstInfo;

CertID certID;
CertID certIDv2;

public TSPToken(ContentInfo contentInfo) throws TSPException {
	this((SignedData) contentInfo.content);
}

public TSPToken(SignedData signedData) throws TSPException {
	this.tsToken = signedData;
	if (!this.tsToken.encapContentInfo.eContentType
		.equals(new Asn1ObjectIdentifier(new OID(TSP_OID).value))) {
		throw new TSPException("ContentInfo object not for a time stamp");
	}
	final SignerInfo[] signs = tsToken.signerInfos.elements;
	if (signs.length != 1)
		throw new IllegalArgumentException("Time-stamp token signed by "
				+ signs.length
				+ " signers, but it must contain just the TSA signature");
	tsaSignerInfo = signs[0];
	try {
		final Asn1BerDecodeBuffer asnDBuf =
				new Asn1BerDecodeBuffer(tsToken.encapContentInfo.eContent.value);
		final TSTInfo tstInf = new TSTInfo();
		tstInf.decode(asnDBuf);
		this.tstInfo = new TSPTokenInfo(tstInf);

		//verify sign certificate and set certID
		verifySignCert();

	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public void verifySignCert() throws TSPException {
	final SigningCertificate cert = getSignCert();
	final SigningCertificateV2 certV2 = getSignCertV2();
	if (cert == null && certV2 == null) {
		throw new TSPException(
			"no signing certificate attribute present, time stamp invalid");
	}
	if (cert != null)
		this.certID = new CertID(cert.certs.elements[0]);
	if (certV2 != null)
		this.certIDv2 = new CertID(certV2.certs.elements[0]);
}

public SigningCertificate getSignCert() throws TSPException {
	final Attribute[] attrs = getSignedAttributes().elements;
	for (int i = 0; i < attrs.length; i++) {
		if (attrs[i].type.equals(new Asn1ObjectIdentifier(
			new OID(SIGN_CERT_OID).value))) {
			try {
				return (SigningCertificate) attrs[i].values.elements[0];
			} catch (final ClassCastException e) {
				try {
					final SigningCertificate s = new SigningCertificate();
					final Asn1DerDecodeBuffer dec =
							new Asn1DerDecodeBuffer(
								((Asn1OctetString) attrs[i].values.elements[0]).value);
					s.decode(dec);
					return s;
				} catch (final Exception e1) {
					final TSPException ex =
							new TSPException("Can't parse SigningCertificate");
					throw ex;
				}
			}
		}
	}
	return null;
}

public SigningCertificateV2 getSignCertV2() throws TSPException {
	final Attribute[] attrs = getSignedAttributes().elements;
	for (int i = 0; i < attrs.length; i++) {
		if (attrs[i].type.equals(new Asn1ObjectIdentifier(new OID(
			SIGN_CERT_V2_OID).value))) {
			try {
				return (SigningCertificateV2) attrs[i].values.elements[0];
			} catch (final ClassCastException e) {
				try {
					final SigningCertificateV2 s2 = new SigningCertificateV2();
					final Asn1DerDecodeBuffer dec =
							new Asn1DerDecodeBuffer(
								((Asn1OctetString) attrs[i].values.elements[0]).value);
					s2.decode(dec);
					return s2;
				} catch (final Exception e1) {
					final TSPException ex =
							new TSPException("Can't parse SigningCertificateV2");
					throw ex;
				}
			}
		}
	}
	return null;
}

public TSPTokenInfo getTimeStampInfo() {
	return tstInfo;
}

public SignerIdentifier getSID() {
	return tsaSignerInfo.sid;
}

public SignedAttributes getSignedAttributes() {
	return tsaSignerInfo.signedAttrs;
}

public UnsignedAttributes getUnsignedAttributes() {
	return tsaSignerInfo.unsignedAttrs;
}

public byte[] getSignatureValue() {
	return tsaSignerInfo.signature.value;
}

public CertStore getCertificatesAndCRLs(String type) throws TSPException {
	final List<X509Extension> certsAndCRLs = new ArrayList<X509Extension>(0);
	try {
		final CertificateFactory cf = CertificateFactory.getInstance(type);
		if (tsToken.certificates != null) {
			final CertificateChoices[] certs = tsToken.certificates.elements;
			for (int i = 0; i < certs.length; i++) {
				final Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
				certs[i].encode(encBuf);
				final X509Certificate cert =
						(X509Certificate) cf.generateCertificate(encBuf
							.getInputStream());
				certsAndCRLs.add(cert);
			}
		}
		if (tsToken.crls != null) {
			final CertificateList[] crls = tsToken.crls.elements;
			for (int i = 0; i < crls.length; i++) {
				final Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
				crls[i].encode(encBuf);
				final X509CRL crl =
						(X509CRL) cf.generateCRL(encBuf.getInputStream());
				certsAndCRLs.add(crl);
			}
		}
		final CollectionCertStoreParameters par =
				new CollectionCertStoreParameters(certsAndCRLs);

		final CertStore cs = CertStore.getInstance("Collection", par);
		return cs;
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

/**
 * Validate the time stamp token.
 * 
 * @throws TSPException something wrong/not valid
 */
public void validate(X509Certificate cert, String provider) throws TSPException {
	try {
		CertID cid = null;
		if (MessageDigest.isEqual(certID.getCertHash(), MessageDigest
			.getInstance(certID.getHashAlgorithm()).digest(cert.getEncoded()))) {
			cid = certID;
		} else if (MessageDigest
			.isEqual(certIDv2.getCertHash(), MessageDigest.getInstance(
				certIDv2.getHashAlgorithm()).digest(cert.getEncoded()))) {
			cid = certIDv2;
		}
		if (cid == null)
			throw new TSPException(
				"certificate hash does not match certID hash");
		if (cid.getIssuerSerial() != null) {
			if (!cid.getIssuerSerial().serialNumber.value.equals(cert
				.getSerialNumber()))
				throw new TSPException(
					"certificate serial number does not match certID for signature");

			final Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
			final GeneralName[] el = cid.getIssuerSerial().issuer.elements;
			for (int i = 0; i < el.length; i++)
				el[i].getElement().encode(enc);
			final byte[] uu = enc.getMsgCopy();
			final byte[] u = cert.getIssuerX500Principal().getEncoded();
			final X500Principal pp = new X500Principal(uu);
			final X500Principal p = new X500Principal(u);
			if (!p.equals(pp))
				throw new TSPException(
					"certificate name does not match certID for signature");
		}

		//validate key usage
		validateCert(cert);

		cert.checkValidity(tstInfo.getGenTime());

		if (!signatureVerify(cert, provider))
			throw new TSPException("signature not created by certificate");

	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

public SignedData toSignedData() {
	return tsToken;
}

public byte[] getEncoded() throws Asn1Exception {
	final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
	tsToken.encode(asnBuf, true);
	return asnBuf.getMsgCopy();
}

private void validateCert(X509Certificate cert) throws TSPException {
	try {
		if (cert.getVersion() != 3) {
			throw new IllegalArgumentException(
				"Certificate must have an ExtendedKeyUsage extension");
		}
		final TBSCertificate tcert = new TBSCertificate();
		final Asn1DerDecodeBuffer asnDerBuf =
				new Asn1DerDecodeBuffer(cert.getTBSCertificate());
		tcert.decode(asnDerBuf);
		final Extension[] exts = tcert.extensions.elements;
		boolean found = false;
		for (int i = 0; i < exts.length; i++) {
			if (new OID(exts[i].extnID.value)
				.equals(new OID(EXT_KEY_USAGE_OID))) {
				found = true;

				//TODO возможно не должно быть критическим
				//if (!exts[i].critical.value)
				//	throw new TSPException(
				//		"Certificate must have an ExtendedKeyUsage extension marked as critical");

				final Asn1DerDecodeBuffer derbuf =
						new Asn1DerDecodeBuffer(exts[i].extnValue.value);
				final _extKeyUsage_ExtnType t = new _extKeyUsage_ExtnType();
				t.decode(derbuf);
				final KeyPurposeId[] p = t.elements;
				boolean fnd = false;
				for (int j = 0; j < p.length; j++) {
					if (new OID(p[j].value).equals(new OID(TS_USAGE_OID))) {
						fnd = true;
						break;
					}
				}
				if (!fnd)
					throw new TSPException(
						"ExtendedKeyUsage not solely time stamping");
				break;
			}
		}
		if (!found)
			throw new TSPException(
				"Certificate must have an ExtendedKeyUsage extension");
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

private boolean signatureVerify(X509Certificate cert, String provider)
		throws TSPException {
	try {
		if (getSignedAttributes() == null)
			throw new TSPException("attributes not present");
		final Attribute[] signAttrElem = getSignedAttributes().elements;
		//verify content-type
		final Asn1ObjectIdentifier contentTypeOid =
				new Asn1ObjectIdentifier(new OID(CONT_TYP_ATTR_OID).value);
		Attribute contentTypeAttr = null;
		for (int i = 0; i < signAttrElem.length; i++) {
			final Asn1ObjectIdentifier oid = signAttrElem[i].type;
			if (oid.equals(contentTypeOid)) {
				contentTypeAttr = signAttrElem[i];
				break;
			}
		}
		if (contentTypeAttr == null)
			throw new TSPException("content-type attribute not present");

		if (!contentTypeAttr.values.elements[0]
			.equals(new Asn1ObjectIdentifier(
				toSignedData().encapContentInfo.eContentType.value)))
			throw new TSPException(
				"content-type attribute OID not equal eContentType OID");
		//verify message-digest
		final Asn1ObjectIdentifier messageDigestOid =
				new Asn1ObjectIdentifier(new OID(DIGEST_ATTR_OID).value);
		Attribute messageDigestAttr = null;
		for (int r = 0; r < signAttrElem.length; r++) {
			final Asn1ObjectIdentifier oid = signAttrElem[r].type;
			if (oid.equals(messageDigestOid)) {
				messageDigestAttr = signAttrElem[r];
			}
		}
		if (messageDigestAttr == null)
			throw new TSPException("message-digest attribute not present");
		final Asn1Type open = messageDigestAttr.values.elements[0];
		final Asn1OctetString hash = (Asn1OctetString) open;
		final DigestAlgorithmIdentifiers dalgs =
				toSignedData().digestAlgorithms;
		if (dalgs == null)
			throw new TSPException("No digest algorithm present");
		if (dalgs.elements.length != 1)
			throw new TSPException(
				"More/less then one digest algorithm present");
		final String mdAlg =
				new OID(dalgs.elements[0].algorithm.value).toString();
		MessageDigest digest = null;
		if (provider != null)
			try {
				digest = MessageDigest.getInstance(mdAlg, provider);
			} catch (final NoSuchProviderException e) {
				//TODO ignore?
			}
		if (digest == null)
			digest = MessageDigest.getInstance(mdAlg);
		if (!MessageDigest.isEqual(hash.value, digest
			.digest(toSignedData().encapContentInfo.eContent.value))) {
			throw new TSPException(
				"certificate hash does not match certID hash.");
		}
		// data
		final Asn1BerEncodeBuffer encBufSignedAttr = new Asn1BerEncodeBuffer();
		getSignedAttributes().encode(encBufSignedAttr);
		final byte[] data = encBufSignedAttr.getMsgCopy();
		// signature verify
		Signature signature = null;
		if (provider != null)
			try {
				signature =
						Signature.getInstance(cert.getSigAlgName(), provider);
			} catch (final NoSuchProviderException e) {
				//TODO ignore?
			}
		if (signature == null)
			signature = Signature.getInstance(cert.getSigAlgName());
		signature.initVerify(cert);
		signature.update(data);
		return signature.verify(getSignatureValue());
	} catch (final Exception e) {
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	}
}

private class CertID {

private final String NIST_SHA256 = "2.16.840.1.101.3.4.2.1";

private final ESSCertID cID;
private final ESSCertIDv2 cIDv2;

CertID(ESSCertID crtID) {
	this.cID = crtID;
	this.cIDv2 = null;
}

CertID(ESSCertIDv2 crtID) {
	this.cID = null;
	this.cIDv2 = crtID;
}

public String getHashAlgorithm() {
	if (cID != null) {
		return "SHA-1";
	} else {
		if (NIST_SHA256.equals(cIDv2.hashAlgorithm.algorithm.toString())) {
			return "SHA-256";
		}
		return new OID(cIDv2.hashAlgorithm.algorithm.value).toString();
	}
}

public byte[] getCertHash() {
	if (cID != null) {
		return cID.certHash.value;
	} else {
		return cIDv2.certHash.value;
	}
}

public IssuerSerial getIssuerSerial() {
	if (cID != null) {
		return cID.issuerSerial;
	} else {
		return cIDv2.issuerSerial;
	}
}
}
}
