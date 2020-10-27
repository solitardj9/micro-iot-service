package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestUpdateThing extends RequestCreateThing {
	
	 private Integer expectedVersion;
	 
	 private Boolean removeThingType;
}