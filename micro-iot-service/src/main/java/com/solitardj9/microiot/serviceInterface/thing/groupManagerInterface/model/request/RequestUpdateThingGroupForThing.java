package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestUpdateThingGroupForThing extends RequestDefault {
	
	private List<String> thingGroupsToAdd;
	
	private List<String> thingGroupsToRemove;
	
	private String thingName;
}