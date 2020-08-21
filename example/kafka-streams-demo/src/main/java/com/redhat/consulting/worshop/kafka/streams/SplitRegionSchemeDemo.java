package com.redhat.consulting.worshop.kafka.streams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import com.redhat.consulting.worshop.kafka.model.Person;
import com.redhat.consulting.worshop.kafka.wrapper.JsonPOJODeserializer;
import com.redhat.consulting.worshop.kafka.wrapper.JsonPOJOSerializer;
/**
 * Get a JSON from topic and process it. 
 * @author Rogério L Santos
 *
 */
public class SplitRegionSchemeDemo {
	
	public static final String INPUT_TOPIC = "lab3_input";
	public static final String OUTPUT_TOPIC = "lab3_output";

	public static void main(String[] args) {

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "json-java");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.15.192:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		// Serialize and deserialize Person.class
		Map<String, Object> serdeProps = new HashMap<>();
		final Serializer<Person> personSerializer = new JsonPOJOSerializer<>();
		serdeProps.put("JsonPOJOClass", Person.class);
		personSerializer.configure(serdeProps, false);

		final Deserializer<Person> personDeserialize = new JsonPOJODeserializer<>();
		serdeProps.put("JsonPOJOClass", Person.class);
		personDeserialize.configure(serdeProps, false);

		final Serde<Person> personSerde = Serdes.serdeFrom(personSerializer, personDeserialize);

		// we disable the cache to demonstrate all the "steps" involved in the
		// transformation - not recommended in prod
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");


		StreamsBuilder builder = new StreamsBuilder();

		// Step 1: Receive o JSON
		KStream<String, Person> inputTopic = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), personSerde));

		//Get the JSON, and process the country and split it in regions
		KStream<String, String> splitRegion = inputTopic.filter((key, value) -> isDomestic(value))
				.selectKey((key, value) -> value.getName().toUpperCase())
				.mapValues(value -> mapRegion(value))
				.filter((key, value) -> Arrays.asList("LATAM", "NORTH AMERICA", "EMEA", "I DONT KNOW").contains(value));
		;		
		splitRegion.to("lab3_tmp_topic_a");

		// step 2 - we read that topic as a KTable so that updates are read correctly
		KTable<String, String> splitRegionTable = builder.table("lab3_tmp_topic_a");

		Serde<String> stringSerde = Serdes.String();
		Serde<Long> longSerde = Serdes.Long();

		// step 3 - we count the occurences of colours
		KTable<String, Long> outputTable = splitRegionTable
				.groupBy((a, b) -> new KeyValue<>(b, b))  // We group by regions within the KTable
				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("CountCountry") //Count by Regions
						.withKeySerde(stringSerde).withValueSerde(longSerde)); //Convert format

		outputTable.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

		
		// Step2:
		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		// only do this in dev - not in prod
		streams.cleanUp();
		streams.start();

		// print the topology
		streams.localThreadsMetadata().forEach(data -> System.out.println(data));

		// shutdown hook to correctly close the streams application
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}

	private static Boolean isDomestic(Person value) {

		System.out.print("*******************" + value.getCountry());

		return true;
	}

	private static String mapRegion(Person value) {

		if (value.getCountry().equalsIgnoreCase("BR")) {

			return "LATAM";

		} else if (value.getCountry().equalsIgnoreCase("US")) {

			return "NORTH AMERICA";

		} else if (value.getCountry().equalsIgnoreCase("IL")) {

			return "EMEA";
		
		} else {
			
			return "I DONT KNOW";
			
		}

	}

}
