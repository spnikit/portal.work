package com.aisa.crypto.ocsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;


public class OCSPcheck {

//last occurred exception
private Exception lastException;
private static Logger	log	= Logger.getLogger(OCSPcheck.class);
private ArrayList<OCSPServerState> servers;

public OCSPcheck(String[] serverUrls, String[] timeouts) {
	servers = new ArrayList<OCSPServerState>();
	for (int i = 0; i < serverUrls.length; i++) 
		servers.add(new OCSPServerState(serverUrls[i],Integer.valueOf(timeouts[i])));
	
	}

/**
 * ������� �������� ����� OCSP ������.
 * 
 * @param userCert ���������� ������������
 * @param issuerCert ���������� ��������
 * @param hashAlg �������� �����������
 * @param provider ��������� (����� �� ��������� - null)
 * @param serverUrl ����� ocsp �������
 * @return true/false
 */
public boolean quickCheck(X509Certificate userCert, X509Certificate issuerCert, /*String url,*/
		String hashAlg, String provider) {
//		try {
//			
//			servers = new ArrayList<OCSPServerState>();
//			servers.add(new OCSPServerState(url, 5000));
//			final OCSPoverHTTP sr = new OCSPoverHTTP(url,servers.get(0).getTimeout());
//
//			final OCSPReq req = new OCSPReq(userCert, issuerCert, hashAlg, provider);
//			final OCSPResp resp = new OCSPResp(sr.getResp(req));
//
//			
//			System.out.println("respstat: "+resp.getResponseStatus());
//			if (resp.getResponseStatus() == OCSPResp.SUCCESSFUL) {
//				final OCSPCertStatus st = new OCSPCertStatus(resp.getCertStatus());
//				if (st.getCertStatus() == OCSPCertStatus.GOOD){
//					log.warn("OCSP "+userCert.getSerialNumber()+"  url : "+servers.get(0).getIp() + " success");
//					servers.get(0).setBroken(false);
//					if (servers.get(0).isNeedtosendOK()){
//				servers.get(0).setNeedtosendOK(false);
//						servers.get(0).setNeedtosetTimer(true);
//					}
//					return true;
//				}			
//				else {
//					lastException = new OCSPException("No exception: certificate is "+ st.getCertStatusSTR());
//		}
//			} else{
//				lastException =new OCSPException("No exception: response status is "+ resp.getResponseStatusStr());
//				reportAboutProblem(servers.get(0));
//
//			}
//				return false;
//		} catch (final Exception e) {
//			lastException = e;
//			StringWriter outError = new StringWriter();
//			   PrintWriter errorWriter = new PrintWriter( outError );
//			   e.printStackTrace(errorWriter);
//			   log.error("OCSP   url :  "+servers.get(0).getIp()+"\n"+outError.toString());
//			   if (e.getCause() instanceof IOException) reportAboutProblem(servers.get(0));
//				
//		}

	for (int i = 0; i < servers.size(); i++) {
		try {
		
			final OCSPoverHTTP sr = new OCSPoverHTTP(servers.get(i).getIp(),servers.get(i).getTimeout());
			final OCSPReq req = new OCSPReq(userCert, issuerCert, hashAlg, provider);
			final OCSPResp resp = new OCSPResp(sr.getResp(req));
		if (resp.getResponseStatus() == OCSPResp.SUCCESSFUL) {
				final OCSPCertStatus st = new OCSPCertStatus(resp.getCertStatus());
				if (st.getCertStatus() == OCSPCertStatus.GOOD){
					log.warn("OCSP "+userCert.getSerialNumber()+"  url : "+servers.get(i).getIp() + " success");
					servers.get(i).setBroken(false);
					if (servers.get(i).isNeedtosendOK()){
					servers.get(i).setNeedtosendOK(false);
						servers.get(i).setNeedtosetTimer(true);
					}
					return true;
				}			
				else {
					lastException = new OCSPException("No exception: certificate is "+ st.getCertStatusSTR());
			}
			} else{
				lastException =new OCSPException("No exception: response status is "+ resp.getResponseStatusStr());
				reportAboutProblem(servers.get(i));
				continue;
			}
				return false;
		} catch (final Exception e) {
			lastException = e;
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error("OCSP   url :  "+servers.get(i).getIp()+"\n"+outError.toString());
			   if (e.getCause() instanceof IOException) reportAboutProblem(servers.get(i));
				
		}
	}
	return false;
	
	
}


private void reportAboutProblem(OCSPServerState srvstate){
//	SNMPSender.sendMessage("CA_ERROR", "CA_0001", "0001", "", "OCSP/TSP ip="+srvstate.getIp()+" session timeout");
	srvstate.setBroken(true);
	if (srvstate.isNeedtosetTimer()){
		srvstate.setLastbadquery(System.currentTimeMillis());
		srvstate.setNeedtosetTimer(false);
	}
	if ( (srvstate.getLastbadquery() + 300000) < System.currentTimeMillis()){
//		SNMPSender.sendMessage("CA_ERROR", "CA_0003", "0003", "", "OCSP/TSP ip="+srvstate.getIp()+" server is unavailable");
		srvstate.setNeedtosetTimer(true);
		srvstate.setNeedtosendOK(true);
	}
	
}

public boolean checkWithCertPathValidator(X509Certificate rootCert,
		X509Certificate userCert, String serverUrl) {
	Security.setProperty("ocsp.enable", "true");
	Security.setProperty("ocsp.responderCertSubjectName", rootCert
		.getSubjectX500Principal().getName());
	Security.setProperty("ocsp.responderURL", serverUrl);
	try {
		final Set<TrustAnchor> trust = new HashSet<TrustAnchor>(0);
		trust.add(new TrustAnchor(rootCert, null));

		final List<X509Certificate> cert = new ArrayList<X509Certificate>(0);
		cert.add(userCert);
		cert.add(rootCert);

		//���������
		final PKIXBuilderParameters cpp =
				new PKIXBuilderParameters(trust, null);
		cpp.setSigProvider(null);
		final CollectionCertStoreParameters par =
				new CollectionCertStoreParameters(cert);
		final CertStore store = CertStore.getInstance("Collection", par);
		cpp.addCertStore(store);
		final X509CertSelector selector = new X509CertSelector();
		selector.setCertificate(userCert);
		cpp.setTargetCertConstraints(selector);
		//����������� (CertPath)
		cpp.setRevocationEnabled(false);
		final PKIXCertPathBuilderResult res =
				(PKIXCertPathBuilderResult) CertPathBuilder.getInstance("PKIX")
					.build(cpp);
		final CertPath cp = res.getCertPath();
		//��������
		final CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
		cpp.setRevocationEnabled(true);
		cpv.validate(cp, cpp);
		return true;
	} catch (final Exception e) {
		lastException = e;
		return false;
	}
}

/**
 * @return last occurred exception
 */
public Exception getLastException() {
	return lastException;
}
}
