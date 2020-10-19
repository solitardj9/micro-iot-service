package com.solitardj9.microiot.serviceInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/state")
public class TestStateController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestStateController.class);

	@GetMapping(value="/getState")
	public ResponseEntity getState(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[TestStateController].getState is called.");
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
}