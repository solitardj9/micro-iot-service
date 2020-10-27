package com.solitardj9.microiot.application.thing.groupManager.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroup {

	private Integer id;
	
	private String thingGroupName;
	
	private Integer version;
	
	private Map<String, String> attributes;
	
	private String parentGroupName;
	
	private String thingGroupDescription;
}
