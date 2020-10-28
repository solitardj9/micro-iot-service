package com.solitardj9.microiot.application.thing.groupManager.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import com.solitardj9.microiot.application.thing.groupManager.model.ThingGroup;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupAlreayExist;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupManagerFailure;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupNotFound;
import com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupVersionMismatch;
import com.solitardj9.microiot.application.thing.groupManager.service.ThingGroupManager;
import com.solitardj9.microiot.application.thing.groupManager.service.dao.ThingGroupDao;
import com.solitardj9.microiot.application.thing.groupManager.service.dao.ThingGroupNativeQueryDao;
import com.solitardj9.microiot.application.thing.groupManager.service.dao.ThingGroupTreeDao;
import com.solitardj9.microiot.application.thing.groupManager.service.dao.dto.ThingGroupDto;
import com.solitardj9.microiot.application.thing.groupManager.service.dao.dto.ThingGroupTreeDto;
import com.solitardj9.microiot.application.thing.groupManager.service.data.LayeredGroup;
import com.solitardj9.microiot.application.thing.groupManager.service.data.ThingGroupEx;
import com.solitardj9.microiot.application.thing.groupManager.service.data.ThingGroupManagerParams.ThingGroupInMemoryMap;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastDistributedObjectNameConflict;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyClosed;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.InMemoryServerManager;

@Service("thingGroupManager")
public class ThingGroupManagerImpl implements ThingGroupManager {

	private static final Logger logger = LoggerFactory.getLogger(ThingGroupManagerImpl.class);
	
	@Autowired
	ThingGroupDao thingGroupDao;
	
	@Autowired
	ThingGroupTreeDao thingGroupTreeDao;
	
	@Autowired
	ThingGroupNativeQueryDao thingGroupNativeQueryDao;
	
	@Autowired
	InMemoryServerManager inMemoryServerManager;
	
	private Boolean isInitialized = false;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		//
		createTable();
		
		createMap();
		
		intialize();
		
		isInitialized = true;
	}
	
	private void createTable() {
		//
		thingGroupNativeQueryDao.createGroupTable();
		thingGroupNativeQueryDao.createGroupTreeTable();
	}
	
	private void createMap() {
		//
		try {
			inMemoryServerManager.createMap(ThingGroupInMemoryMap.THING_GROUP.getMapName());
			inMemoryServerManager.createMap(ThingGroupInMemoryMap.THING_GROUP_TREE.getMapName());
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastDistributedObjectNameConflict e) {
			logger.error("[ThingGroupManager].createMap : error = " + e);
		}
	}
	
	private void intialize() {
		//
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
	@Override
	public ThingGroup createThingGroup(String thingGroupName, 
									   String parentGroupName, 
									   Map<String, String> attributes, 
									   Boolean merge,
									   String thingGroupDescription) throws ExceptionThingGroupBadRequest, ExceptionThingGroupAlreayExist, ExceptionThingGroupManagerFailure {
		//
		try {
			if (thingGroupName == null || thingGroupName.isEmpty())
				throw new ExceptionThingGroupBadRequest();
			
			if (!isExistThingGroupNameFromMemCluster(thingGroupName)) {
				// 
				addThingGroupToClusters(thingGroupName, parentGroupName, attributes, thingGroupDescription);
				
				ThingGroupEx thingGroupEx = getThingGroupFromMemCluster(thingGroupName);
				if (thingGroupEx != null)
					return makeThingGroupExToThingGroup(thingGroupEx);
				else
					return null;
			}
			else {
				logger.error("[ThingGroupManager].createThingGroup : error = Thing group is already exist.");
				throw new ExceptionThingGroupAlreayExist();
			}
		} catch(Exception e) {
			logger.error("[ThingGroupManager].createThingGroup : error = " + e);
			throw new ExceptionThingGroupManagerFailure();
		}
	}
	
	private Boolean addThingGroupToClusters(String thingGroupName, 
											String parentGroupName, 
											Map<String, String> attributes, 
											String thingGroupDescription) {
		//
		if (parentGroupName == null || parentGroupName.isEmpty()) {
			return createRootThingGroupToClusters(thingGroupName, attributes, thingGroupDescription);
		}
		else {
			return addChildThingGroupToClusters(thingGroupName, parentGroupName, attributes, thingGroupDescription);
		}
	}
	
	private Boolean createRootThingGroupToClusters(String thingGroupName, 
												   Map<String, String> attributes,
												   String thingGroupDescription) {
		//
		ThingGroupEx thingGroupEx = new ThingGroupEx(null, thingGroupName, 1, attributes, null, thingGroupDescription, thingGroupName); // root and group is same.
		
		if (addThingGroupToDBCluster(thingGroupEx)) {
			ThingGroupEx savedThingGroupEx;
			try {
				savedThingGroupEx = makeThingGroupDtoToThingGroupEx(getThingGroupDto(thingGroupName));
				addThingGroupToMemCluster(savedThingGroupEx);
				
				Map<String, TreeNode<String>> thingGroupTree = createThingGroupTree(thingGroupName);
				
				if (addThingGroupTreeToDBCluster(thingGroupName, thingGroupTree)) {
					addThingGroupTreeToMemCluster(thingGroupName, thingGroupTree);
				}
				
			} catch (JsonProcessingException e) {
				logger.error("[ThingGroupManager].createRootThingGroupToClusters : error = " + e);
				return false;
			}
			return true;
		}
		return false;
	}
	
	private Map<String, TreeNode<String>> createThingGroupTree(String rootGroupName) {
		//
		Map<String, TreeNode<String>> groupTree = new HashMap<>();
		
		// make tree node for root group
		TreeNode<String> groupNode = new ArrayMultiTreeNode<String>(rootGroupName);
		groupTree.put(rootGroupName, groupNode);
		
		return groupTree;
	}
	
	// A group can have at most one direct parent.
	private Boolean addChildThingGroupToClusters(String thingGroupName,
												 String parentGroupName,  
												 Map<String, String> attributes, 
												 String thingGroupDescription) {
		//
		try {
			if (!isExistThingGroupNameFromMemCluster(parentGroupName))
				return false;
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].addChildThingGroupToClusters : error = " + e);
			return false;
		}
			
		String rootGroupName = null;
		try {
			rootGroupName = getRootThingGroupNameFromMemCluster(parentGroupName);
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].addChildThingGroupToClusters : error = " + e);
			return false;
		}
		
		try {
			lockThingGroupTreeInMemCluster(rootGroupName);
			lockThingGroupInMemCluster(parentGroupName);
					
			Map<String, TreeNode<String>> thingGroupTree = getGroupTreeFromMemCluster(rootGroupName);

			// make tree node for child group
			TreeNode<String> groupNode = new ArrayMultiTreeNode<>(thingGroupName);
			
			// find tree node for parent group (already exist)
			TreeNode<String> parentGroupNode = thingGroupTree.get(parentGroupName);
			parentGroupNode.add(groupNode);
			thingGroupTree.put(thingGroupName, groupNode);
			
			//------------------------------------------------
			ThingGroupEx thingGroupEx = new ThingGroupEx(null, thingGroupName, 1, attributes, parentGroupName, thingGroupDescription, rootGroupName);
			
			if (addThingGroupToDBCluster(thingGroupEx)) {
				ThingGroupEx savedThingGroupEx = makeThingGroupDtoToThingGroupEx(getThingGroupDto(thingGroupName));
				addThingGroupToMemCluster(savedThingGroupEx);
					
				if (addThingGroupTreeToDBCluster(rootGroupName, thingGroupTree)) {
					addThingGroupTreeToMemCluster(rootGroupName, thingGroupTree);
				}
			}
			//------------------------------------------------
			
			unlockThingGroupInMemCluster(parentGroupName);
			unlockThingGroupTreeInMemCluster(rootGroupName);
					
			return true;
		} catch (Exception e) {
			logger.error("[ThingGroupManager].addChildThingGroupToClusters : error = " + e);
			
			try {
				unlockThingGroupInMemCluster(parentGroupName);
				unlockThingGroupTreeInMemCluster(rootGroupName);
			} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e1) {
				logger.error("[ThingGroupManager].addChildThingGroupToClusters : error = " + e);
				return false;
			}
					
			return false;
		}
	}
	
	private Boolean addThingGroupToDBCluster(ThingGroupEx thingGroupEx) {
		//
		try {
			ThingGroupDto thingGroupDto = new ThingGroupDto(thingGroupEx.getId(), 
															thingGroupEx.getThingGroupName(), 
															thingGroupEx.getVersion(), 
															om.writeValueAsString(thingGroupEx.getAttributes()), 
															thingGroupEx.getParentGroupName(), 
															thingGroupEx.getThingGroupDescription(), 
															thingGroupEx.getRootGroupName());
			
			saveThingGroupDto(thingGroupDto);
			return true;
		} catch (Exception e) {
			logger.error("[ThingGroupManager].addThingGroupToDBCluster : error = Fail to add ThingGroup to DB.");
			return false;
		}
	}
	
	private Boolean addThingGroupToMemCluster(ThingGroupEx thingGroupEx) {
		//
		try {
			inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).put(thingGroupEx.getThingGroupName(), thingGroupEx);
			
			System.out.println(inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).get(thingGroupEx.getThingGroupName()));
			return true;
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].addThingGroupMapToMemCluster : error = Fail to add ThingGroup to Mem.");
			return false;
		}
	}
	
	private ThingGroupEx getThingGroupFromMemCluster(String thingGroupName) {
		//
		try {
			return (ThingGroupEx)inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).get(thingGroupName);
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].addThingGroupMapToMemCluster : error = Fail to add ThingGroup to Mem.");
			return null;
		}
	}
	
	private Boolean addThingGroupTreeToDBCluster(String rootGroupName, Map<String, TreeNode<String>> thingGroupTree) {
		//
		try {
			TreeNode<String> rootNode = thingGroupTree.get(rootGroupName);
			LayeredGroup layeredGroup = convertGroupTreeToLayeredGroup(rootNode);
			
			ThingGroupTreeDto thingGroupTreeDto = new ThingGroupTreeDto(rootGroupName, om.writeValueAsString(layeredGroup));
			saveThingGroupTreeDto(thingGroupTreeDto);
			
			return true;
		} catch (Exception e) {
			logger.error("[ThingGroupManager].addThingGroupTreeToDBCluster : error = Fail to add ThingGroupTree to DB.");
			return false;
		}
	}

	private Boolean addThingGroupTreeToMemCluster(String rootGroupName, Map<String, TreeNode<String>> groupTree) {
		//
		try {
			inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP_TREE.getMapName()).put(rootGroupName, groupTree);
			return true;
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].addGroupTreeToMemCluster : error = Fail to add ThingGroupTree to Mem.");
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, TreeNode<String>> getGroupTreeFromMemCluster(String rootGroupName) {
		//
		Map<String, TreeNode<String>> thingGroupTree = null;
		try {
			thingGroupTree = (Map<String, TreeNode<String>>)inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP_TREE.getMapName()).get(rootGroupName);
			return thingGroupTree;
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ThingGroupManager].getGroupTreeFromMemCluster : error = Fail to get ThingGroupTree from Mem.");
			return thingGroupTree;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ThingGroup makeThingGroupExToThingGroup(ThingGroupEx thingGroupEx) {
		//
		return new ThingGroup(thingGroupEx.getId(), thingGroupEx.getThingGroupName(), thingGroupEx.getVersion(), thingGroupEx.getAttributes(), thingGroupEx.getParentGroupName(), thingGroupEx.getThingGroupDescription());
	}
	
	@SuppressWarnings("unchecked")
	private ThingGroupEx makeThingGroupDtoToThingGroupEx(ThingGroupDto thingGroupDto) throws JsonMappingException, JsonProcessingException {
		//
		return new ThingGroupEx(thingGroupDto.getId(), thingGroupDto.getThingGroupName(), thingGroupDto.getVersion(), om.readValue(thingGroupDto.getAttributes(), Map.class), thingGroupDto.getParentGroupName(), thingGroupDto.getThingGroupDescription(), thingGroupDto.getRootGroupName());
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
	
	
	
	
	
	private void saveThingGroupDto(ThingGroupDto thingGroupDto) {
		//
		thingGroupDao.save(thingGroupDto);
	}
	
	private ThingGroupDto getThingGroupDto(String thingGroupName) {
		//
		return thingGroupDao.findByThingGroupName(thingGroupName);
	}
	
	
	
	private void saveThingGroupTreeDto(ThingGroupTreeDto thingGroupTreeDto) {
		//
		thingGroupTreeDao.save(thingGroupTreeDto);
	}
	
	
	
	private Boolean isExistThingGroupNameFromMemCluster(String thingGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		return inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).containsKey(thingGroupName);
	}
	
	private String getRootThingGroupNameFromMemCluster(String thingGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		return ((ThingGroupEx)inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).get(thingGroupName)).getRootGroupName();
	}
	
	private void lockThingGroupInMemCluster(String thingGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).lock(thingGroupName);
	}
	
	private void unlockThingGroupInMemCluster(String thingGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP.getMapName()).unlock(thingGroupName);
	}
	
	private void lockThingGroupTreeInMemCluster(String rootGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP_TREE.getMapName()).lock(rootGroupName);
	}
	
	private void unlockThingGroupTreeInMemCluster(String rootGroupName) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		inMemoryServerManager.getMap(ThingGroupInMemoryMap.THING_GROUP_TREE.getMapName()).unlock(rootGroupName);
	}
	
	
	
	private LayeredGroup convertGroupTreeToLayeredGroup(TreeNode<String> node) {
		//
		if (node.isLeaf()) {
			LayeredGroup ret = new LayeredGroup(node.data(), (node.parent() == null) ? null : node.parent().data(), node.root().data(), null);
			return ret;
		}
		else {
			List<LayeredGroup> childGroups = new ArrayList<>();
			
			Collection<? extends TreeNode<String>> subtrees = node.subtrees();
			for (TreeNode<String> childIter : subtrees) {
				LayeredGroup childGroup = convertGroupTreeToLayeredGroup(childIter);
				childGroups.add(childGroup);
			}
			
			LayeredGroup ret = new LayeredGroup(node.data(), (node.isRoot()) ? null : node.parent().data(), node.root().data(), childGroups);
			return ret;
		}
	}
}