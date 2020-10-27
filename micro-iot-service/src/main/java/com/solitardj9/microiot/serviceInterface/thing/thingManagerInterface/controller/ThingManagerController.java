package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.controller;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.application.thing.thingManager.model.Thing;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingVersionMismatch;
import com.solitardj9.microiot.application.thing.thingManager.service.ThingManager;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.excpetion.ExceptionThingBadRequest;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request.RequestCreateThing;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request.RequestUpdateThing;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.response.ResponseCreateThing;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.response.ResponseError;
import com.solitardj9.microiot.util.reqExpUtil.RegExpUtil;

@RestController
@RequestMapping(value="/thingManager/")
public class ThingManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ThingManagerController.class);
	
	@Autowired
	ThingManager thingManager;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.thing}")
	private String regExpThing;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.key}")
	private String regExpAttributeKey;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.value}")
	private String regExpAttributeValue;
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param thingName
	 * @param requestBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : "string"
	 *			},
	 *			"merge": boolean
	 *		},
	 *		"thingTypeName": "string"
	 *	}
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/things/{thingName}")
	public ResponseEntity createThing(@PathVariable("thingName") String thingName,
									  @RequestBody(required=false) String requestBody) {		
		//
		logger.info("[ThingManagerController].createThing is called.");
		
		RequestCreateThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestCreateThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingManagerController].createThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestCreateThing(thingName, request);
			} catch (ExceptionThingBadRequest e) {
				logger.error("[ThingManagerController].createThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		Thing thing = null;
		try {
			thing = thingManager.createThing(thingName, request.getThingTypeName(), request.getAttributePayload().getAttributes(), request.getAttributePayload().getMerge());
		} catch (ExceptionThingAlreayExist e) {
			logger.error("[ThingManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(new ResponseCreateThing(thing.getId().toString(), thing.getThingName()), HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/things/{thingName}")
	public ResponseEntity describeThing(@PathVariable("thingName") String thingName) {
		//
		try {
			checkRegExpForRequestDescribeThing(thingName);
		} catch (ExceptionThingBadRequest e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		Thing thing = null;
		try {
			thing = thingManager.getThingByThingName(thingName);
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
	
		return new ResponseEntity(thing , HttpStatus.OK);
	}
	
	/**
	 * @param thingName
	 * @param reqeustBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : "string"
	 *			},
	 *			"merge": boolean
	 *		},
	 *		"expectedVersion": number,
	 *		"removeThingType": boolean,
	 *		"thingTypeName": "string"
	 *	}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/things/{thingName}")
	public ResponseEntity updateThing(@PathVariable("thingName") String thingName, 
									  @RequestBody(required=true) String requestBody) {
		//
		RequestUpdateThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingManagerController].updateThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateThing(thingName, request);
			} catch (ExceptionThingBadRequest e) {
				logger.error("[ThingManagerController].updateThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			thingManager.updateThing(thingName, request.getThingTypeName(), request.getRemoveThingType(), request.getAttributePayload().getAttributes(), request.getAttributePayload().getMerge(), request.getExpectedVersion());
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].updateThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingVersionMismatch e) {
			logger.error("[ThingManagerController].updateThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].updateThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/things/{thingName}")
	public ResponseEntity deleteThing(@PathVariable("thingName") String thingName,
									  @RequestParam(value="expectedVersion", required=false) Integer expectedVersion) {
		//
		try {
			checkRegExpForRequestDescribeThing(thingName);
		} catch (ExceptionThingBadRequest e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			thingManager.deleteThing(thingName, expectedVersion);
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingVersionMismatch e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// TODO : 
		//groupManager.removeThing(thingName);
		
		return new ResponseEntity(null , HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/things")
	public ResponseEntity selectThings(@RequestParam(value = "attributeName", required=false) String attributeName,
									   @RequestParam(value = "attributeValue", required=false) String attributeValue,
									   @RequestParam(value = "thingTypeName", required=false) String thingTypeName) {
		//
		try {
			List<Thing> things = thingManager.getThings(attributeName, attributeValue, thingTypeName);
			return new ResponseEntity(things , HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[ThingManagerController].selectThings : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	
	
	private Boolean checkRegExpForRequestCreateThing(String thingName, RequestCreateThing request) throws ExceptionThingBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingTypeName(), regExpThing, true))
			throw new ExceptionThingBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionThingBadRequest();
		
		for (Entry<String, String> entry : request.getAttributePayload().getAttributes().entrySet()) {
			//
			String key = entry.getKey();
			String value = entry.getValue();	
			
			if (!RegExpUtil.isValidExpression(key, regExpAttributeKey, false)) {
				throw new ExceptionThingBadRequest();
			}

			if (!RegExpUtil.isValidExpression(value, regExpAttributeValue, true)) {
				throw new ExceptionThingBadRequest();
			}
		}
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateThing(String thingName, RequestUpdateThing request) throws ExceptionThingBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingTypeName(), regExpThing, true))
			throw new ExceptionThingBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionThingBadRequest();
		
		for (Entry<String, String> entry : request.getAttributePayload().getAttributes().entrySet()) {
			//
			String key = entry.getKey();
			String value = entry.getValue();	
			
			if (!RegExpUtil.isValidExpression(key, regExpAttributeKey, false)) {
				throw new ExceptionThingBadRequest();
			}

			if (!RegExpUtil.isValidExpression(value, regExpAttributeValue, true)) {
				throw new ExceptionThingBadRequest();
			}
		}
		
		if (request.getExpectedVersion() == null)
			throw new ExceptionThingBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestDescribeThing(String thingName) throws ExceptionThingBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingBadRequest();
		
		return true;
	}
}