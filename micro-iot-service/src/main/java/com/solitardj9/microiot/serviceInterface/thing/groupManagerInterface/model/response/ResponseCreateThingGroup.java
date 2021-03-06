package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseCreateThingGroup extends ResponseDefault {
	
	private String thingGroupId;
	
	private String thingGroupName;
}