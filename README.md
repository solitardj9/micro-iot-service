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

##### 3.1.2. Make Account for Micro-IoT Service
> > 1) used by Config-Service
> > 2) used by API-GW-Service
> > 3) used by IoT-Service

> > If you don't want to use default account, chage account of datasource in bootstrap of Config-Service after 3.1.2.

##### 3.1.2. Make Account for Micro-IoT Service



