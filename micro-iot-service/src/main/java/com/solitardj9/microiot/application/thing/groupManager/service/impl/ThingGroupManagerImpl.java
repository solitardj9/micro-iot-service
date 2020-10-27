package com.solitardj9.microiot.application.thing.groupManager.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.application.thing.groupManager.model.ThingGroup;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupAlreayExist;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupManagerFailure;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupNotFound;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupVersionMismatch;
import com.solitardj9.microiot.application.thing.groupManager.service.ThingGroupManager;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;

@Service("groupManager")
public class ThingGroupManagerImpl implements ThingGroupManager {

	private Boolean isInitialized = false;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		//
		//thingNativeQueryDao.createThingTable();
		
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
	@Override
	public ThingGroup createThingGroup(String thingGroupName, String parentGroupName, Map<String, String> attributes, Boolean merge,
			String thingGroupDescription) throws ExceptionThingGroupBadRequest, ExceptionThingGroupAlreayExist, ExceptionThingGroupManagerFailure {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThingGroup getThingGroupByThingGroupName(String thingGroupName) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateThingGroup(String thingGroupName, Map<String, String> attributes, Boolean merge,
			Integer expectedVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteThingGroup(String thingGroupName, Integer expectedVersion)
			throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound, ExceptionThingGroupVersionMismatch,
			ExceptionThingGroupManagerFailure {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getNamesByParentGroup(String parentGroupName, Boolean recursive) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getNamesOfRootToGroup(String thingGroupName) throws ExceptionThingGroupNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addThingToThingGroup(String thingGroupName, String thingName)
			throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateThingGroupsForThing(List<String> thingGroupsToAdd, List<String> thingGroupsToRemove,
			String thingName) throws ExceptionThingGroupBadRequest {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean removeThingFromThingGroup(String thingGroupName, String thingName) throws ExceptionThingGroupBadRequest, ExceptionThingGroupNotFound {
		// TODO Auto-generated method stub
				return null;
	}

	@Override
	public List<String> getNamesOfThingsInThingGroup(String thingGroupName, Boolean recursive) throws ExceptionThingGroupNotFound, ExceptionThingGroupManagerFailure {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getNamesOfThingGroupsForThing(String thingName)
			throws ExceptionThingNotFound, ExceptionThingGroupManagerFailure {
		// TODO Auto-generated method stub
		return null;
	}
}