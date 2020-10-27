package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.excpetion;

import org.springframework.http.HttpStatus;

public class ExceptionThingBadRequest extends Exception{
    //
	private static final long serialVersionUID = -8641117992385689736L;
	
	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingBadRequest() {
		//
    	super(ExceptionCodeThingManagerController.Bad_Request.getMessage());
    	errCode = ExceptionCodeThingManagerController.Bad_Request.getCode();
    	httpStatus = ExceptionCodeThingManagerController.Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThingManagerController.Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThingManagerController.Bad_Request.getCode();
		httpStatus = ExceptionCodeThingManagerController.Bad_Request.getHttpStatus();
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