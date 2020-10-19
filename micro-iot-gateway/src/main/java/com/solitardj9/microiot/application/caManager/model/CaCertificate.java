package com.solitardj9.microiot.application.caManager.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CaCertificate {	
    //
	private PrivateKey privateKey;
	
	private X509Certificate certificate;
	
	public CaCertificate() {
		
	}
	
	public CaCertificate(PrivateKey privateKey, X509Certificate certificate) {
	    //
		this.privateKey = privateKey;
		this.certificate = certificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

    @Override
    public String toString() {
        return "Ca [privateKey=" + privateKey + ", certificate=" + certificate + "]";
    }
}