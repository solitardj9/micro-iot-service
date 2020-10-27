package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingVersionMismatch extends Exception{
    //
	private static final long serialVersionUID = 3009630110312154574L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingVersionMismatch() {
		//
    	super(ExceptionCodeThingManager.Thing_Version_Mismatch.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Version_Mismatch.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Version_Mismatch.getHttpStatus();
    }
    
	public ExceptionThingVersionMismatch(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Version_Mismatch.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Version_Mismatch.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Version_Mismatch.getHttpStatus();
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