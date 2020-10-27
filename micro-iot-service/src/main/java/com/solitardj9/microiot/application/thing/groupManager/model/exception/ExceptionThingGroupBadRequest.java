package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupBadRequest extends Exception{
	//
	private static final long serialVersionUID = 2224487629262853095L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupBadRequest() {
		//
    	super(ExceptionCodeThingGroupManager.Group_Bad_Request.getMessage());
    	errCode = ExceptionCodeThingGroupManager.Group_Bad_Request.getCode();
    	httpStatus = ExceptionCodeThingGroupManager.Group_Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingGroupBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManager.Group_Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManager.Group_Bad_Request.getCode();
		httpStatus = ExceptionCodeThingGroupManager.Group_Bad_Request.getHttpStatus();
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