package com.solitardj9.microiot.application.thing.groupManager.service.data;

public class ThingGroupManagerParams {
	
	public enum ThingGroupInMemoryMap {
	    //
		THING_GROUP("thingGroup"),
		THING_GROUP_TREE("thingGroupTree")
		;
		
		private String mapName;
		
		private ThingGroupInMemoryMap(String mapName) {
			this.mapName = mapName;
	    }
	    
		public String getMapName() { 
	        return mapName;
	    }
	    
	    @Override
	    public String toString() {
	        return mapName;
	    }
	}
}