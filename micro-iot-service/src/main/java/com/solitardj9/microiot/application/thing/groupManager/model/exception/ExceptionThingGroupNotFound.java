package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupNotFound extends Exception{
	//
	private static final long serialVersionUID = 8043833850508225665L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupNotFound() {
		//
    	super(ExceptionCodeThingGroupManager.Group_Not_Found.getMessage());
    	errCode = ExceptionCodeThingGroupManager.Group_Not_Found.getCode();
    	httpStatus = ExceptionCodeThingGroupManager.Group_Not_Found.getHttpStatus();
    }
    
	public ExceptionThingGroupNotFound(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManager.Group_Not_Found.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManager.Group_Not_Found.getCode();
		httpStatus = ExceptionCodeThingGroupManager.Group_Not_Found.getHttpStatus();
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