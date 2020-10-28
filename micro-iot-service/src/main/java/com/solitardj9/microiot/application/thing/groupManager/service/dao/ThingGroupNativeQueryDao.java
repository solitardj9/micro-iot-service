package com.solitardj9.microiot.application.thing.groupManager.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository("thingGroupNativeQueryDao")
public class ThingGroupNativeQueryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createGroupTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS thing_group("
				   + "id						int NOT NULL AUTO_INCREMENT, "
				   + "thing_group_name			varchar(128) DEFAULT NULL, "
				   + "version					int DEFAULT NULL, "
				   + "attributes				LONGTEXT DEFAULT NULL, "
				   + "parent_group_name			varchar(128) DEFAULT NULL, "
				   + "thing_group_description	varchar(2028) DEFAULT NULL, "
				   + "root_group_name			varchar(128) DEFAULT NULL, "
				   + "PRIMARY KEY PKEY_GROUP (id),"
				   + "CONSTRAINT UQ_GROUP UNIQUE (thing_group_name));";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
	
	@Transactional
	public Integer createGroupTreeTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS thing_group_tree("
				   + "root_name			varchar(128) DEFAULT NULL, "
				   + "group_tree		LONGTEXT DEFAULT NULL, "
				   + "PRIMARY KEY PKEY_GROUP_TREE (root_name));";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
}
