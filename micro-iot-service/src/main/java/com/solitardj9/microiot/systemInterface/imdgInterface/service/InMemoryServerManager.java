package com.solitardj9.microiot.systemInterface.imdgInterface.service;

import com.hazelcast.core.IMap;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.InMemoryInstane;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastDistributedObjectNameConflict;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapBadRequest;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyClosed;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyOpened;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerConfigError;

public interface InMemoryServerManager {
	//
	public Boolean startServer() throws ExceptionHazelcastServerAlreadyOpened, ExceptionHazelcastServerConfigError;
	
	public Boolean stopServer() throws ExceptionHazelcastServerAlreadyClosed;
	
	public IMap<Object, Object> createMap(String map) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastDistributedObjectNameConflict;
	
	public IMap<Object, Object> createMap(InMemoryInstane inMemoryInstane) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastDistributedObjectNameConflict, ExceptionHazelcastIMapBadRequest;
	
	public IMap<Object, Object> getMap(String map) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound;
	
	public void clearMap(String map) throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound;
}