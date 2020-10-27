package com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.request;

import com.solitardj9.microiot.serviceInterface.thing.groupManagerInterface.model.common.ThingGroupProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestUpdateThingGroup extends RequestDefault {

	private Integer expectedVersion;
	
	private ThingGroupProperties thingGroupProperties;
}