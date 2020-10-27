package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroupMetadata {
	
	private String parentGroupName;
	
	private List<String> rootToParentThingGroups;
}
