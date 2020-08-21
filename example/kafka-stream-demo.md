
## KAFKA MSTREAMS DEMO
  

### JMX EXPORTER  

    -javaagent:/home/rosantos/workspace-ansible/files/jmx_prometheus_javaagent-0.13.0.jar=7090:/home/rosantos/workspace-ansible/files/kstream.yml  

### create input topic with two partitions

    /opt/kafka/bin/kafka-topics.sh --create --zookeeper <ZOOKEEPER HOST>:2181 --replication-factor 1 --partitions 2 --topic streams-plaintext-input
  

### create output topic

    /opt/kafka/bin/kafka-topics.sh --create --zookeeper <ZOOKEEPER HOST>:2181 --replication-factor 1 --partitions 2 --topic streams-wordcount-output  

### consumer

    /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server <HOST>:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer


### producer

/opt/kafka/bin/kafka-console-producer.sh --broker-list <HOST>:9092 --topic streams-plaintext-input  

### Run STreams  

    java -jar WordCount-1.1.jar <HOST>:9092

