## CONNECT 
  export KAFKA_HOME=/opt/lab/amq_streams/kafka_2.12-2.5.0.redhat-00003
  $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server 192.168.15.192:9092 --topic KAFKA_CONNECT_TEST_TOPIC

## Executar Connector
  $KAFKA_HOME/bin/connect-standalone.sh ./connect-standalone.properties ./CamelFileSourceConnector.properties

## Executar Consumer
  $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server 192.168.15.198:9092  --topic KAFKA_CONNECT_TEST_TOPIC   --from-beginning 

 Criar um arquivo na pasta configurada na propriedade  camel.source.path.directoryName do arquivo CamelFileSourceConnector.properties
    
    