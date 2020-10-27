package com.solitardj9.microiot.application.thing.thingManager.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thing {

	private Integer id;
	
	private String thingName;
	
	private Integer version;
	
	private Map<String, String> attributes;
	
	private String thingTypeName;
	
	private ThingToken token;
}