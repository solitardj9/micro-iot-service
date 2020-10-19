package com.solitardj9.microiot.application.caManager.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solitardj9.microiot.application.caManager.model.CaCertificate;
import com.solitardj9.microiot.application.caManager.service.CaCertificateManager;
import com.solitardj9.microiot.application.caManager.service.dao.CaCertificateNativeQueryDao;

@Service("caCertificateManager")
public class CaCertificateManagerImpl implements CaCertificateManager {

	@Autowired
	CaCertificateNativeQueryDao caCertificateNativeQueryDao;
	
	@PostConstruct
	public void init() {
		//
		createTable();
	}
	
	private void createTable() {
		//
		caCertificateNativeQueryDao.createCaCertificateTable();
	}
	
	@Override
	public CaCertificate getCa() {
		// TODO Auto-generated method stub
		return null;
	}

}
