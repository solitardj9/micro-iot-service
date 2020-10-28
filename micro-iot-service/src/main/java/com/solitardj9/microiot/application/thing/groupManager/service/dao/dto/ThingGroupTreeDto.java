package com.solitardj9.microiot.application.thing.groupManager.service.dao.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="thing_group_tree")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingGroupTreeDto {
	//
	@Id
	@Column(name="root_name")
	private String rootName;
	
	@Column(name="group_tree")
	private String groupTree;
}