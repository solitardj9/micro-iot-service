package com.solitardj9.microiot.instance.instanceManager.model;

import java.io.Serializable;

public class Instance implements Serializable {

	private static final long serialVersionUID = -3009627091522010848L;

	private String instanceName;

	public Instance() {
		
	}
	
	public Instance(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDataNodeName() {
		return instanceName;
	}

	public void setDataNodeName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public String toString() {
		return "Instance [instanceName=" + instanceName + "]";
	}
}