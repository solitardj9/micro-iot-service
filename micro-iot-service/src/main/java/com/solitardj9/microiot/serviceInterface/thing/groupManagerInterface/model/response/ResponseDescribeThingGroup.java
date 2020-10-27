package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.response;

import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.ThingGroupMetadata;
import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.ThingGroupProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseDescribeThingGroup extends ResponseDefault {
	
	private String thingGroupId;
	
	private String thingGroupName;
	
	private ThingGroupProperties thingGroupProperties;
	
	private Integer version;
	
	private ThingGroupMetadata thingGroupMetadata;
}