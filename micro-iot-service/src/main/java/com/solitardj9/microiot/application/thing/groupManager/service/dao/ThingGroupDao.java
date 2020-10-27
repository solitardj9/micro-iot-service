package com.solitardj9.microiot.application.thing.groupManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.microiot.application.thing.thingManager.service.dao.dto.ThingDto;

public interface ThingGroupDao extends JpaRepository<ThingDto, Integer> {

	ThingDto findByThingName(String thingName);
}