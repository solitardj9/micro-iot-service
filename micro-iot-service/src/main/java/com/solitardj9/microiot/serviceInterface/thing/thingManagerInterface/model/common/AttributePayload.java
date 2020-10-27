package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.common;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributePayload {
	
	private Map<String, String> attributes;
	
	private Boolean merge;
}