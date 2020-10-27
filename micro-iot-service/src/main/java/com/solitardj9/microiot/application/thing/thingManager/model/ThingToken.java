package com.solitardj9.microiot.application.thing.thingManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingToken {
	
	private String serviceId;
	
	private String sendEndPoint;
	
	private String recvEndPoint;
}