package com.solitardj9.microiot.instance.instanceManager.service;

import java.util.Map;

import com.solitardj9.microiot.instance.instanceManager.model.Group;

public interface InstanceManager {
	
	public void checkHealth();
	
	public Map<String, Group> getTopology();
	
	public Map<String, Group> selectTopologyWithAliveInstance();
}