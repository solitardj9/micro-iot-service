package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupVersionMismatch extends Exception{
    //
	private static final long serialVersionUID = -6871586009232322972L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupVersionMismatch() {
		//
    	super(ExceptionCodeThingGroupManager.Group_Version_Mismatch.getMessage());
    	errCode = ExceptionCodeThingGroupManager.Group_Version_Mismatch.getCode();
    	httpStatus = ExceptionCodeThingGroupManager.Group_Version_Mismatch.getHttpStatus();
    }
    
	public ExceptionThingGroupVersionMismatch(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManager.Group_Version_Mismatch.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManager.Group_Version_Mismatch.getCode();
		httpStatus = ExceptionCodeThingGroupManager.Group_Version_Mismatch.getHttpStatus();
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