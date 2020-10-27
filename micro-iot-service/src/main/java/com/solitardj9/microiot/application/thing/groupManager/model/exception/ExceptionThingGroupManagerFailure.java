package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupManagerFailure extends Exception{
    //
	private static final long serialVersionUID = -2435169291666914929L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupManagerFailure() {
		//
    	super(ExceptionCodeThingGroupManager.Group_Manager_Failure.getMessage());
    	errCode = ExceptionCodeThingGroupManager.Group_Manager_Failure.getCode();
    	httpStatus = ExceptionCodeThingGroupManager.Group_Manager_Failure.getHttpStatus();
    }
    
	public ExceptionThingGroupManagerFailure(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManager.Group_Manager_Failure.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManager.Group_Manager_Failure.getCode();
		httpStatus = ExceptionCodeThingGroupManager.Group_Manager_Failure.getHttpStatus();
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