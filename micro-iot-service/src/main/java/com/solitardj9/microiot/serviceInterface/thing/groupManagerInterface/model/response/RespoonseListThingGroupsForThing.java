package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RespoonseListThingGroupsForThing extends ResponseDefault {
	
	private List<String> thingGroups;
}