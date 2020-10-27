package com.solitardj9.microiot.application.thing.queueManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.microiot.application.thing.thingManager.service.dao.dto.ThingDto;

public interface QueueDao extends JpaRepository<ThingDto, Integer> {

	ThingDto findByThingName(String thingName);
}