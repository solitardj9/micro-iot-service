package com.solitardj9.microiot.application.thing.groupManager.model;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroup implements Serializable {

	private static final long serialVersionUID = 4124664944461718183L;

	private Integer id;
	
	private String thingGroupName;
	
	private Integer version;
	
	private Map<String, String> attributes;
	
	private String parentGroupName;
	
	private String thingGroupDescription;
}
