package com.mycompany.app;

import org.apache.edgent.topology.TStream;
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

        TStream<String> tempReadings = topology.poll(sensor, 1, TimeUnit.MILLISECONDS);
        TStream<String> filteredReadings = tempReadings.filter(reading -> Float.parseFloat(reading) < TempSensor.LOW+20 || Float.parseFloat(reading) > TempSensor.HIGH-20);

        // Create the KafkaProducer broker connector
        Map<String,Object> config = newConfig();
        KafkaProducer kafka = new KafkaProducer(topology, () -> config);
        
        // Publish the stream to the topic.  The String tuple is the message value.
        kafka.publish(filteredReadings, options.get(OPT_TOPIC));
        
        return topology;
    }
    
    private Map<String,Object> newConfig() {
        Map<String,Object> config = new HashMap<>();
        // required kafka configuration items
        config.put("bootstrap.servers", options.get(OPT_BOOTSTRAP_SERVERS));
        return config;
    }

    /*
    public static void main(String[] args) throws Exception {
        // create a sensor
        TempSensor sensor = new TempSensor();
        // create a provider
        DirectProvider dp = new DirectProvider();
        // create a topology
        Topology topology = dp.newTopology();
        
        // build the topology
        TStream<String> tempReadings = topology.poll(sensor, 1, TimeUnit.MILLISECONDS);
        TStream<String> filteredReadings = tempReadings.filter(reading -> reading < 50 || reading > 80);
        filteredReadings.print();
        

        dp.submit(topology);
    }
    */
}
