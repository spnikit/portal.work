package com.aisa.portal.invoice.integration.security;

import java.security.cert.Certificate;
import java.security.PrivateKey;

public class KeyObj {
	public PrivateKey getKey() {
		return key;
	}
	public void setKey(PrivateKey key) {
		this.key = key;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	PrivateKey key;
	Certificate certificate;
}
