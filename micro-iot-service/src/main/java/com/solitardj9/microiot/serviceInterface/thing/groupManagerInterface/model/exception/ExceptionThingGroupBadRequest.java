package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingGroupBadRequest extends Exception{
    //
	private static final long serialVersionUID = -8641117992385689736L;
	
	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingGroupBadRequest() {
		//
    	super(ExceptionCodeThingGroupManagerController.Bad_Request.getMessage());
    	errCode = ExceptionCodeThingGroupManagerController.Bad_Request.getCode();
    	httpStatus = ExceptionCodeThingGroupManagerController.Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingGroupBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThingGroupManagerController.Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThingGroupManagerController.Bad_Request.getCode();
		httpStatus = ExceptionCodeThingGroupManagerController.Bad_Request.getHttpStatus();
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