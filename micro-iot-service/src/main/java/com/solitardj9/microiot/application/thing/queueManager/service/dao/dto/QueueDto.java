package com.solitardj9.microiot.application.thing.queueManager.service.dao.dto;

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
@Table(name="group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueDto {
	//
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)							
	private Integer id;
	
	@Column(name="thing_name")
	private String groupName;
}