package com.solitardj9.microiot.instance.instanceManager.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {

	private static final long serialVersionUID = -4100048561526911504L;
	
	private String groupName;
	
	private ConcurrentHashMap<String, Instance> instances;
}