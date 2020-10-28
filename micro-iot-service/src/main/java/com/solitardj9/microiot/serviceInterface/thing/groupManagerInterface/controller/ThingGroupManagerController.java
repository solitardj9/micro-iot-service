package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.application.thing.groupManager.model.ThingGroup;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupAlreayExist;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupNotFound;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupVersionMismatch;
import com.solitardj9.microiot.application.thing.groupManager.service.ThingGroupManager;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.AttributePayload;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.ThingGroupMetadata;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.ThingGroupProperties;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.exception.ExceptionThingGroupBadRequest;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request.RequestAddThingToThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request.RequestCreateThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request.RequestRemoveThingFromThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request.RequestUpdateThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request.RequestUpdateThingGroupForThing;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response.ResponseDescribeThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response.ResponseListThingGroups;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response.ResponseUpdateThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response.RespoonseListThingGroupsForThing;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response.RespoonseListThingsInThingGroup;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.excpetion.ExceptionThingBadRequest;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.response.ResponseCreateThing;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.response.ResponseError;
import com.solitardj9.microiot.util.reqExpUtil.RegExpUtil;

@RestController
@RequestMapping(value="/thingManager/")
public class ThingGroupManagerController {

	private static final Logger logger = LoggerFactory.getLogger(ThingGroupManagerController.class);
	
	@Autowired
	ThingGroupManager thingGroupManager;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.thing}")
	private String regExpGroup;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.key}")
	private String regExpAttributeKey;
	
	@Value("${serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.value}")
	private String regExpAttributeValue;
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param thingGroupName
	 * @param requestBody
	 *		{
	 *			"parentGroupName": "string",
	 *			"thingGroupProperties": {
	 *				"attributePayload": {
	 *					"attributes": {
	 *						"string" : "string"
	 *					},
	 *					"merge": boolean
	 *				},
	 *				"thingGroupDescription": "string"
	 *			}
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/thing-groups/{thingGroupName}")
	public ResponseEntity createThingGroup(@PathVariable("thingGroupName") String thingGroupName,
									  	   @RequestBody(required=true) String requestBody) {		
		//
		RequestCreateThingGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestCreateThingGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManagerController].createThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestCreateThingGroup(thingGroupName, request);
			} catch (ExceptionThingGroupBadRequest e) {
				logger.error("[ThingGroupManagerController].createThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		ThingGroup thingGroup = null;
		try {
			thingGroup = thingGroupManager.createThingGroup(thingGroupName, request.getParentGroupName(), request.getThingGroupProperties().getAttributePayload().getAttributes(), request.getThingGroupProperties().getAttributePayload().getMerge(), request.getThingGroupProperties().getThingGroupDescription());
		} catch (ExceptionThingGroupAlreayExist e) {
			logger.error("[ThingGroupManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(new ResponseCreateThing(thingGroup.getId().toString(), thingGroup.getThingGroupName()), HttpStatus.OK);
	}
	
	/**
	 * @param thingGroupName
	 * @return
	 *		{
	 *			"thingGroupId" : "string",
	 *			"thingGroupName" : "string",
	 *			"version" : number,
	 *			"thingGroupProperties" : {
	 *				"attributePayload" : {
	 *					"attributes" : {
	 *						"string" : "string"
	 *					},
	 *					"merge" : null
	 *				},
	 *				"thingGroupDescription" : "string"
	 *			},
	 *			"thingGroupMetadata" : {
	 *				"parentGroupName" : "string",
	 *				"rootToParentThingGroups" : [ "{parentGroupName}" ]
	 *			}
	 *		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/thing-groups/{thingGroupName}")
	public ResponseEntity describeThingGroup(@PathVariable("thingGroupName") String thingGroupName) {
		//
		try {
			checkRegExpForRequestThingGroup(thingGroupName);
		} catch (ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		ResponseDescribeThingGroup response = new ResponseDescribeThingGroup();
		ThingGroup thingGroup = null;
		ThingGroup parentThingGroup = null;
		List<String> rootToParentGroupNames = new ArrayList<>();
		try {
			thingGroup = thingGroupManager.getThingGroupByThingGroupName(thingGroupName);
			parentThingGroup = thingGroupManager.getThingGroupByThingGroupName(thingGroup.getParentGroupName());
			rootToParentGroupNames = thingGroupManager.getNamesOfRootToGroup(parentThingGroup.getParentGroupName());
			
			response.setThingGroupId(thingGroup.getId().toString());
			response.setThingGroupName(thingGroup.getThingGroupName());
			response.setVersion(thingGroup.getVersion());
			
			response.setThingGroupProperties(new ThingGroupProperties(new AttributePayload(thingGroup.getAttributes(), null), thingGroup.getThingGroupDescription()));
			
			response.setThingGroupMetadata(new ThingGroupMetadata(parentThingGroup.getParentGroupName(), rootToParentGroupNames));
		} catch (ExceptionThingGroupNotFound e) {
			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
	
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
	/**
	 * @param thingGroupName
	 * @param requestBody
	 *		{
	 *			"thingGroupProperties" : {
	 *				"attributePayload" : {
	 *					"attributes" : {
	 *						"string" : "string"
	 *					},
	 *					"merge": boolean
	 *				},
	 *				"thingGroupDescription": "string"
	 *			},
	 *			"expectedVersion" : number
	 *		}
	 * @return
	 *		{
	 *			"version" : number
	 *		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/thing-groups/{thingGroupName}")
	public ResponseEntity updateThingGroup(@PathVariable("thingName") String thingGroupName,
										   @RequestBody(required=true) String requestBody) {
		//
		RequestUpdateThingGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateThingGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateThingGroup(thingGroupName, request);
			} catch (ExceptionThingGroupBadRequest e) {
				logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}

		ResponseUpdateThingGroup response = new ResponseUpdateThingGroup();
		try {
			Integer version = thingGroupManager.updateThingGroup(thingGroupName, request.getThingGroupProperties().getAttributePayload().getAttributes(), request.getThingGroupProperties().getAttributePayload().getMerge(), request.getExpectedVersion());
			response.setVersion(version);
		} catch (ExceptionThingGroupNotFound e) {
			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingGroupVersionMismatch e) {
			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/thing-groups/{thingGroupName}")
	public ResponseEntity deleteThingGroup(@PathVariable("thingGroupName") String thingGroupName,
										   @RequestParam(value="expectedVersion", required=false) Integer expectedVersion) {
		//
		try {
			checkRegExpForRequestThingGroup(thingGroupName);
		} catch (ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			thingGroupManager.deleteThingGroup(thingGroupName, expectedVersion);
		} catch (ExceptionThingGroupNotFound e) {
			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingGroupVersionMismatch e) {
			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}	

	/**
	 * @param parentGroup
	 * @param recursive
	 * @return
	 *		{
	 *			"thingGroups": [ "{thingGroupName}" ]
	 *		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/thing-groups")
	public ResponseEntity listThingGroups(
			   @RequestParam(value = "parentGroup", required=false) String parentGroupName,
			   @RequestParam(value = "recursive", required=false) Boolean recursive) {
		//
		try {
			checkRegExpForRequestThingGroup(parentGroupName);
		} catch (ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].listThingGroups : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}

		ResponseListThingGroups response = new ResponseListThingGroups();
		try {
			List<String> thingGroupNames = thingGroupManager.getNamesByParentGroup(parentGroupName, recursive);
			response.setThingGroups(thingGroupNames);
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].listThingGroups : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * @param requestBody
	 *		{
	 *			"thingGroupName": "string",
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/thing-groups/thing")
	public ResponseEntity addThingToThingGroup(@RequestBody(required=true) String requestBody) {
		//
		RequestAddThingToThingGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestAddThingToThingGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestAddThingToThingGroup(request);
			} catch (ExceptionThingGroupBadRequest e) {
				logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			thingGroupManager.addThingToThingGroup(request.getThingGroupName(), request.getThingName());
		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingGroupNotFound e) {
			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(HttpStatus.OK);
	}
	
	/**
	 * @param requestBody
	 *		{
	 *			"thingGroupsToAdd": [ "string" ],
	 *			"thingGroupsToRemove": [ "string" ],
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/thing-groups/thing")
	public ResponseEntity updateThingGroupsForThing(@RequestBody(required=true) String requestBody) {
		//
		RequestUpdateThingGroupForThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateThingGroupForThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateThingGroupForThing(request);
			} catch (ExceptionThingGroupBadRequest e) {
				logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			thingGroupManager.updateThingGroupsForThing(request.getThingGroupsToAdd(), request.getThingGroupsToRemove(), request.getThingName());
		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * @param body
	 *		{
	 *			"thingGroupName": "string",
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/thing-groups/thing")
	public ResponseEntity removeThingFromThingGroup(@RequestBody(required=false) String requestBody) {
		//
		RequestRemoveThingFromThingGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestRemoveThingFromThingGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestRemoveThingFromThingGroup(request);
			} catch (ExceptionThingGroupBadRequest e) {
				logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			thingGroupManager.removeThingFromThingGroup(request.getThingGroupName(), request.getThingName());
		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return new ResponseEntity(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param thingGroupName
	 * @param recursive
	 * @return
	 * {
    		"things": [ "string" ]
		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/thing-groups/{thingGroupName}/things")
	public ResponseEntity listThingsInThingGroup(@PathVariable("thingGroupName") String thingGroupName,
												 @RequestParam(value="recursive", required=true) Boolean recursive) {
		//
		try {
			checkRegExpForRequestThingGroup(thingGroupName);
		} catch (ExceptionThingGroupBadRequest e) {
			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
				
		RespoonseListThingsInThingGroup response = new RespoonseListThingsInThingGroup();
		try {
			List<String> things = thingGroupManager.getNamesOfThingsInThingGroup(thingGroupName, recursive);
			response.setThings(things);
		} catch(ExceptionThingGroupNotFound e) {
			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch(Exception e) {
			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);			
		} 
				
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * @param thingName
	 * @return
	 *		{
	 *			"thingGroups" : [ "string" ]
	 *		}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/things/{thingName}/thing-groups")
	public ResponseEntity listThingGroupsForThing(@PathVariable("thingName") String thingName) {
		//
		try {
			checkRegExpForRequestDescribeThing(thingName);
		} catch (ExceptionThingBadRequest e) {
			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		RespoonseListThingGroupsForThing response = new RespoonseListThingGroupsForThing();
		try {
			List<String> thingGroups = thingGroupManager.getNamesOfThingGroupsForThing(thingName);
			response.setThingGroups(thingGroups);
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	private Boolean checkRegExpForRequestCreateThingGroup(String thingGroupName, RequestCreateThingGroup request) throws ExceptionThingGroupBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getParentGroupName(), regExpGroup, true))
			throw new ExceptionThingGroupBadRequest();
		
		if (request.getThingGroupProperties() == null)
			throw new ExceptionThingGroupBadRequest();
		
		if (request.getThingGroupProperties().getAttributePayload() == null)
			throw new ExceptionThingGroupBadRequest();
		
		for (Entry<String, String> entry : request.getThingGroupProperties().getAttributePayload().getAttributes().entrySet()) {
			//
			String key = entry.getKey();
			String value = entry.getValue();	
			
			if (!RegExpUtil.isValidExpression(key, regExpAttributeKey, false)) {
				throw new ExceptionThingGroupBadRequest();
			}

			if (!RegExpUtil.isValidExpression(value, regExpAttributeValue, true)) {
				throw new ExceptionThingGroupBadRequest();
			}
		}
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateThingGroup(String thingGroupName, RequestUpdateThingGroup request) throws ExceptionThingGroupBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		if (request.getThingGroupProperties() == null)
			throw new ExceptionThingGroupBadRequest();
		
		if (request.getThingGroupProperties().getAttributePayload() == null)
			throw new ExceptionThingGroupBadRequest();
		
		for (Entry<String, String> entry : request.getThingGroupProperties().getAttributePayload().getAttributes().entrySet()) {
			//
			String key = entry.getKey();
			String value = entry.getValue();	
			
			if (!RegExpUtil.isValidExpression(key, regExpAttributeKey, false)) {
				throw new ExceptionThingGroupBadRequest();
			}

			if (!RegExpUtil.isValidExpression(value, regExpAttributeValue, true)) {
				throw new ExceptionThingGroupBadRequest();
			}
		}
		
		if (request.getExpectedVersion() == null)
			throw new ExceptionThingGroupBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestThingGroup(String thingGroupName) throws ExceptionThingGroupBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestAddThingToThingGroup(RequestAddThingToThingGroup request) throws ExceptionThingGroupBadRequest {
		//
		if (!RegExpUtil.isValidExpression(request.getThingGroupName(), regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateThingGroupForThing(RequestUpdateThingGroupForThing request) throws ExceptionThingGroupBadRequest {
		//
		List<String> thingGroupsToAdd = request.getThingGroupsToAdd();
		for (String iter : thingGroupsToAdd) {
			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
				throw new ExceptionThingGroupBadRequest();
		}
		
		List<String> thingGroupsToRemove = request.getThingGroupsToRemove();
		for (String iter : thingGroupsToRemove) {
			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
				throw new ExceptionThingGroupBadRequest();
		}
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestRemoveThingFromThingGroup(RequestRemoveThingFromThingGroup request) throws ExceptionThingGroupBadRequest {
		//
		if (!RegExpUtil.isValidExpression(request.getThingGroupName(), regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionThingGroupBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestDescribeThing(String thingName) throws ExceptionThingBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpGroup, false))
			throw new ExceptionThingBadRequest();
		
		return true;
	}
}