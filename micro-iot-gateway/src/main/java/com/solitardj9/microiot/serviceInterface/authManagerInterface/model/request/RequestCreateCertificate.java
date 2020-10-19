package com.solitardj9.microiot.serviceInterface.authManagerInterface.model.request;

public class RequestCreateCertificate extends RequestDefault {
	//
	private String clientId;
	
	private String certificateSigningRequest;
	
	public RequestCreateCertificate() {
		
	}
	
	public RequestCreateCertificate(String clientId, String certificateSigningRequest) {
		this.clientId = clientId;
		this.certificateSigningRequest = certificateSigningRequest;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCertificateSigningRequest() {
		return certificateSigningRequest;
	}

	public void setCertificateSigningRequest(String certificateSigningRequest) {
		this.certificateSigningRequest = certificateSigningRequest;
	}

	@Override
	public String toString() {
		return "RequestCreateCertificate [clientId=" + clientId + ", certificateSigningRequest="
				+ certificateSigningRequest + "]";
	}
}