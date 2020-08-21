package com.redhat.consulting.workshop.kafka.producer;

import java.io.File;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import io.apicurio.registry.utils.serde.AbstractKafkaSerDe;
import io.apicurio.registry.utils.serde.AbstractKafkaSerializer;
import io.apicurio.registry.utils.serde.AvroKafkaSerializer;
import io.apicurio.registry.utils.serde.strategy.FindLatestIdStrategy;
//import io.confluent.kafka.serializers.KafkaAvroSerializer;

/**
 * 
 * @author Rogerio L Santos
 *
 */
public class NewOrderMain {

	private static final String TOPIC = "transactions";
	public static void main(String args[]) throws Exception {
			  
		
		 final Properties props = new Properties();
	        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.15.192:9092");
	        props.put(ProducerConfig.ACKS_CONFIG, "all");
	        props.put(ProducerConfig.RETRIES_CONFIG, 0);
	        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);	      
	        props.put(AbstractKafkaSerDe.REGISTRY_URL_CONFIG_PARAM, "https://service-registry-kafkaproject.apps.rls-5bc2.open.redhat.com/artifacts");	         
	        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getName()); 
	        props.put(AbstractKafkaSerializer.REGISTRY_GLOBAL_ID_STRATEGY_CONFIG_PARAM, FindLatestIdStrategy.class.getName());
	        
	        
	        Schema schema = new Schema.Parser().parse(
	                new File(NewOrderMain.class.getClassLoader().getResource("person-scheme.avsc").getFile()));
	        
	     
			
		}
	

}
