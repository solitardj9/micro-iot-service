package com.solitardj9.microiot.application.thing.thingManager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.application.thing.thingManager.model.Thing;
import com.solitardj9.microiot.application.thing.thingManager.model.ThingToken;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingTokenNotAvailable;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingVersionMismatch;
import com.solitardj9.microiot.application.thing.thingManager.service.ThingManager;
import com.solitardj9.microiot.application.thing.thingManager.service.dao.ThingDao;
import com.solitardj9.microiot.application.thing.thingManager.service.dao.ThingNativeQueryDao;
import com.solitardj9.microiot.application.thing.thingManager.service.dao.dto.ThingDto;
import com.solitardj9.microiot.instance.instanceManager.model.Group;
import com.solitardj9.microiot.instance.instanceManager.service.InstanceManager;
import com.solitardj9.microiot.util.jsonUtil.JsonUtil;

@Service("thingManager")
public class ThingManagerImpl implements ThingManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ThingManagerImpl.class);

	@Autowired
	ThingNativeQueryDao thingNativeQueryDao;
	
	@Autowired
	ThingDao thingDao;
	
	@Autowired
	InstanceManager instanceManager;
	
	private Boolean isInitialized = false;
	
	private ObjectMapper om = new ObjectMapper();
	
	private Integer index = 1;
	
	@PostConstruct
	public void init() {
		//
		thingNativeQueryDao.createThingTable();
		
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
	@Override
	public Thing createThing(String thingName, String thingTypeName, Map<String, String> attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].createThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto != null) {
			logger.error("[ThingManager].createThing : error = thing is already exist.");
			throw new ExceptionThingAlreayExist();
		}
		
		try {
			String strAttributes = om.writeValueAsString(attributes);
			
			String token = createThingTokenAsString(thingName);
			
			// TODO : create queue
			
			// change last null to token as string 
			thingDto = new ThingDto(null, thingName, 1, strAttributes, thingTypeName, token);
			
			saveThing(thingDto);
			
			return makeThingDtoToThing(getThing(thingName));
		} catch (Exception e) {
			logger.error("[ThingManager].createThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}
	
	private String createThingTokenAsString(String thingName) throws ExceptionThingTokenNotAvailable {
		//
		try {
			return om.writeValueAsString(createThingToken(thingName));
		} catch (ExceptionThingTokenNotAvailable e) {
			logger.error("[ThingManager].createThingTokenAsString : error = " + e);
			throw e;
		} catch (JsonProcessingException e) {
			logger.error("[ThingManager].createThingTokenAsString : error = " + e);
			throw new ExceptionThingTokenNotAvailable();
		} 
	}
	
	private ThingToken createThingToken(String thingName) throws ExceptionThingTokenNotAvailable {
		//
		try {
			Map<String, Group> availableTopology = instanceManager.selectTopologyWithAliveInstance();
			
			if (index >= Integer.MAX_VALUE)
				index = 1;
			
			Integer count = 0;
			for (Entry<String, Group> iter : availableTopology.entrySet()) {
				if (iter.getValue().getInstances().size() > 0) {
					count++;
				}
			}
			Integer rest = index % count;
			Group group = (Group) availableTopology.values().toArray()[rest];
		
			if (group == null) {
				logger.error("[ThingManager].createThingToken : error = no available group.");
				throw new ExceptionThingTokenNotAvailable();
			}
			else {
				index += 1;
				return new ThingToken(group.getGroupName(), group.getGroupName(), thingName);
			}
		} catch (Exception e) {
			logger.error("[ThingManager].createThingToken : error = " + e);
			throw new ExceptionThingTokenNotAvailable();
		}
	}

	@Override
	public Thing getThingByThingName(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].getThingByThingName : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto == null) {
			logger.error("[ThingManager].getThingByThingName : error = thing is not exist.");
			throw new ExceptionThingNotFound();
		}
		
		return makeThingDtoToThing(thingDto);
	}

	@Override
	public Boolean updateThing(String thingName, String thingTypeName, Boolean removeThingType,
							   Map<String, String> attributes, Boolean merge, Integer expectedVersion) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingVersionMismatch, ExceptionThingManagerFailure {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].updateThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto == null) {
			logger.error("[ThingManager].updateThing : error = thing is not exist.");
			throw new ExceptionThingNotFound();
		}
		
		Integer version = thingDto.getVersion();
		if (expectedVersion != null && !version.equals(expectedVersion)) { 
			logger.error("[ThingManager].updateThing : error = thing version is mismatch.");
			throw new ExceptionThingVersionMismatch();
		}
		
		if (removeThingType)
			thingDto.setThingTypeName(null);
		else
			thingDto.setThingTypeName(thingTypeName);
		
		String strAttributes;
		try {
			strAttributes = om.writeValueAsString(attributes);
			if (merge) {
				String oldAttributes = thingDto.getAttributes();
				String mergedAttributes = JsonUtil.mergeJsonString(oldAttributes, strAttributes);
				thingDto.setAttributes(mergedAttributes);
			}
			else {
				thingDto.setAttributes(strAttributes);
			}
		} catch (JsonProcessingException e) {
			logger.error("[ThingManager].updateThing : error = attributes is invallid.");
		}
		
		try {
			thingDto.setVersion(thingDto.getVersion() + 1);
			saveThing(thingDto);
			
			return true;
		} catch (Exception e) {
			logger.error("[ThingManager].updateThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}

	@Override
	public Boolean deleteThing(String thingName, Integer expectedVersion) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingVersionMismatch, ExceptionThingManagerFailure {
		// TODO Auto-generated method stub
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].deleteThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto == null) {
			logger.error("[ThingManager].deleteThing : error = thing is not exist.");
			throw new ExceptionThingNotFound();
		}
		
		Integer version = thingDto.getVersion();
		if (expectedVersion != null && !version.equals(expectedVersion)) { 
			logger.error("[ThingManager].deleteThing : error = thing version is mismatch.");
			throw new ExceptionThingVersionMismatch();
		}
		
		try {
			deleteThing(thingDto);
			
			// TODO : delete queue
			// TODO : delete thing in group
			
			return true;
		} catch (Exception e) {
			logger.error("[ThingManager].deleteThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}
	
	@Override
	public List<Thing> getThings(String attributeName, String attributeValue, String thingTypeName) {
		//
		List<Thing> things = new ArrayList<>();
		
		List<ThingDto> thingDts = selectThings(attributeName, attributeValue, thingTypeName);
		if (thingDts != null) {
			for(ThingDto iter : thingDts) {
				things.add(makeThingDtoToThing(iter));
			}
		}
		
		return things;
	}
	
	private void saveThing(ThingDto thingDto) {
		//
		thingDao.save(thingDto);
	}
	
	private ThingDto getThing(String thingName) {
		//
		return thingDao.findByThingName(thingName);
	}
	
	private void deleteThing(ThingDto thingDto) {
		//
		thingDao.delete(thingDto);
	}
	
	private List<ThingDto> selectThings(String attributeName, String attributeValue, String thingTypeName) {
		//
		return thingNativeQueryDao.selectThings(thingTypeName, attributeName, attributeValue);
	}
	
	@SuppressWarnings("unchecked")
	private Thing makeThingDtoToThing(ThingDto thingDto) {
		//
		try {
			Map<String, String> attributes = om.readValue(thingDto.getAttributes(), Map.class);
			ThingToken token = om.readValue(thingDto.getToken(), ThingToken.class);
			
			return new Thing(thingDto.getId(), thingDto.getThingName(), thingDto.getVersion(), attributes, thingDto.getThingTypeName(), token);
		} catch (JsonProcessingException e) {
			logger.error("[ThingManager].createThing : makeThingDtoToThing = " + e);
			return null;
		}
	}
}