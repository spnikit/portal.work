package com.aisa.portal.invoice.integration.security;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Attribute;
import ru.CryptoPro.JCP.KeyStore.HDImage.HDImageStore;
import ru.CryptoPro.JCP.params.OID;
import ru.CryptoPro.JCP.tools.Array;
import sheff.rjd.utils.SecurityManager;
import cms.CmsTools;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Type;
public class Manager extends SecurityManager{
 
 	private String keyname;
 	
 	private HashMap<String,KeyObj> keymap=new HashMap <String,KeyObj>();
 	private HashMap<String,Certificate> certmap=new HashMap <String,Certificate>();
 	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	private Certificate certificate;
	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	PrivateKey key=null;
	public PrivateKey getKey() {
		return key;
	}

	public void setKey(PrivateKey key) {
		this.key = key;
	}

	private String trustStore="";
	public String getTrustStore() {
		return trustStore;
	}
	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	private String hDImageStoreDir="";
	public String gethDImageStoreDir() {
		return hDImageStoreDir;
	}
	public void sethDImageStoreDir(String hDImageStoreDir) {
		this.hDImageStoreDir = hDImageStoreDir;
	}
	private String trustStorePassword="";
	private String keyStorePassword=""; 
 
	private static Logger log = Logger.getLogger(Manager.class);
public Manager(Resource pref, String ivector, String clientPass, String hDImageStoreDir,String TrustStore,String TrustStorePassword, String[] KeyStorePassword, String[] KeyName) {
		
	
	super(pref, ivector, clientPass);
	try { 
		String localpath=pref.getFile().getParent();
		
		this.sethDImageStoreDir(localpath+hDImageStoreDir);
		this.setTrustStore(localpath+TrustStore);
		this.setTrustStorePassword(TrustStorePassword);
		//this.setKeyStorePassword(KeyStorePassword);
		//this.setKeyname(KeyName);
		

	  if (gethDImageStoreDir().length()>0) HDImageStore.setDir(gethDImageStoreDir());
	    System.out.println("HDImageStore.getDir()"+HDImageStore.getDir()); 
	    System.out.println(" getTrustStore()"+ getTrustStore()); 
	    System.setProperty("javax.net.ssl.trustStoreType", "HDImageStore");
	 	System.setProperty("javax.net.ssl.trustStore", getTrustStore());
	 	System.setProperty("javax.net.ssl.trustStorePassword", getTrustStorePassword()); 
	 	System.setProperty("javax.net.ssl.keyStorePassword", getKeyStorePassword());
    
	 	 KeyStore hdImageStore = KeyStore.getInstance("HDImageStore"); 
		for (int i=0; i<KeyStorePassword.length;i++){
			
	 	 KeyStore ks = KeyStore.getInstance("MemoryStore", "JCP");
		 ks.load(null, null);
		 KeyObj keyobj=new KeyObj();
		    hdImageStore.load(null, null);
		    char[] password = KeyStorePassword[i].toCharArray();
		    if (password.length<1) password=null; 
		    PrivateKey pkey = (PrivateKey)hdImageStore.getKey(KeyName[i], password); 
		    certificate = (X509Certificate) hdImageStore.getCertificate(KeyName[i]);
		    Certificate[] chain = new Certificate[]{certificate};  
		    ks.setKeyEntry(KeyName[i], pkey, password, chain);  
	        key= (PrivateKey) ks.getKey(KeyName[i], password);
	        keyobj.setKey(key);
	        keyobj.setCertificate(certificate);
	        keymap.put( KeyName[i],keyobj);
	        certmap.put( KeyName[i],certificate);
	       
	        
		}
	} catch (Exception e) {
		 
		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(outError);
		e.printStackTrace(errorWriter);
		log.error(outError.toString());
	}
	
	 	
	 	System.out.println("DONE"); 
  	
	 
	}

public boolean CheckSignatureXML (byte[] xml, byte[] sgn){
	int count=0;
	try {
		// System.out.println("manager CheckSignatureXML");
		//CMSSignedData data=new CMSSignedData(sgn); 
		count =CMSVerify(sgn,null,xml);
	 
	} catch (Exception e) { 
		count=0;
		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(outError);
		e.printStackTrace(errorWriter);
		log.error(outError.toString());
	}
	if ( count>0){
		return true;
	}
	else
	return false;
	
}

public Certificate getCert(String alias){
	return certmap.get(alias); 
}
public  int CMSVerify(byte[] buffer, Certificate[] certs, byte[] data)
        throws Exception {
	//System.out.println("manager CMSVerify");
    //clear buffers fo logs
	 int validsign=0;
    final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(buffer);
    final ContentInfo all = new ContentInfo();
    all.decode(asnBuf);
    if (!new OID("1.2.840.113549.1.7.2").eq(all.contentType.value))
        throw new Exception("Not supported");
    final SignedData cms = (SignedData) all.content;
    final byte[] text;
  /*  if (cms.encapContentInfo.eContent != null){
        text = cms.encapContentInfo.eContent.value;
        
    }
    else if (data != null) text = data;
    else throw new Exception("No content for verify");*/
    text = data;
    OID digestOid = null;
    
    final DigestAlgorithmIdentifier digestAlgorithmIdentifier =
            new DigestAlgorithmIdentifier(new OID(JCP.GOST_DIGEST_OID).value);
    for (int i = 0; i < cms.digestAlgorithms.elements.length; i++) {
        if (cms.digestAlgorithms.elements[i].algorithm
                .equals(digestAlgorithmIdentifier.algorithm)) {
            digestOid =
                    new OID(cms.digestAlgorithms.elements[i].algorithm.value);
            break;
        }
    }
    if (digestOid == null)
        throw new Exception("Unknown digest");
    final OID eContTypeOID = new OID(cms.encapContentInfo.eContentType.value);
    if (cms.certificates != null) {
        //Проверка на вложенных сертификатах
        log.debug("Validation on certificates founded in CMS.");
        for (int i = 0; i < cms.certificates.elements.length; i++) {
            final Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
            cms.certificates.elements[i].encode(encBuf);

            final CertificateFactory cf =
                    CertificateFactory.getInstance("X.509");
            final X509Certificate cert =
                    (X509Certificate) cf
                            .generateCertificate(encBuf.getInputStream());

            for (int j = 0; j < cms.signerInfos.elements.length; j++) {
                final SignerInfo info = cms.signerInfos.elements[j];
                if (!digestOid
                        .equals(new OID(info.digestAlgorithm.algorithm.value)))
                    throw new Exception("Not signed on certificate.");
                final boolean checkResult = verifyOnCert(cert,
                        cms.signerInfos.elements[j], text, eContTypeOID);
                if (checkResult) validsign++;
            }
        }
    } else if (certs != null) {
        //Проверка на указанных сертификатах
    	 log.debug("Certificates for validation not found in CMS.\n" +
                "      Try verify on specified certificates...");
        for (int i = 0; i < certs.length; i++) {
            final X509Certificate cert = (X509Certificate) certs[i];
            for (int j = 0; j < cms.signerInfos.elements.length; j++) {
                final SignerInfo info = cms.signerInfos.elements[j];
                if (!digestOid.equals(new OID(  info.digestAlgorithm.algorithm.value)))
                    throw new Exception("Not signed on certificate.");
                final boolean checkResult = verifyOnCert(cert,
                        cms.signerInfos.elements[j], text, eContTypeOID);
                if (checkResult) validsign++;
            }
        }
    } else {
    	 log.warn("Certificates for validation not found");
    }
   
	if (validsign == 0) throw new Exception("Signatures are invalid");
    if (cms.signerInfos.elements.length > validsign)
        throw new Exception("Some signatures are invalid");
    else log.debug("All signatures are valid");
	return validsign;
}

/**
 * Попытка проверки подписи на указанном сертификате
 *
 * @param cert сертификат для проверки
 * @param text текст для проверки
 * @param info подпись
 * @return верна ли подпись
 * @throws Exception ошибки
 */
private static boolean verifyOnCert(X509Certificate cert, SignerInfo info,
                                    byte[] text, OID eContentTypeOID)
        throws Exception {
    //подпись
//	System.out.println("verifyOnCert");
    final byte[] sign = info.signature.value;
    //данные для проверки подписи
    final byte[] data;
    if (info.signedAttrs == null) {
        //аттрибуты подписи не присутствуют
        //данные для проверки подписи
    	
        data = text;
    } else {
        //присутствуют аттрибуты подписи (SignedAttr)
        final Attribute[] signAttrElem = info.signedAttrs.elements;

        //проверка аттрибута content-type
        final Asn1ObjectIdentifier contentTypeOid = new Asn1ObjectIdentifier(
                (new OID("1.2.840.113549.1.9.3")).value);
        Attribute contentTypeAttr = null;

        for (int r = 0; r < signAttrElem.length; r++) {
            final Asn1ObjectIdentifier oid = signAttrElem[r].type;
            if (oid.equals(contentTypeOid)) {
                contentTypeAttr = signAttrElem[r];
            }
        }

        if (contentTypeAttr == null)
            throw new Exception("content-type attribute not present");

        if (!contentTypeAttr.values.elements[0]
                .equals(new Asn1ObjectIdentifier(eContentTypeOID.value)))
            throw new Exception(
                    "content-type attribute OID not equal eContentType OID");

        //проверка аттрибута message-digest
        final Asn1ObjectIdentifier messageDigestOid = new Asn1ObjectIdentifier(
                (new OID("1.2.840.113549.1.9.4")).value);

        Attribute messageDigestAttr = null;

        for (int r = 0; r < signAttrElem.length; r++) {
            final Asn1ObjectIdentifier oid = signAttrElem[r].type;
            if (oid.equals(messageDigestOid)) {
                messageDigestAttr = signAttrElem[r];
            }
        }

        if (messageDigestAttr == null)
            throw new Exception("message-digest attribute not present");

        final Asn1Type open = messageDigestAttr.values.elements[0];
        final Asn1OctetString hash = (Asn1OctetString) open;
        final byte[] md = hash.value;

        //вычисление messageDigest
        final byte[] dm = CmsTools.digestm(text, CmsTools.DIGEST_ALG_NAME);

        if (!Array.toHexString(dm).equals(Array.toHexString(md)))
            throw new Exception("message-digest attribute verify failed");

        //проверка аттрибута signing-time
        final Asn1ObjectIdentifier signTimeOid = new Asn1ObjectIdentifier(
                (new OID(CmsTools.STR_CMS_OID_SIGN_TYM_ATTR)).value);

        Attribute signTimeAttr = null;

        for (int r = 0; r < signAttrElem.length; r++) {
            final Asn1ObjectIdentifier oid = signAttrElem[r].type;
            if (oid.equals(messageDigestOid)) {
                signTimeAttr = signAttrElem[r];
            }
        }

        if (signTimeAttr != null) {
            //проверка (необязательно)
        }

        //данные для проверки подписи
        final Asn1BerEncodeBuffer encBufSignedAttr = new Asn1BerEncodeBuffer();
        info.signedAttrs.encode(encBufSignedAttr);
        data = encBufSignedAttr.getMsgCopy();
    }
    // проверка подписи
    final Signature signature =
            Signature.getInstance(JCP.GOST_EL_SIGN_NAME);
    signature.initVerify(cert);
    signature.update(data);
    return signature.verify(sign);
}



public byte[] ConsrtuctSignature(byte [] xml, String alias) throws Exception{
     KeyObj kkey = keymap.get(alias);
	return CmsTools.ConstruactCMSSignTrue(xml,null,null,kkey.getKey(),kkey.getCertificate());
	 
}
}
