package com.solitardj9.microiot.application.thing.groupManager.model.exception;

import org.springframework.http.HttpStatus;


public enum ExceptionCodeThingGroupManager {
    //
	Group_Bad_Request(400, "GroupBadRequest.", HttpStatus.BAD_REQUEST),
	Group_Not_Found(404, "GroupNotFound.", HttpStatus.NOT_FOUND),
	Group_Alreay_Exist(409, "GroupAlreayExist.", HttpStatus.CONFLICT),
	Group_Version_Mismatch(409, "GroupVersionMismatch.", HttpStatus.CONFLICT),
	Group_Manager_Failure(500, "GroupManagerFailure.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
 
    private Integer code;
    private String message;
    private HttpStatus httpStatus;
 
    ExceptionCodeThingGroupManager(Integer code, String msg, HttpStatus httpStatus) {
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