package com.solitardj9.microiot.application.caManager.service.dao.dto;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// TODO : 
@Entity
@Table(name="ca_certificate")
public class CaCertificateDto  implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4622324934835028638L;
}