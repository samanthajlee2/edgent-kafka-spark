package com.mycompany.app;

import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.connectors.kafka.KafkaProducer;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.mycompany.app.KafkaClient.OPT_BOOTSTRAP_SERVERS;
import static com.mycompany.app.KafkaClient.OPT_PUB_CNT;
import static com.mycompany.app.KafkaClient.OPT_TOPIC;


/**
 * Edgent Application template.
 */
public class TempSensorApp {
    
    public static void main(String[] args) throws Exception {
        // create a sensor
        TempSensor sensor = new TempSensor();
        // create a provider
        DirectProvider dp = new DirectProvider();
        // create a topology
        Topology topology = dp.newTopology();
        
        // build the topology
        TStream<String> tempReadings = topology.poll(sensor, 1, TimeUnit.MILLISECONDS);
        // TStream<String> filteredReadings = tempReadings.filter(reading -> reading < 50 || reading > 80);
        tempReadings.print();
        

        dp.submit(topology);
    }
    
}
