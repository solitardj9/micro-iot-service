package com.solitardj9.microiot.serviceInterface.authManagerInterface.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.serviceInterface.authManagerInterface.model.request.RequestCreateCertificate;
import com.solitardj9.microiot.serviceInterface.common.model.ResponseDefualt;

@RestController
@RequestMapping(value="/gateway")
public class AuthManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthManagerController.class);

	//@Autowired
	//AuthManager authManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param requestBody
	 * 		{
	 * 			"clientId" : "{clientId}",
	 * 			"certificateSigningRequest" : "{csr}"
	 * 		}
	 * @return
	 * 		{
	 * 			"certificateId" : "{certificateId}",
	 * 			"certificatePem" : "{certificatePem}"
	 * 		}
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/certificates")
	public ResponseEntity createCertificate(@RequestBody(required=true) String requestBody) {
		//
		logger.info("[AuthManagerController].createCertificate is called.");
		
		try {
			RequestCreateCertificate request = om.readValue(requestBody, RequestCreateCertificate.class);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (JsonProcessingException e) {
			logger.error("[ServiceManagerController].createCertificate : error = " + e);
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.BAD_REQUEST.value(), e.toString()), HttpStatus.BAD_REQUEST);
		}
    }
	
	 
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@PostMapping("/certificates")
//	public ResponseEntity createCertificateFromCsr (@RequestParam(value = "setAsActive",   required=false) Boolean setAsActive, 
//			@RequestBody Map<String, String> csrMap) {
//		ResponseCert deviceCertObj = null;		
//		String csr = csrMap.get("certificateSigningRequest");
//		if (csr == null || csr.equals("")) {
//			return new ResponseEntity(StatusCode.Invalid_Request , HttpStatus.BAD_REQUEST);
//		}
//		DeviceCertificate cert = null;
//
//		try {
//			cert = certManager.createDeviceCertificate(csr);
//		} catch (InvalidRequestException ire) {
//			return new ResponseEntity(StatusCode.Invalid_Request , HttpStatus.BAD_REQUEST);
//		} catch (Exception e) {
//			return new ResponseEntity(StatusCode.Invalid_Request.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		if (cert == null)
//			return new ResponseEntity(StatusCode.Internal_Failure.getMapMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
//		// 매핑
//		deviceCertObj = new ResponseCert(cert.getArn(), cert.getId().toString(), cert.getPem());
//
//		return new ResponseEntity(deviceCertObj , HttpStatus.OK);
//	}
	
	
	
	
	
	
}