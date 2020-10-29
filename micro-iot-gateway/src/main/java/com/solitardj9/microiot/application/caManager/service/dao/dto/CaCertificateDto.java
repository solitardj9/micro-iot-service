package com.solitardj9.microiot.application.caManager.service.dao.dto;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ca_certificate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaCertificateDto  implements Serializable {	

	private static final long serialVersionUID = -7999547422649445483L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Integer id;
	
	@Column(name="certificate_pem")
	private String certificatePem;
	
	@Column(name="private_key_pem")
	private String privateKeyPem;
	
	@Column(name="creation_date")
	private Date creationDate;
	
	@Column(name="expired_date")
	private Date expiredDate;
	
	@Column(name="modified_date")
	private Date modifiedDate;
	
	@Column(name="issuer_name")
	private String issuerName; 
	
	@Column(name="subject_name")
	private String subjectName; 
}