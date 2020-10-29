SET @discovery.ip 			= '127.0.0.1';
SET @discovery.port 			= '19771';

SET @apigateway.ip	 		= '127.0.0.1';
SET @apigateway.port 		= '19571';

SET @gateway.port 			= '19471';

SET @service.port 			= '19371';

SET @db				= '127.0.0.1:3306' ;
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
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.certificate.issuer', 'micro-iot');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.certificate.subject', 'micro-iot-thing');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.certificate.duration.years', '10');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.certificate.alarm.duration.days', '7');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.certificate.signatureAlgorithm', 'SHA256withRSA');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.privateKey.algorithm', 'RSA');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.privateKey.length', '2048');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.file.location', '/home/{admin}/micro-iot/ca');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.file.certificate.name', 'CaCertificate.pem');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.ca.file.privateKey.name', 'CaPrivateKey.pem');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.memCluster.backupCount', '3');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.memCluster.readBackup', 'true');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-gateway', 'default', 'master', 'application.caManager.caCertificateManager.memCluster.lockTimeout.ms', '1000');


insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'server.port', @service.port);
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'eureka.client.serviceUrl.defaultZone',  concat('http://', @discovery.ip, ':', @discovery.port, '/eureka/'));
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'instance.topologyMap.backupCount', '3');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'serviceInterface.thing.thingManagerController.regExp.createThing.thing', '[a-zA-Z0-9:_-]{1,128}+');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.key', '[a-zA-Z0-9_.,@/:#!-]+{1,128}');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'serviceInterface.thing.thingManagerController.regExp.createThing.attributePayload.attributes.value', '[a-zA-Z0-9_.,@/:#-]*{1,800}');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'instance.instanceManager.healthCheckBatch', '*/3 * * * * ?');
insert into properties (application, profile, label, p_key, value) values ('micro-iot-service', 'default', 'master', 'instance.instanceManager.healthCheckMissTermByMs', '10000');








