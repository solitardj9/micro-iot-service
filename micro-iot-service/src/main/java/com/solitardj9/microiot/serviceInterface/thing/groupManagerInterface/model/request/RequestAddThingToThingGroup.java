package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestAddThingToThingGroup extends RequestDefault {
	
	private String thingGroupName;
	
	private String thingName;
}