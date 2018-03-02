package com.mycompany.app;

import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.TWindow;
import org.apache.edgent.topology.Topology;
import org.apache.edgent.topology.TopologyProvider;
import org.apache.edgent.connectors.kafka.KafkaProducer;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.mycompany.app.KafkaClient.OPT_BOOTSTRAP_SERVERS;
import static com.mycompany.app.KafkaClient.OPT_PUB_CNT;
import static com.mycompany.app.KafkaClient.OPT_TOPIC;
import static com.mycompany.app.KafkaClient.OPT_TOPIC_2;

import com.mycompany.app.TempSensor;

/**
 * Edgent Application template.
 */
public class TempSensorPubApp {
    private final TopologyProvider tp;
    private final Options options;

    /**
     * @param tp the TopologyProvider to use.
     * @param options
     */
    TempSensorPubApp(TopologyProvider tp, Options options) {
        this.tp = tp;
        this.options = options;
    }

    public Topology buildAppTopology() {
        Topology topology = tp.newTopology("kafkaClientPublisher");

        TempSensor sensor = new TempSensor();

        TStream<String> tempReadings = topology.poll(sensor, 100, TimeUnit.MILLISECONDS);
        TStream<String> filteredReadings = tempReadings.filter(reading -> Float.parseFloat(reading) < TempSensor.LOW+20 || Float.parseFloat(reading) > TempSensor.HIGH-20);
        TWindow<String,Integer> batchedReadings = tempReadings.last(1, TimeUnit.SECONDS, tuple -> 0);
        TStream<String> averagedReadings = batchedReadings.batch((tuples, key) -> {
          float avg = 0;
          for (String str : tuples) {
            avg += Float.parseFloat(str);
          }
          return String.valueOf(avg / tuples.size());
        });



        // Create the KafkaProducer broker connector
        Map<String,Object> config = newConfig();
        KafkaProducer kafka = new KafkaProducer(topology, () -> config);

        // Publish the stream to the topic.  The String tuple is the message value.
        kafka.publish(filteredReadings, options.get(OPT_TOPIC));

        Map<String,Object> config2 = newConfig();
        KafkaProducer kafka2 = new KafkaProducer(topology, () -> config2);

        kafka2.publish(averagedReadings, options.get(OPT_TOPIC_2));

        return topology;
    }

    private Map<String,Object> newConfig() {
        Map<String,Object> config = new HashMap<>();
        // required kafka configuration items
        config.put("bootstrap.servers", options.get(OPT_BOOTSTRAP_SERVERS));
        return config;
    }
}
