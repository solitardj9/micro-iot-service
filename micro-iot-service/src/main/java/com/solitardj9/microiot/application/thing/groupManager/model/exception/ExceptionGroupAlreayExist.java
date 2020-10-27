package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupAlreayExist extends Exception{
    //
	private static final long serialVersionUID = 1330924554379479395L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupAlreayExist() {
		//
    	super(ExceptionCodeGroupManager.Thing_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeGroupManager.Thing_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeGroupManager.Thing_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionGroupAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeGroupManager.Thing_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeGroupManager.Thing_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeGroupManager.Thing_Alreay_Exist.getHttpStatus();
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