package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingNotFound extends Exception{
    //
	private static final long serialVersionUID = -1523842109364377340L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingNotFound() {
		//
    	super(ExceptionCodeThingManager.Thing_Not_Found.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Not_Found.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Not_Found.getHttpStatus();
    }
    
	public ExceptionThingNotFound(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Not_Found.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Not_Found.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Not_Found.getHttpStatus();
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