package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingBadRequest extends Exception{
	//
	private static final long serialVersionUID = 6947962638961370061L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingBadRequest() {
		//
    	super(ExceptionCodeThingManager.Thing_Bad_Request.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Bad_Request.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Bad_Request.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Bad_Request.getHttpStatus();
	}
	
	public int getErrCode() {
		//
		return errCode;
    }
	
	public HttpStatus getHttpStatus() {
		//
		return httpStatus;
    }
}