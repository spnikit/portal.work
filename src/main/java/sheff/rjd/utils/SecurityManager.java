
package sheff.rjd.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.CryptoPro.Crypto.Key.GostSecretKey;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.Key.SecretKeyInterface;
import ru.CryptoPro.JCP.KeyStore.HDImage.HDImageStore;
import ru.CryptoPro.JCP.params.CryptParamsSpec;
import ru.aisa.rgd.etd.extend.SNMPSender;

/**
* 
* SecurityManager - verify Signature.
* 
* 
*/
public class SecurityManager {


private Cipher cipherEncrypt;
private Cipher cipherDecrypt;
private boolean isInintedCipher=false;



private static final String CIPHER_ALG ="GOST28147/CFB/NoPadding";

private NamedParameterJdbcTemplate npjt;
private OCSPutils ocsp;
private JCP jcp;
private String algorithm;
private static Logger log = Logger.getLogger(SecurityManager.class);

public SecurityManager(Resource pref, String ivector, String clientPass) {
try {
InputStream is = pref.getInputStream();

XmlSupport.importPreferences(is);
is.close();
initCipher(ivector,clientPass);
log.warn("security manager started successfully");
log.warn(provsInfo());
} catch (Exception e) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
e.printStackTrace(errorWriter);
log.error(outError.toString());
}
}

private void initCipher(String ivector, String clientPass){
try{
byte[] test=Base64.decode(ivector);//{ 0x01, 0x05, 0x62, 0x0A, 0x3E, 0x02, 0x09, (byte)0xFF };
IvParameterSpec ivspec = new IvParameterSpec(test);

byte[] clientPassword = Base64.decode(clientPass); /*{(byte) 0xAB,(byte) 0xE8, (byte) 0x32, (byte) 0x15, (byte) 0x79, (byte) 0xFB, (byte) 0x58, (byte) 0x94, (byte) 0x73, (byte) 0x13, (byte) 0x90, (byte) 0x61, (byte) 0x0E, (byte) 0x99, (byte) 0x6D, (byte) 0x2B,
(byte) 0x66, (byte) 0x22, (byte) 0x3A, (byte) 0x5E, (byte) 0x80, (byte) 0x39, (byte) 0x1B, (byte) 0x36, (byte) 0xA1, (byte) 0xB3, (byte) 0xE9, (byte) 0x31, (byte) 0x28, (byte) 0xD4, (byte) 0x35, (byte) 0x82
};*/
byte[] hashedPassword = MessageDigest.getInstance("GOST3411").digest(clientPassword);

CryptParamsSpec params = CryptParamsSpec.getInstance();
ru.CryptoPro.JCP.Key.SecretKeySpec spec = new ru.CryptoPro.JCP.Key.SecretKeySpec(hashedPassword, params);
SecretKey skey = new GostSecretKey(spec); 
cipherEncrypt = Cipher.getInstance(CIPHER_ALG);
cipherEncrypt.init(Cipher.ENCRYPT_MODE, skey, ivspec, null);
cipherDecrypt = Cipher.getInstance(CIPHER_ALG);
cipherDecrypt.init(Cipher.DECRYPT_MODE, skey, ivspec, null);
isInintedCipher=true;
}catch (Exception e){
    e.printStackTrace();
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
e.printStackTrace(errorWriter);
log.error(outError.toString());
isInintedCipher=false;

}

}

public class certBean{
private byte[] publickey;
private String certstate;

public certBean(){
certstate = null;
publickey = null;
}

public String getState(){
return certstate;
}

public void setState(String state){
certstate = state;
}

public byte[] getPublickey(){
return publickey;
}

public void setPublickey(byte[] publickey){
this.publickey = publickey;
}
}	public boolean checkCert( String certserial){
try {
Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr ="";


try {certSerivalStr = new BigInteger(certserial, 16).toString();}
catch (Exception e) {
log.error("CERT not BIGINT = "+ certserial);
return false;
}


mapa.put("CERTSERIAL", certSerivalStr);
byte[] certbytes=null;
List l = getNpjt().queryForList("select publickey from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa);
if (l.size()>0) certbytes = (byte[])(((Map)l.get(0)).get("PUBLICKEY"));
else {
//no user registered
log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return false;
}
return ocsp.checkOCSP(certbytes);
} catch (Exception e){
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
e.printStackTrace(errorWriter);
log.error(outError.toString());
return false;
}


}
public synchronized byte[] DecryptMsg(byte[] indata) throws Exception{
   
byte[] decryptedtext = cipherDecrypt.doFinal(indata, 0, indata.length);

return decryptedtext;

}
public synchronized byte[]   EncryptMsg(byte[] indata) throws Exception{
byte[] encrypedtext = cipherEncrypt.doFinal(indata, 0, indata.length);
return encrypedtext;

}
public int checkSignatureXml(byte[] data, byte[] signature, String certserial, String hash) {
try {

//System.out.println("checkSignatureXml");
if (signature.length <= 1)
return 3;
Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr ="";

try {

certSerivalStr = new BigInteger(certserial, 16).toString();

//byte [] dd = bs;
}
catch (Exception e) {

log.error("CERT not BIGINT = "+ certSerivalStr);
return 1;
}
//
////

mapa.put("CERTSERIAL", certSerivalStr);
//	
SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

List<certBean> l = getNpjt().query("SELECT publickey,certstate from snt.personall where certserial = :CERTSERIAL ", mapa, 
new ParameterizedRowMapper<certBean>() {
public certBean mapRow(ResultSet rs, int numrow) throws SQLException {
certBean cb = new certBean();
try{
cb.setPublickey(rs.getBytes("publickey"));
cb.setState(rs.getString("certstate"));

}catch(Exception e){
}
return cb;
}


});

byte[] certbytes=null;


String oldCertState = "Unknown";

if (l.size()>0){

certbytes = l.get(0).getPublickey();

oldCertState = l.get(0).getState();


}
else {

log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return 1;
}

String certStateToBD = null;
if (ocsp.checkOCSP(certbytes)) {
int res = 0;
try {
Signature sig = Signature.getInstance(algorithm, jcp);
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certbytes));
X509Certificate cert509 = (X509Certificate) cert;
try {

cert509.checkValidity();

} catch (Exception e) {

log.warn("check validity failed = "+cert509.getSerialNumber());
return 1;
}
PublicKey pubkey = cert.getPublicKey();	

try{
sig.initVerify(pubkey);

}catch(Exception exx){
System.out.println("PUBERROR");
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
exx.printStackTrace(errorWriter);
log.error(outError.toString());


return 3;
}

sig.update(data);

boolean verifies = sig.verify(signature);
if (verifies) {
res = 0;
} else {
res = 3;
}



} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());

return 1;
}

return res;
} else{

try{
HashMap local =new HashMap();
local.put("cert", certSerivalStr);
getNpjt().update("UPDATE snt.personall SET certstate = 'Сертификат отозван' WHERE certserial=:cert", local);
}catch(Exception e){
log.error("ERRORQLecurityManager:199riginal error message="+e);
}
return 2;
}

} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());

return 3;
}
}
public boolean checkCertBigInt( BigInteger certserial){
try {
Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr =certserial.toString();




mapa.put("CERTSERIAL", certSerivalStr);
byte[] certbytes=null;
List l = getNpjt().queryForList("select publickey from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa);
if (l.size()>0) certbytes = (byte[])(((Map)l.get(0)).get("PUBLICKEY"));
else {
//no user registered
log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return false;
}
return ocsp.checkOCSP(certbytes);
} catch (Exception e){
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
e.printStackTrace(errorWriter);
log.error(outError.toString());
return false;
}


}
public boolean checkSignature(String username, byte[] signature, String certserial) {
try {
	//System.out.println("checkSignature");
if (signature.length == 1)
return false;
Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr ="";

////
//
//when comes "er" from client
try {certSerivalStr = new BigInteger(certserial, 16).toString();}
catch (Exception e) {
log.error("CERT not BIGINT = "+ certserial);
return false;
}
//
////

mapa.put("CERTSERIAL", certSerivalStr);
//	 List l = getNpjt().queryForList("select publickey from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa);

SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

List<certBean> l = getNpjt().query("SELECT publickey, certstate from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa, 
new ParameterizedRowMapper<certBean>() {
public certBean mapRow(ResultSet rs, int numrow) throws SQLException {
certBean cb = new certBean();
try{
cb.setPublickey(rs.getBytes("publickey"));
cb.setState(rs.getString("certstate"));
}catch(Exception e){
}
return cb;
}
});

byte[] certbytes=null;

//if (l.size()>0) certbytes = (byte[])(((Map)l.get(0)).get("PUBLICKEY"));
String oldCertState = "Unknown";
if (l.size()>0){
certbytes = l.get(0).getPublickey();
oldCertState = l.get(0).getState();
}
else {
//no user registered
log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return false;
}

//Проверка сертификата на валидность в базе:
// (null) - Сертификат непроверен
// 01.01.1901 - сертификат отозван
// хх.хх.хххх (валидная дата) - если сертификат проверен и не отозван

String certStateToBD = null;
if (ocsp.checkOCSP(certbytes)) {
boolean res = false;
try {
Signature sig = Signature.getInstance(algorithm, jcp);
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certbytes));
X509Certificate cert509 = (X509Certificate) cert;
try {
cert509.checkValidity();
} catch (Exception e) {
log.warn("ceck validity failed = "+cert509.getSerialNumber());
return false;
}
PublicKey pubkey = cert.getPublicKey();	
try{
sig.initVerify(pubkey);
}catch(Exception exx){
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
exx.printStackTrace(errorWriter);
log.error(outError.toString());

SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
return false;
}	
sig.update(certserial.getBytes("UTF-8"));
boolean verifies = sig.verify(signature);
if (verifies) {
res = true;
} else {
res = false;
}

/*	Date current = new Date();
Date certDate = cert509.getNotAfter();
if(!certDate.before(current)) certStateToBD = "Действителен до "+formatter.format(certDate);
else certStateToBD = "Срок действия истек";*/
} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());
SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
return false;
}
/*	 if(!certStateToBD.equalsIgnoreCase(oldCertState)){
try{
//System.out.println("*********upd");
getNpjt().update("UPDATE snt.personall SET certstate = '"+certStateToBD+"' WHERE certserial='"+certSerivalStr+"'", new HashMap());
}catch(Exception e){
log.error("ERRORQLecurityManager:191riginal error message="+e);
}
}*/
return res;
} else{
//	 System.out.println("security manager return false");//for test!!!!!
try{
HashMap local =new HashMap();
local.put("cert", certSerivalStr);
getNpjt().update("UPDATE snt.personall SET certstate = 'Сертификат отозван' WHERE certserial=:cert", local);
}catch(Exception e){
log.error("ERRORQLecurityManager:199riginal error message="+e);
}
return false;
}

} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());
SNMPSender.sendMessage(ex);
return false;
}
}

public boolean checkAuthSignature(String username, byte[] signature, String certserial) {
try {
	
//	System.out.println("checkAuthSignature");
	
if (signature.length == 1)
return false;
Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr ="";

////
//
//when comes "er" from client
try {certSerivalStr = new BigInteger(certserial, 16).toString();}
catch (Exception e) {
log.error("CERT not BIGINT = "+ certserial);
return false;
}
//
////

mapa.put("CERTSERIAL", certSerivalStr);
//	 List l = getNpjt().queryForList("select publickey from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa);

SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

List<certBean> l = getNpjt().query("SELECT publickey, certstate from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa, 
new ParameterizedRowMapper<certBean>() {
public certBean mapRow(ResultSet rs, int numrow) throws SQLException {
certBean cb = new certBean();
try{
cb.setPublickey(rs.getBytes("publickey"));
cb.setState(rs.getString("certstate"));
}catch(Exception e){
}
return cb;
}
});

byte[] certbytes=null;

//if (l.size()>0) certbytes = (byte[])(((Map)l.get(0)).get("PUBLICKEY"));
String oldCertState = "Unknown";
if (l.size()>0){
certbytes = l.get(0).getPublickey();
oldCertState = l.get(0).getState();
}
else {
//no user registered
log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return false;
}

//Проверка сертификата на валидность в базе:
// (null) - Сертификат непроверен
// 01.01.1901 - сертификат отозван
// хх.хх.хххх (валидная дата) - если сертификат проверен и не отозван

String certStateToBD = null;
if (ocsp.checkOCSP(certbytes)) {
boolean res = false;
try {
Signature sig = Signature.getInstance(algorithm, jcp);
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certbytes));
X509Certificate cert509 = (X509Certificate) cert;
try {
cert509.checkValidity();

} catch (Exception e) {
log.warn("ceck validity failed = "+cert509.getSerialNumber());
return false;
}
PublicKey pubkey = cert.getPublicKey();	
try{
sig.initVerify(pubkey);

System.out.println("init");

}catch(Exception exx){
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
exx.printStackTrace(errorWriter);
log.error(outError.toString());

SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
return false;
}	
sig.update(certserial.getBytes("UTF-8"));
boolean verifies = sig.verify(signature);
//System.out.println(verifies);
if (verifies) {
res = true;
} else {
res = false;
}

Date current = new Date();
Date certDate = cert509.getNotAfter();
if(!certDate.before(current)) certStateToBD = "Действителен до "+formatter.format(certDate);
else certStateToBD = "Срок действия истек";
} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());
SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
return false;
}
//	 if(!certStateToBD.equalsIgnoreCase(oldCertState)){
//	 try{
//	 //System.out.println("*********upd");
//	 getNpjt().update("UPDATE snt.personall SET certstate = '"+certStateToBD+"' WHERE certserial='"+certSerivalStr+"'", new HashMap());
//	 }catch(Exception e){
//	 log.error("ERRORQLecurityManager:191riginal error message="+e);
//	 }
//	 }
return res;
} else{
try{
getNpjt().update("UPDATE snt.personall SET certstate = 'Сертификат отозван' WHERE certserial='"+certSerivalStr+"'", new HashMap());
}catch(Exception e){
log.error("ERRORQLecurityManager:199riginal error message="+e);
}
return false;
}

} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());
SNMPSender.sendMessage(ex);
return false;
}
}

public boolean checkAuthSignatureVVK(String username, byte[] signature, String certserial) {
try {

Map<String, String> mapa = new HashMap<String, String>();
String certSerivalStr ="";

try {certSerivalStr = new BigInteger(certserial, 16).toString();}
catch (Exception e) {
log.error("CERT not BIGINT = "+ certserial);
return false;
}


mapa.put("CERTSERIAL", certSerivalStr);

SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

List<certBean> l = getNpjt().query("SELECT publickey, certstate from snt.personall where CERTSERIAL = :CERTSERIAL ", mapa, 
new ParameterizedRowMapper<certBean>() {
public certBean mapRow(ResultSet rs, int numrow) throws SQLException {
certBean cb = new certBean();
try{
cb.setPublickey(rs.getBytes("publickey"));
cb.setState(rs.getString("certstate"));
}catch(Exception e){
}
return cb;
}
});

byte[] certbytes=null;

String oldCertState = "Unknown";
if (l.size()>0){
certbytes = l.get(0).getPublickey();
oldCertState = l.get(0).getState();
}
else {
//no user registered
log.warn("NOT REGISTERED user cert = "+certSerivalStr);
return false;
}

String certStateToBD = null;
if (ocsp.checkOCSP(certbytes)) {
//	System.out.println("true");
boolean res = false;
try {
Signature sig = Signature.getInstance(algorithm, jcp);
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certbytes));
X509Certificate cert509 = (X509Certificate) cert;
try {
cert509.checkValidity();

} catch (Exception e) {
log.warn("ceck validity failed = "+cert509.getSerialNumber());
return false;
}
PublicKey pubkey = cert.getPublicKey();	
try{
sig.initVerify(pubkey);

}catch(Exception exx){
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
exx.printStackTrace(errorWriter);
log.error(outError.toString());

return false;
}	

res = true;
Date current = new Date();
Date certDate = cert509.getNotAfter();
//System.out.println(certDate);
if(!certDate.before(current)) certStateToBD = "Действителен до "+formatter.format(certDate);
else certStateToBD = "Срок действия истек";
} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());

return false;
}

return res;
} else{
try{
getNpjt().update("UPDATE snt.personall SET certstate = 'Сертификат отозван' WHERE certserial='"+certSerivalStr+"'", new HashMap());
}catch(Exception e){
log.error("ERRORQLecurityManager:199riginal error message="+e);
}
return false;
}

} catch (Exception ex) {
StringWriter outError = new StringWriter();
PrintWriter errorWriter = new PrintWriter(outError);
ex.printStackTrace(errorWriter);
log.error(outError.toString());
SNMPSender.sendMessage(ex);
return false;
}
}





/*
* GETTERS AND SETTERS
*/


private static String keyInfo(PublicKey key) {
final StringBuffer sb = new StringBuffer();
sb.append(key.toString());
sb.append("|| public key algorithm: ");
sb.append(key.getAlgorithm());
sb.append("; enc: ");
//import ru.CryptoPro.JCP.tools.Array; тут можно любую аналогичную функцию
sb.append(new String(Hex.encodeHex(key.getEncoded())));
sb.append("\n");
return sb.toString();
}

private static String provsInfo() {
final StringBuffer sb = new StringBuffer();
final Provider[] provs = Security.getProviders();
for (int i = 0; i < provs.length; i++) {
sb.append(provs[i].getName());
sb.append(" v");
sb.append(provs[i].getVersion());
if (i != provs.length - 1)
sb.append("; ");
}
sb.append("\n");
return sb.toString();
}


public NamedParameterJdbcTemplate getNpjt() {
return npjt;
}

public void setNpjt(NamedParameterJdbcTemplate npjt) {
this.npjt = npjt;
}


public OCSPutils getOcsp() {
return ocsp;
}

public void setOcsp(OCSPutils ocsp) {
this.ocsp = ocsp;
}

public JCP getJcp() {
return jcp;
}

public void setJcp(JCP jcp) {
this.jcp = jcp;
}

public String getAlgorithm() {
return algorithm;
}

public void setAlgorithm(String algorithm) {
this.algorithm = algorithm;
}

}