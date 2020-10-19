package com.solitardj9.microiot.application.caManager.service.data;

public enum CaCertificateMapParamEnum {
	//
	CA_CERTIFICATE_MAP("CaCertificateMap"),
	CA_CERTIFICATE_MAP_LOCK("CaCertificateMapLock"),
	CA_CERTIFICATE_MAP_KEY("CaCertificate")
	;
	
	private String param;
	
	private CaCertificateMapParamEnum(String param) {
		this.param = param;
	}
	
	public String getParam() {
		return param;
	}
	
	@Override
	public String toString() {
		return param;
	}
}