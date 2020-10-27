package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request;

import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.common.AttributePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestCreateThing extends RequestDefault {
	
	private AttributePayload attributePayload;
	
	private String thingTypeName;
}