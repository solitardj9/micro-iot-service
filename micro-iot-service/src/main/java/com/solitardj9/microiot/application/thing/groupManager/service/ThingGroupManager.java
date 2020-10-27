package com.solitardj9.microiot.application.thing.groupManager.service;

import java.util.List;
import java.util.Map;

import com.solitardj9.microiot.application.thing.groupManager.model.ThingGroup;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupAlreayExist;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupManagerFailure;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupNotFound;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupVersionMismatch;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;

public interface ThingGroupManager {
	//
	public Boolean isInitialized();
	
	public ThingGroup createThingGroup(String thingGroupName, String parentGroupName, Map<String, String> attributes, Boolean merge, String thingGroupDescription) throws ExceptionThingGroupBadRequest, ExceptionThingGroupAlreayExist, ExceptionThingGroupManagerFailure;
	
	public ThingGroup getThingGroupByThingGroupName(String thingGroupName) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound;
	
	public Integer updateThingGroup(String thingGroupName, Map<String, String> attributes, Boolean merge, Integer expectedVersion) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound, ExceptionThingGroupVersionMismatch, ExceptionThingGroupManagerFailure;
	
	public Boolean deleteThingGroup(String thingGroupName, Integer expectedVersion) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound, ExceptionThingGroupVersionMismatch, ExceptionThingGroupManagerFailure;
	
	public List<String> getNamesByParentGroup(String parentGroupName, Boolean recursive);
	
	public List<String> getNamesOfRootToGroup(String thingGroupName) throws ExceptionThingGroupNotFound;
	
	public Boolean addThingToThingGroup(String thingGroupName, String thingName) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound;
	
	public Boolean updateThingGroupsForThing(List<String> thingGroupsToAdd, List<String> thingGroupsToRemove, String thingName) throws ExceptionThingGroupBadRequest;
	
	public Boolean removeThingFromThingGroup(String thingGroupName, String thingName) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound;
	
	public List<String> getNamesOfThingsInThingGroup(String thingGroupName, Boolean recursive) throws ExceptionThingGroupNotFound, ExceptionThingGroupManagerFailure;
	
	public List<String> getNamesOfThingGroupsForThing(String thingName) throws ExceptionThingNotFound, ExceptionThingGroupManagerFailure;
}