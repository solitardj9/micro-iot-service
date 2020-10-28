package com.solitardj9.microiot.application.thing.groupManager.service.data;

import java.io.Serializable;
import java.util.Map;

import com.solitardj9.microiot.application.thing.groupManager.model.ThingGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class ThingGroupEx extends ThingGroup implements Serializable {
	
	private static final long serialVersionUID = -3114041073436636856L;

	private String rootGroupName;
	
	public ThingGroupEx(Integer id, String thingGroupName, Integer version, Map<String, String> attributes, String parentGroupName, String thingGroupDescription, String rootGroupName) {
		//
		super(id, thingGroupName, version, attributes, parentGroupName, thingGroupDescription);
		this.rootGroupName = rootGroupName;
	}
}
