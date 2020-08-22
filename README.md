# micro-iot-service

## 1. Concept
<div>
  <img src="https://user-images.githubusercontent.com/24906833/90782997-7d6b1700-e33a-11ea-87a5-3e0abad79102.png">
</div>
  
## 2. System Architecture

## 3. Build a System
### 3.1 Message Middleware (RabbitMQ)
#### Installation
<pre>
<code>
In this case, just use single node.
If you want to cluster(In local), need to install LB(HA Proxy : http://www.haproxy.org/) and VIP(Keepalived : https://www.keepalived.org/)
Then, configure RabbitMQ.conf.
</code>
</pre>

rabbitMQ.conf
SSL
Auth
> > rabbitmq-auth-backend-http

### 3.2 Im-Memory Storage (Hazelcast)
#### Installation
<pre>
<code>
In this case, just use single node.
If you want to cluster(In local), need to install LB(HA Proxy : http://www.haproxy.org/) and VIP(Keepalived : https://www.keepalived.org/)
Then, configure hazelcast.xml.
</code>
</pre>

### 3.3 Data Storage (MariaDB)
#### Installation
<pre>
<code>
In this case, just use single node.
If you want to cluster(In local), need to install LB(Max Scale : https://mariadb.com/kb/en/maxscale/) and HA(Galera Cluster : https://galeracluster.com/products/)
</code>
</pre>
##### 3.1.1. Install
> > mariadb 10.5

##### 3.1.2. Make Account for Micro-IoT Service
> > 1) used by config-service
> > 2) used by api-service
> > 3) used by iot-service

> > If you don't want to use default account, chage account of datasource in bootstrap of Config-Service. (see 3.4.1. Change Account for Database)
> > 
<pre>
<code>
// use MySQL Client
// default account
create user 'micro-iot'@'%' indentified by '9e60a46d06bc';
grant all privileges on *.* to 'micro-iot'@'%' indentified by '9e60a46d06bc' with grant option;
</code>
</pre>

##### 3.1.3. Make Database for Micro-IoT Service
<pre>
<code>
// use MySQL Client
// reconnect by user 'micro-iot'
create datbase micro_iot;
use micro_iot;
</code>
</pre>

##### 3.1.4. Make Property Table for Micro-IoT Service
<pre>
<code>
create table `properties` (
			`p_key` VARCHAR(512) NOT NULL,
			`value` VARCHAR(4096) NULL DEFAULT NULL,
			`application` VARCHAR(128) NOT NULL,
			`profile` VARCHAR(128) NOT NULL,
			`label` VARCHAR(128) NOT NULL,
			PRIMARY KEY (`p_key`, `application`, `profile`, `label`)
);
</code>
</pre>

### 3.4 Micro-IoT Service
#### Installation
<pre>
<code>
In this case, just use single node.
For the multi node case, modify bootstrap in iot-service.
> > change propety of consumer for HA(High Availability)
> > change propety of consumer and consumer group for HA(High Availability) and LB(Load Balancing)
</code>
</pre>
concept of multip node
<div>
  <img src="https://user-images.githubusercontent.com/24906833/90783213-c4590c80-e33a-11ea-8c28-2bba7619a37f.png">
</div>

##### 3.4.1. Change Account for Database
> > chage account of datasource in bootstrap of config-service

##### 3.4.2. Change Account for RabbitMQ
> > chage account of rabbitmq in data.sql of config-service

##### 3.4.3. run Micro-IoT Service

##### 3.4.4. copy cacert from Micro-IoT Service to RabbitMQ
> > copy cacert.pem file in iot-service to RabbitMQ server

##### 3.4.5. install cert/key to RabbitMQ
> > copy cert.pem and key.pem file to RabbitMQ server

##### Micro-IoT Services
###### > > GW-Service : https://github.com/solitardj9/micro-iot_gw-service
###### > > IoT-Service : https://github.com/solitardj9/micro-iot_iot-service
###### > > Discovery-Service : https://github.com/solitardj9/micro-iot_discovery-service
###### > > Config-Service : https://github.com/solitardj9/micro-iot_config-service
###### > > API-Service : https://github.com/solitardj9/micro-iot_api-service





