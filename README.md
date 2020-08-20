# micro-iot-service

## 1. Concept
<div>
  <img src="https://user-images.githubusercontent.com/24906833/90308734-04745580-df1d-11ea-8dfb-e1a8bbc9a535.png">
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
> > 1) used by Config-Service
> > 2) used by API-GW-Service
> > 3) used by IoT-Service

> > If you don't want to use default account, chage account of datasource in bootstrap of Config-Service after 3.1.2.
> > 
<pre>
<code>
// use MySQL Client
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





