package com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseCreateThing extends ResponseDefault {
	
	private String thingId;
	
	private String thingName;
}