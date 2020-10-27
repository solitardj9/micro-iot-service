package com.solitardj9.microiot.application.thing.thingManager.service;

import java.util.List;
import java.util.Map;

import com.solitardj9.microiot.application.thing.thingManager.model.Thing;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingVersionMismatch;

public interface ThingManager {
	//
	public Boolean isInitialized();
	
	public Thing createThing(String thingName, String thingTypeName, Map<String, String> attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure;
	
	public Thing getThingByThingName(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound;
	
	public Boolean updateThing(String thingName, String thingTypeName, Boolean removeThingType, Map<String, String> attributes, Boolean merge, Integer expectedVersion) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingVersionMismatch, ExceptionThingManagerFailure;
	
	public Boolean deleteThing(String thingName, Integer expectedVersion) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingVersionMismatch, ExceptionThingManagerFailure;
	
	public List<Thing> getThings(String attributeName, String attributeValue, String thingTypeName);
}