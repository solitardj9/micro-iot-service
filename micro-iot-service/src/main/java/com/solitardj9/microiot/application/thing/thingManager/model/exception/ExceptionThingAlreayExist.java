package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingAlreayExist extends Exception{
    //
	private static final long serialVersionUID = 1330924554379479395L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingAlreayExist() {
		//
    	super(ExceptionCodeThingManager.Thing_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionThingAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Alreay_Exist.getHttpStatus();
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