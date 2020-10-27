package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateThingAttributePayload {
	
	private Map<String, String> attributes;
	
	private Boolean merge;
}