package com.solitardj9.microiot.application.thing.groupManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.microiot.application.thing.groupManager.service.dao.dto.ThingGroupDto;

public interface ThingGroupDao extends JpaRepository<ThingGroupDto, Integer> {

	ThingGroupDto findByThingGroupName(String thingGroupName);
}