package com.solitardj9.microiot.application.thing.groupManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.microiot.application.thing.groupManager.service.dao.dto.ThingGroupTreeDto;

public interface ThingGroupTreeDao extends JpaRepository<ThingGroupTreeDto, String> {

}