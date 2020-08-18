readme.md

## Metrics For Kafka

### kafka_server_replicamanager_leadercount

This metric is used to show the number of active brokers in cluster.

### kafka_controller_kafkacontroller_activecontrollercount

The sum of ActiveControllerCount across all of your brokers should always equal one, and you should alert on any other value that lasts for longer than one second.

### kafka_controller_controllerstats_uncleanleaderelectionspersec

Unclean leader elections occur when there is no qualified partition leader among Kafka brokers.You should alert on this metric, as it signals data loss.

### kafka_server_replicamanager_partitioncount
### kafka_server_replicamanager_underreplicatedpartitions

In a healthy cluster, the number of in sync replicas (ISRs) should be exactly equal to the total number of replicas.

### kafka_controller_kafkacontroller_offlinepartitionscount

This metric reports the number of partitions without an active leader. 

### kafka_server_brokertopicmetrics_messagesin_total

Aggregate incoming message rate per second

### kafka_server_brokertopicmetrics_bytesout_total
### kafka_server_brokertopicmetrics_bytesin_total

In a healthy cluster, the number of in sync replicas (ISRs) should be exactly equal to the total number of replicas. 

## Metrics For Zookeeper

### zookeeper_quorumsize
### zookeeper_numaliveconnections
### zookeeper_inmemorydatatree_watchcount
### zookeeper_inmemorydatatree_nodecount
### zookeeper_outstandingrequests
### zookeeper_minrequestlatency
### zookeeper_avgrequestlatency
### zookeeper_maxrequestlatency
