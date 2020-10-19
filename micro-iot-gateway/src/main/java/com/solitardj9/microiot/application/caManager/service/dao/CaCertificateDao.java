package com.solitardj9.microiot.application.caManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.microiot.application.caManager.service.dao.dto.CaCertificateDto;

public interface CaCertificateDao extends JpaRepository<CaCertificateDto, Integer>{

}