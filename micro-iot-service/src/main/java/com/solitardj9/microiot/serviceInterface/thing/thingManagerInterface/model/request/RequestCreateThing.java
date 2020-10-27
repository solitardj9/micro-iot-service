package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestCreateThing extends RequestDefault {
	
	private RequestCreateThingAttributePayload attributePayload;
	
	private String thingTypeName;
}