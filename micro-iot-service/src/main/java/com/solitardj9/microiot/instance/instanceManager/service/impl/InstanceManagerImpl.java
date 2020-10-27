package com.solitardj9.microiot.instance.instanceManager.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solitardj9.microiot.instance.instanceManager.model.Group;
import com.solitardj9.microiot.instance.instanceManager.model.Instance;
import com.solitardj9.microiot.instance.instanceManager.service.InstanceManager;
import com.solitardj9.microiot.instance.instanceManager.service.data.InstanceManagerParams.InstanceManagerMapEnum;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.InMemoryInstane;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastDistributedObjectNameConflict;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapBadRequest;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyClosed;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.InMemoryServerManager;
import com.solitardj9.microiot.systemInterface.schedulerInterface.service.SchedulerManager;

@Service("instanceManager")
public class InstanceManagerImpl implements InstanceManager, Job {
	//
	private static final Logger logger = LoggerFactory.getLogger(InstanceManagerImpl.class);
	
	@Autowired
	InMemoryServerManager inMemoryServerManager;
	
	@Autowired
	SchedulerManager schedulerManager;
	
	@Value("${application.group}")
	private String groupName;
	
	@Value("${application.instance}")
	private String instanceName;
	
	@Value("${instance.topologyMap.backupCount}")
	private String topologyMapBackupCount;
	
	@Value("${instance.instanceManager.healthCheckBatch}")
	private String healthCheckBatch;
	
	@Value("${instance.instanceManager.healthCheckMissTermByMs}")
	private Long healthCheckMissTermByMs;
	
	@PostConstruct
	public void init() {
		//
		try {
			createTopologyMap();
			
			updateTopologyMap();
			
			initializeScheduler();
			
			logger.info("[InstanceManager].init : topology map = " + readTopologyMap().toString());
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastDistributedObjectNameConflict | ExceptionHazelcastIMapBadRequest | ExceptionHazelcastIMapNotFound e) {
			logger.error("[InstanceManager].init : error = " + e);
		}
	}
	
	private void createTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastDistributedObjectNameConflict, ExceptionHazelcastIMapBadRequest {
		//
		InMemoryInstane inMemoryInstane = new InMemoryInstane();

		inMemoryInstane.setName(InstanceManagerMapEnum.TOPOLOGY_MAP.getType());
		inMemoryInstane.setBackupCount(Integer.valueOf(topologyMapBackupCount));
		inMemoryInstane.setReadBackupData(true);
		
		inMemoryServerManager.createMap(inMemoryInstane);
	}
	
	private void updateTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		Object object = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).get(groupName);
		
		Group group = null;
		if (object != null) {
			group = (Group)object;
		}
		else {
			group = new Group(groupName, new ConcurrentHashMap<String, Instance>());
		}
		
		group.getInstances().put(instanceName, new Instance(instanceName, new Timestamp(System.currentTimeMillis())));
		
		inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).put(groupName, group);				
	}
	
	@Override
	public Map<String, Group> getTopology() {
		//
		try {
			return readTopologyMap();
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ServiceInstancesManager].initializeScheduler : error = " + e);
			return new HashMap<String, Group>();
		}
	}
	
	@Override
	public Map<String, Group> selectTopologyWithAliveInstance() {
		//
		try {
			return readTopologyMapWithAliveInstance();
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ServiceInstancesManager].initializeScheduler : error = " + e);
			return new HashMap<String, Group>();
		}
	}

	private Map<String, Group> readTopologyMap() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		Map<String, Group> topolocyMap = new HashMap<>();
		
		Map<Object, Object> objectTopolocyMap = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType());
		
		for (Entry<Object, Object> iter : objectTopolocyMap.entrySet()) {
			topolocyMap.put((String)iter.getKey(), (Group)iter.getValue());
		}
		
		return topolocyMap;
	}
	
	private Map<String, Group> readTopologyMapWithAliveInstance() throws ExceptionHazelcastServerAlreadyClosed, ExceptionHazelcastIMapNotFound {
		//
		Timestamp criteriaTime = new Timestamp(System.currentTimeMillis());
		
		Map<String, Group> topolocyMap = new HashMap<>();
		
		Map<Object, Object> objectTopolocyMap = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType());
		
		for (Entry<Object, Object> groupIter : objectTopolocyMap.entrySet()) {
			//
			Group group = (Group)groupIter.getValue();
			
			ConcurrentHashMap<String, Instance> newInstances = new ConcurrentHashMap<>();
			
			for (Entry<String, Instance> instanceIter : group.getInstances().entrySet()) {
				//
				Instance instance = instanceIter.getValue();
				
				Long diffTime = criteriaTime.getTime() - instance.getUpdatedTime().getTime();
				if (diffTime < healthCheckMissTermByMs) {
					newInstances.put(instance.getInstanceName(), instance);
				}
			}
			Group newGroup = new Group(group.getGroupName(), newInstances);
			
			topolocyMap.put(newGroup.getGroupName(), newGroup);
		}
		
		return topolocyMap;
	}
	
	private void initializeScheduler() {
		//
		Scheduler scheduler = schedulerManager.getScheduler();
		
		try {
			if(scheduler.isStarted()) {
				String schedJobName = "CHECK_HEALTH_JOB";
				String schedJobGroup = "CHECK_HEALTH_JOB_GROUP";
				String schedTriggerName = "CHECK_HEALTH_TRIGGER";
				String schedTriggerGroup = "CHECK_HEALTH_TRIGGER_GROUP";
				
				JobDetail job = newJob(this.getClass()).withIdentity(schedJobName, schedJobGroup).storeDurably(true).build();
				
				JobDataMap jobDataMap = job.getJobDataMap();
				jobDataMap.put("instanceManager", this);
				
				CronTrigger trigger = newTrigger().withIdentity(schedTriggerName, schedTriggerGroup).withSchedule(cronSchedule(healthCheckBatch)).forJob(schedJobName, schedJobGroup).build();
				
				scheduler.scheduleJob(job, trigger);
			}
		}
		catch (SchedulerException e) {
			logger.error("[ServiceInstancesManager].initializeScheduler : error = " + e);
		}
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//
		JobDetail job = (JobDetail)context.getJobDetail();
		InstanceManager instanceManager = (InstanceManager)job.getJobDataMap().get("instanceManager");
		
		instanceManager.checkHealth();
	}
	
	@SuppressWarnings("unchecked")
	public void checkHealth() {
		//
		try {
			inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).lock(groupName);

			Object groupObject = inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).get(groupName);
			if (groupObject != null) {
				Group group = (Group)groupObject;

				group.getInstances().put(instanceName, new Instance(instanceName, new Timestamp(System.currentTimeMillis())));
				
				inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).put(groupName, group);
				
				inMemoryServerManager.getMap(InstanceManagerMapEnum.TOPOLOGY_MAP.getType()).unlock(groupName);
			}
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[ServiceInstancesManager].initializeScheduler : error = " + e);
		}
	}
}