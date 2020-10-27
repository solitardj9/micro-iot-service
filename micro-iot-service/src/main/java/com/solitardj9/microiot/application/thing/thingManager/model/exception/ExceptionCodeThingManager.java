package com.solitardj9.microiot.application.thing.thingManager.model.exception;

import org.springframework.http.HttpStatus;


public enum ExceptionCodeThingManager {
    //
	Thing_Bad_Request(400, "ThingBadRequest.", HttpStatus.BAD_REQUEST),
	Thing_Not_Found(404, "ThingNotFound.", HttpStatus.NOT_FOUND),
	Thing_Token_Not_Available(405, "ThingTokenNotAvailable.", HttpStatus.METHOD_NOT_ALLOWED),
	Thing_Alreay_Exist(409, "ThingAlreayExist.", HttpStatus.CONFLICT),
	Thing_Version_Mismatch(409, "ThingVersionMismatch.", HttpStatus.CONFLICT),
	Thing_Manager_Failure(500, "ThingManagerFailure.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
 
    private Integer code;
    private String message;
    private HttpStatus httpStatus;
 
    ExceptionCodeThingManager(Integer code, String msg, HttpStatus httpStatus) {
        this.code = code;
        this.message = msg;
        this.httpStatus = httpStatus;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public String getMessage() {
        return this.message;
    }

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}