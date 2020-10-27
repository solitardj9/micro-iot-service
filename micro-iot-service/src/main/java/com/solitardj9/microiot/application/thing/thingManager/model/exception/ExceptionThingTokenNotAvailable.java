package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingTokenNotAvailable extends Exception{
    //
	private static final long serialVersionUID = 1330924554379479395L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingTokenNotAvailable() {
		//
    	super(ExceptionCodeThingManager.Thing_Token_Not_Available.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Token_Not_Available.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Token_Not_Available.getHttpStatus();
    }
    
	public ExceptionThingTokenNotAvailable(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Token_Not_Available.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Token_Not_Available.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Token_Not_Available.getHttpStatus();
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