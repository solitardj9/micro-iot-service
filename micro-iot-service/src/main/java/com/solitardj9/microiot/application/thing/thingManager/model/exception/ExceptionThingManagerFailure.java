package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingManagerFailure extends Exception{
    //
	private static final long serialVersionUID = 3278977838617994499L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingManagerFailure() {
		//
    	super(ExceptionCodeThingManager.Thing_Manager_Failure.getMessage());
    	errCode = ExceptionCodeThingManager.Thing_Manager_Failure.getCode();
    	httpStatus = ExceptionCodeThingManager.Thing_Manager_Failure.getHttpStatus();
    }
    
	public ExceptionThingManagerFailure(Throwable cause) {
		//
		super(ExceptionCodeThingManager.Thing_Manager_Failure.getMessage(), cause);
		errCode = ExceptionCodeThingManager.Thing_Manager_Failure.getCode();
		httpStatus = ExceptionCodeThingManager.Thing_Manager_Failure.getHttpStatus();
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