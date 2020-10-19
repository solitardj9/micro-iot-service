package com.solitardj9.microiot.instance.instanceManager.model;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group implements Serializable {

	private static final long serialVersionUID = -4100048561526911504L;
	
	private String groupName;
	
	private CopyOnWriteArrayList<Instance> instances;

	public Group() {
		
	}
	
	public Group(String groupName, CopyOnWriteArrayList<Instance> instances) {
		this.groupName = groupName;
		this.instances = instances;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(CopyOnWriteArrayList<Instance> instances) {
		this.instances = instances;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((instances == null) ? 0 : instances.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (instances == null) {
			if (other.instances != null)
				return false;
		} else if (!instances.equals(other.instances))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [groupName=" + groupName + ", instances=" + instances + "]";
	}
}