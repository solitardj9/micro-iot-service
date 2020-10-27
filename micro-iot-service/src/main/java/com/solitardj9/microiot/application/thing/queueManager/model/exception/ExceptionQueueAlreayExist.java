package com.solitardj9.microiot.application.thing.queueManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionQueueAlreayExist extends Exception{
    //
	private static final long serialVersionUID = 1330924554379479395L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionQueueAlreayExist() {
		//
    	super(ExceptionCodeQueueManager.Thing_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeQueueManager.Thing_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeQueueManager.Thing_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionQueueAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeQueueManager.Thing_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeQueueManager.Thing_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeQueueManager.Thing_Alreay_Exist.getHttpStatus();
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