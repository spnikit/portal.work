package cms;



 
import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Null;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.*;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Name;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.params.OID;
 

 
 
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.DigestInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
 
import java.security.cert.X509Certificate;

 

public class CmsTools {
 
 
	public static final String CERT_EXT = ".cer";
 
	public static final String CMS_EXT = ".p7b";
 
	public static final String SEPAR = File.separator;

 
	public static final String SIGN_KEY_NAME = "signkey";
	public static final char[] SIGN_KEY_PASSWORD = null;
 
 
	public static final String RECIP_KEY_NAME = "recipkey";
	public static final char[] RECIP_KEY_PASSWORD = null;
 

 
	public static final String STORE_TYPE = "HDImageStore";
	public static final String KEY_ALG_NAME = JCP.GOST_DH_NAME;
	public static final String DIGEST_ALG_NAME = JCP.GOST_DIGEST_NAME;

	public static final String SEC_KEY_ALG_NAME = "GOST28147";

 
	public static final String STR_CMS_OID_DATA = "1.2.840.113549.1.7.1";
	public static final String STR_CMS_OID_SIGNED = "1.2.840.113549.1.7.2";
	public static final String STR_CMS_OID_ENVELOPED = "1.2.840.113549.1.7.3";

	public static final String STR_CMS_OID_CONT_TYP_ATTR = "1.2.840.113549.1.9.3";
	public static final String STR_CMS_OID_DIGEST_ATTR = "1.2.840.113549.1.9.4";
	public static final String STR_CMS_OID_SIGN_TYM_ATTR = "1.2.840.113549.1.9.5";

	public static final String STR_CMS_OID_TS = "1.2.840.113549.1.9.16.1.4";

	public static final String DIGEST_OID = JCP.GOST_DIGEST_OID;
	public static final String SIGN_OID = JCP.GOST_EL_KEY_OID;

 
 
 

	public static synchronized  PrivateKey loadKey(String name, char[] password)
			throws Exception {
		
		
		final KeyStore hdImageStore = KeyStore.getInstance("HDImageStore");
		
		 KeyStore ks = KeyStore.getInstance("MemoryStore0", "JCP");
		   ks.load(null, null);
		   hdImageStore.load(null, null);
		
		if (password.length<1) password=null;
		
		
		  PrivateKey key = (PrivateKey)hdImageStore.getKey(name, password);
		    System.out.println("key "+ key);
		    X509Certificate certificate = (X509Certificate) hdImageStore.getCertificate(name);
		    Certificate[] chain = new Certificate[]{certificate};
		 //   Certificate[] chain = hdImageStore.getCertificateChain(name);
		//    Certificate cert = hdImageStore.getCertificate(name);
		 

		    ks.setKeyEntry(name, key, password, chain);
		  //  ks.setCertificateEntry("0", certificate);
		    System.out.println("name; "+ name);
		    System.out.println("ks.getKey(name, password); "+ ks.getKey(name, password));
	     return (PrivateKey) ks.getKey(name, password);
	    ///return (PrivateKey) hdImageStore.getKey(name, password);

	}
	public static  byte[] ConstruactCMSSign(byte[] data,  String alias, String passwd, PrivateKey key ) throws Exception{
		
		return	CMSSign(data,loadKey(alias,passwd.toCharArray()), loadCertificate(alias), false);

		
	}
	public static byte[] ConstruactCMSSignTrue(byte[] data,  String alias, String passwd , PrivateKey key,Certificate certificate ) throws Exception{
		
		return	CMSSign(data, key, certificate, true);

		
	}
	public static Certificate loadCertificate(String name) throws Exception {
		final KeyStore hdImageStore = KeyStore.getInstance("HDImageStore","JCP");
		hdImageStore.load(null, null);
		return hdImageStore.getCertificate(name);

	}
 
 
	
	public static byte[] CMSSign(byte[] data, PrivateKey key, Certificate cert,
			boolean detached) throws Exception {
		// sign
		final Signature signature = Signature
				.getInstance(JCP.GOST_EL_SIGN_NAME);
		signature.initSign(key);
		signature.update(data);
		final byte[] sign = signature.sign();
		// create cms format
		return createCMS(data, sign, cert, detached);
	}


	public static  byte[]  createCMS(byte[] buffer, byte[] sign,
			Certificate cert, boolean detached) throws Exception {
		final ContentInfo all = new ContentInfo();
		all.contentType = new Asn1ObjectIdentifier(
				new OID(STR_CMS_OID_SIGNED).value);
		final SignedData cms = new SignedData();
		all.content = cms;
		cms.version = new CMSVersion(1);
		// digest
		cms.digestAlgorithms = new DigestAlgorithmIdentifiers(1);
		final DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(
				new OID(DIGEST_OID).value);
		a.parameters = new Asn1Null();
		cms.digestAlgorithms.elements[0] = a;
		if (detached)
			cms.encapContentInfo = new EncapsulatedContentInfo(
					new Asn1ObjectIdentifier(new OID(STR_CMS_OID_DATA).value),
					null);
		else
			cms.encapContentInfo = new EncapsulatedContentInfo(
					new Asn1ObjectIdentifier(new OID(STR_CMS_OID_DATA).value),
					new Asn1OctetString(buffer));
		// certificate
		cms.certificates = new CertificateSet(1);
		final ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate certificate = new ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate();
		final Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(
				cert.getEncoded());
		certificate.decode(decodeBuffer);
		cms.certificates.elements = new CertificateChoices[1];
		cms.certificates.elements[0] = new CertificateChoices();
		cms.certificates.elements[0].set_certificate(certificate);

		// signer info
		cms.signerInfos = new SignerInfos(1);
		cms.signerInfos.elements[0] = new SignerInfo();
		cms.signerInfos.elements[0].version = new CMSVersion(1);
		cms.signerInfos.elements[0].sid = new SignerIdentifier();

		final byte[] encodedName = ((X509Certificate) cert)
				.getIssuerX500Principal().getEncoded();
		final Asn1BerDecodeBuffer nameBuf = new Asn1BerDecodeBuffer(encodedName);
		final Name name = new Name();
		name.decode(nameBuf);

		final CertificateSerialNumber num = new CertificateSerialNumber(
				((X509Certificate) cert).getSerialNumber());
		cms.signerInfos.elements[0].sid
				.set_issuerAndSerialNumber(new IssuerAndSerialNumber(name, num));
		cms.signerInfos.elements[0].digestAlgorithm = new DigestAlgorithmIdentifier(
				new OID(DIGEST_OID).value);
		cms.signerInfos.elements[0].digestAlgorithm.parameters = new Asn1Null();
		cms.signerInfos.elements[0].signatureAlgorithm = new SignatureAlgorithmIdentifier(
				new OID(SIGN_OID).value);
		cms.signerInfos.elements[0].signatureAlgorithm.parameters = new Asn1Null();
		cms.signerInfos.elements[0].signature = new SignatureValue(sign);
		// encode
		final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
		all.encode(asnBuf, true);
		return asnBuf.getMsgCopy();
	}
	public static byte[] digestm(byte[] bytes, String digestAlgorithmName)
	        throws Exception {
	    //calculation messageDigest
	    final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
	    final java.security.MessageDigest digest =  java.security.MessageDigest.getInstance(digestAlgorithmName);
	    final DigestInputStream digestStream =
	            new DigestInputStream(stream, digest);
	    while (digestStream.available() != 0) digestStream.read();
	    return digest.digest();
	}
}
