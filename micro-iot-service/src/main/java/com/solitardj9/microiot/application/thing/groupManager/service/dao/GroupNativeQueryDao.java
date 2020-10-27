package com.solitardj9.microiot.application.thing.groupManager.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository("groupNativeQueryDao")
public class GroupNativeQueryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createThingTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS thing("
				   + "id				int NOT NULL AUTO_INCREMENT, "
				   + "thing_name		varchar(128) DEFAULT NULL, "
				   + "version			int DEFAULT NULL, "
				   + "attributes		LONGTEXT DEFAULT NULL, "
				   + "thing_type_name	varchar(128) DEFAULT NULL, "
				   + "token				LONGTEXT DEFAULT NULL, "
				   + "PRIMARY KEY PKEY (id));";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
}
