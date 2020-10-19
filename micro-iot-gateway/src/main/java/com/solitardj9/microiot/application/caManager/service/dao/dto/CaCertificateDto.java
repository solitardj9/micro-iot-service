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

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Integer id;						///< CA 인증서 Id
	
	@Column
	private String name;						///< CA 인증서 이름
	
	@Column(name="arn")
	private String arn;						///< CA 인증서 Arn
	
	@Column(name="cert_pem")
	private String certPem;				///<  PEM 형식 CA 인증서 데이터
	
	@Column(name="cert_status")
	private String status;					///< CA 인증서 유효상태, ACTIVE|INACTIVE

	@Column(name="type")
	private String type;						///< 자체 인증서: INTERNAL, 외부에서 등록한 인증서: EXTERNAL
	
	@Column(name="key_pem")
	private String keyPem;					///< 추가, CA PrivateKey Pem
	
	
	@Column(name="creation_date")
	private Date creationDate;			///< CA 인증서 생성날짜 (2015, 1,1)	분초 없으면 Date 도 될듯
	
	@Column(name="expired_date")
	private Date expiredDate;			///< 추가, CA 인증서 만료 날짜
	
	@Column(name="issuer_name")
	private String issuerName;			///< 추가, 
	
	@Column(name="subject_name")
	private String subjectName;		///< 추가, 
	
	@Column(name="owned_by")
	private String ownedBy;				///< CA 인증서의 소유자
		
	@Column(name="auto_regi_status")
	private String autoRegiStatus;		///< CA 인증서의 디바이스 인증서 자동등록 구성여부를 지정,  ENABLE|DISABLE
	
	@Column(name="modify_date")
	private Date modifyDate;				///< CA 인증서최종 마지막 수정날짜 (2015, 1, 1)
	
	@Column(name="generation_id")
	private Integer generationId;			///< CA 인증서의 생성 Id

	
//	@Transient 
	private PrivateKey privateKey;		///< 추가, CA privateKey Object
	
//	@Transient 
	private X509Certificate cert;			///< CA Certficate
	
	public CaCertificateDto() {
	}
	
	public CaCertificateDto(String arn, String certiPem, String type) {
		this.arn = arn;
		this.certPem = certiPem;
		this.type = type;
	}

	public CaCertificateDto(String status, String type, String certPem, Date createdDate, Date expiredDate2,
			String issuerName, String subjectName, String keyPem, PrivateKey privateKey, X509Certificate cert) {
		this.status = status;
		this.type = type;
		this.certPem = certPem;
		if (createdDate == null)
			this.creationDate = null;
		else
			this.creationDate = (Date) createdDate.clone();
		if (expiredDate2 == null)
			this.expiredDate = null;
		else
			this.expiredDate = (Date) expiredDate2.clone();
		this.issuerName = issuerName;
		this.subjectName = subjectName;
		this.keyPem = keyPem;
		this.privateKey = privateKey;
		this.cert = cert;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCertPem() {
		return certPem;
	}

	public void setCertPem(String certPem) {
		this.certPem = certPem;
	}

	public Date getCreationDate() {
		if (this.creationDate == null)
			return  null;
		else
			return  (Date) this.creationDate.clone();
	}

	public void setCreationDate(Date creationDate) {
		if (creationDate == null)
			this.creationDate = null;
		else
			this.creationDate = (Date) creationDate.clone();

	}

	public Date getExpiredDate() {
		if (this.expiredDate == null)
			return null;
		else
			return (Date) this.expiredDate.clone();
	}

	public void setExpiredDate(Date expiredDate) {
		if (expiredDate == null)
			this.expiredDate = null;
		else
			this.expiredDate = (Date) expiredDate.clone();
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	public String getAutoRegiStatus() {
		return autoRegiStatus;
	}

	public void setAutoRegiStatus(String autoRegiStatus) {
		this.autoRegiStatus = autoRegiStatus;
	}

	public Date getModifyDate() {
		if (this.modifyDate == null)
			return null;
		else
			return (Date) this.modifyDate.clone();
	}

	public void setModifyDate(Date modifyDate) {	
		if (modifyDate == null)
			this.modifyDate = null;
		else
			this.modifyDate = (Date) modifyDate.clone();
	}

	public Integer getGenerationId() {
		return generationId;
	}

	public void setGenerationId(Integer generationId) {
		this.generationId = generationId;
	}

	public String getKeyPem() {
		return keyPem;
	}

	public void setKeyPem(String keyPem) {
		this.keyPem = keyPem;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	@Override
	public String toString() {
		//
		String ret = "";
		
		ret += "id : " + id.toString() + "\r\n";
		ret += "name : " + name + "\r\n";
		ret += "arn : " + arn + "\r\n";
		ret += "status : " + status + "\r\n";
		
		ret += "certPem : " + certPem + "\r\n";
		ret += "ownedBy : " + ownedBy + "\r\n";
		ret += "creationDate : " + creationDate + "\r\n";
		ret += "autoRegiStatus : " + autoRegiStatus + "\r\n";
		
		if (modifyDate != null)
			ret += "modifyDate : " + modifyDate.toString() + "\r\n";
		else
			ret += "modifyDate is null\r\n";
		
		if (generationId != null)
			ret += "generationId : " + generationId.toString() + "\r\n";
		else
			ret += "generationId is null\r\n";
		
		
		return ret;
	}
}