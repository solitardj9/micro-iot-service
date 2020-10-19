SET @discovery.ip 			= '127.0.0.1';
SET @discovery.port 			= '19771';

SET @apigateway.ip	 		= '127.0.0.1';
SET @apigateway.port 		= '19571';

SET @gateway.port 			= '19471';

SET @service.port 			= '19371';

SET @db				= '192.168.1.42:3306' ;
SET @db.id				= 'micro-iot';
SET @db.pw				= '9e60a46d06bc';


insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'management.endpoints.web.exposure.include', '*');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'management.endpoints.web.base-path', '/');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'management.endpoint.health.show-details', 'ALWAYS');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'logging.level.root', 'info');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.datasource.driver-class-name', 'org.mariadb.jdbc.Driver');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.datasource.url', concat('jdbc:mariadb://', @db, '/micro_iot'));
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.datasource.username', @db.id);
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.datasource.password', @db.pw);
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.jpa.properties.hibernate.id.new_generator_mappings', 'false');	
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.jpa.properties.hibernate.enable_lazy_load_no_trans', 'true');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.jpa.hibernate.ddl-auto', 'none');
insert into properties (application, profile, label, p_key, value) values ('application', 'default', 'master', 'spring.jpa.show-sql', 'true');


insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'server.port', @apigateway.port);
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'eureka.client.serviceUrl.defaultZone',  concat('http://', @discovery.ip, ':', @discovery.port, '/eureka/'));
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.thing.path', '/thing/**');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.thing.serviceId', 'micro-iot-service-thing');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.thing.stripPrefix', 'false');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.state.path', '/state/**');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.state.serviceId', 'micro-iot-service-state');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.state.stripPrefix', 'false');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.gateway.path', '/gateway/**');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.gateway.serviceId', 'micro-iot-gateway');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-api-gateway', 'default', 'master', 'zuul.routes.gateway.stripPrefix', 'false');


insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'server.port', @gateway.port);
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'eureka.client.serviceUrl.defaultZone',  concat('http://', @discovery.ip, ':', @discovery.port, '/eureka/'));


insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'server.port', @service.port);
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'eureka.client.serviceUrl.defaultZone',  concat('http://', @discovery.ip, ':', @discovery.port, '/eureka/'));
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'instance.topologyMap.backupCount', '3');








