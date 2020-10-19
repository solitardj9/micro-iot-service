package com.solitardj9.microiot.instance.instanceManager.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solitardj9.microiot.instance.instanceManager.model.Group;
import com.solitardj9.microiot.instance.instanceManager.model.Instance;
import com.solitardj9.microiot.instance.instanceManager.service.InstanceManager;
import com.solitardj9.microiot.instance.instanceManager.service.data.InstanceManagerParams.InstanceManagerMapEnum;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.InMemoryInstane;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastDistributedObjectNameConflict;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapBadRequest;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyClosed;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.InMemoryServerManager;

@Service("instanceManager")
public class InstanceManagerImpl implements InstanceManager {
	//
	private static final Logger logger = LoggerFactory.getLogger(InstanceManagerImpl.class);
	
	@Value("${application.group}")
	private String groupName;
	
	@Value("${application.instance}")
	private String instanceName;
	
	@Value("${instance.topologyMap.backupCount}")
	private String topologyMapBackupCount;
	
	
	@Autowired
	InMemoryServerManager inMemoryServerManager;
	
	@PostConstruct
	public void init() {
		//
		try {
			createTopologyMap();
			
			updateTopologyMap();
			
			logger.info("[InstanceManager].init : topology map = " + readTopologyMap().toString());
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastDistributedObjectNameConflict | ExceptionHazelcastIMapBadRequest | ExceptionHazelcastIMapNotFound e) {
			logger.error("[InstanceManager].init : error = " + e);
		}
		
		
	}
	
	private void createTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastDistributedObjectNameConflict, ExceptionHazelcastIMapBadRequest {
		//
		InMemoryInstane inMemoryInstane = new InMemoryInstane();

		inMemoryInstane.setName(InstanceManagerMapEnum.TOPOLOGY_MAP.getType());
		inMemoryInstane.setBackupCount(Integer.valueOf(topologyMapBackupCount));
		inMemoryInstane.setReadBackupData(true);
		
		inMemoryServerManager.createMap(inMemoryInstane);
	}
	
	private void updateTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		Object object = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).get(groupName);
		
		Group group = null;
		if (object != null) {
			group = (Group)object;
		}
		else {
			group = new Group(groupName, new CopyOnWriteArrayList<Instance>());
		}
		
		group.getInstances().add(new Instance(instanceName));
		
		inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).put(groupName, group);				
	}

	private Map<String, Group> readTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		Map<String, Group> topolocyMap = new HashMap<>();
		
		Map<Object, Object> objectTopolocyMap = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType());
		
		for (Entry<Object, Object> iter : objectTopolocyMap.entrySet()) {
			topolocyMap.put((String)iter.getKey(), (Group)iter.getValue());
		}
		
		return topolocyMap;
	}
}