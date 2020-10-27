package com.solitardj9.microiot.instance.instanceManager.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instance implements Serializable {

	private static final long serialVersionUID = -3009627091522010848L;

	private String instanceName;
	
	private Timestamp updatedTime;
}