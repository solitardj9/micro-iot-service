package com.solitardj9.microiot.instance.instanceManager.service.data;

public class InstanceManagerParams {
	//
	public enum InstanceManagerMapEnum {
	    //
		TOPOLOGY_MAP("topologyMap")
		;
		
		private String type;
		
		private InstanceManagerMapEnum(String type) {
			this.type = type;
	    }
	    
		public String getType() { 
	        return type;
	    }
	    
	    @Override
	    public String toString() {
	        return type;
	    }
	}
}