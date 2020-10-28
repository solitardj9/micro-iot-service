package com.solitardj9.microiot.application.thing.groupManager.service.dao.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="thing_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroupDto {
	//
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)							
	private Integer id;
	
	@Column(name="thing_group_name")
	private String thingGroupName;
	
	@Column(name="version")
	private Integer version;
	
	@Column(name="attributes")
	private String attributes;
	
	@Column(name="parent_group_name")
	private String parentGroupName;
	
	@Column(name="thing_group_description")
	private String thingGroupDescription;
	
	@Column(name="root_group_name")
	private String rootGroupName;
}