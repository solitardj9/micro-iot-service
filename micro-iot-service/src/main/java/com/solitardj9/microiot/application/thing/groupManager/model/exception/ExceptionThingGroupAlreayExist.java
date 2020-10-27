package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupAlreayExist extends Exception{
    //
	private static final long serialVersionUID = -2385357148462609614L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupAlreayExist() {
		//
    	super(ExceptionCodeThingGroupManager.Group_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeThingGroupManager.Group_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeThingGroupManager.Group_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionThingGroupAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManager.Group_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManager.Group_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeThingGroupManager.Group_Alreay_Exist.getHttpStatus();
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