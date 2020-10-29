package com.solitardj9.microiot.application.caManager.service;

import com.solitardj9.microiot.application.caManager.model.CaCertificate;

public interface CaCertificateManager {
    
	public Boolean isInitialized();
	
	public CaCertificate getCa();
}