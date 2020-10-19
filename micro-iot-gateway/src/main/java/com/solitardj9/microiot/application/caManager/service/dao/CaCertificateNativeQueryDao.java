package com.solitardj9.microiot.application.caManager.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("caCertificateNativeQueryDao")
public class CaCertificateNativeQueryDao {
	//
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createCaCertificateTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ca_certificate("
				   + "id						int NOT NULL AUTO_INCREMENT, "
				   + "certificate_pem		MEDIUMTEXT DEFAULT NULL, "
				   + "key_pem					MEDIUMTEXT DEFAULT NULL, "
				   + "creation_date			varchar(128) DEFAULT NULL, "
				   + "expired_date			varchar(128) DEFAULT NULL, "
				   + "issuer_name				varchar(128) DEFAULT NULL, "
				   + "subject_name			varchar(128) DEFAULT NULL, "
				   + "modify_date				varchar(128) DEFAULT NULL, "
				   + "PRIMARY KEY PKEY (id)); ";	
		Integer ret = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return ret;
	}
}