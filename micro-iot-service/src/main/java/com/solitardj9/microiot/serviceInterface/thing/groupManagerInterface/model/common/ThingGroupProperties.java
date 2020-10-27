package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroupProperties {
	
	private AttributePayload attributePayload;
	
	private String thingGroupDescription;
}
