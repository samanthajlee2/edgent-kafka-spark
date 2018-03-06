# Tutorial: Edgent with Raspberry Pi

## Get started with Kafka + Edgent
Download: `sudo docker pull hausss/edgent-demo`

Run: `sudo docker run --net=host -it hausss/edgent-demo bash`

      note: `--net=host` lets the docker container access the computer's network (so make sure you aren't already running Zookeeper or Kafka)

Leave this terminal running and open two more terminals with ```docker exec -it <container id> bash```.

You can find the container id with ```sudo docker ps``` 

To start Zookeeper: `./kafka/bin/zookeeper-server-start.sh ./kafka/config/zookeeper.properties`

To start Kafka: `./kafka/bin/kafka-server-start.sh ./kafka/config/server.properties`

To get a text editor: `apt install nano`

### Codes and scripts to run them 
`cd edgent`

1. `HelloEdgent.java` run with `./run-helloEdgent.sh`
2. `TempSensorApp.java` run with `./run-tempSensorApp.sh`
3. `TempSensorPubApp.java` run with `./run-tempSensorPubApp.sh`


#### Details:
1. is a simple Hello Edgent program
2. is an application that polls from a simulated temperature generator
3. takes 2. and publishes the temperatures to Kafka

This project includes a maven wrapper script to eliminate the need to
manually download and install maven.


#### Working with Edgent
Edit your edgent java files which are located in ```/lab/edgent/src/main/java/com/mycompany/app```

Then (back in the edgent directory) to build the jar file run:
```sh
./mvnw clean package
```

This will package up your code and place it into the uber-jar

## Edgent-Kafka Interface

`KafkaClient.java` defines three topics.

`OPT_TOPIC = kafkaTempsTopic`
`OPT_TOPIC_2 = kafkaHighTempTopic`
`OPT_TOPIC_3 = kafkaAverageTopic`

To publish to one of these three topics, uncomment the following lines of code in `TempSensorPubApp.java`:

```java
Map<String,Object> config2 = newConfig();
        KafkaProducer kafka2 = new KafkaProducer(topology, () -> config2);

        kafka2.publish(YOUR_STREAM_HERE, options.get(OPT_TOPIC_2));
```

Replace `YOUR_STREAM_HERE` with the stream object you want to hook up to Kafka.

Kafka should automatically create topics for you. If it does not, however, you can create a topic with the following command:

```sh
./kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 -replication-factor 1 --partitions 1 --topic TOPIC_NAME
```

## Running Spark Streaming

Open a terminal with ```docker exec -it <container id> bash```.

To start Spark Streaming:

```sh
./spark/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.3.0 ./spark/tempSummary.py
```

This script will bin received temperatures into one of three categories: `LOW`, `HIGH`, or `FINE`, and display the count of each.

To access the other two Kafka topics, you can run the following commands:

`kafkaHighTempTopic`:

```sh
./spark/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.3.0 ./spark/highestDisplay.py
```

`kafkaAverageTopic`:

```sh
./spark/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.3.0 ./spark/averageDisplay.py
```

These will simply print out data as it arrives. They will not transform the data any further.

## Running the application on Raspberry Pi's

The Raspberry Pi's are living on their own wireless network.

You will need to connect to the lab network to interact with the Pi and to copy data over.

You will need to connect to the normal wireless network **the first time you build your jar** - Maven will need to acquire its dependencies. You may also need to reconnect if Maven decides it wants to download them again.

### Part One

Connect to your Raspberry Pi. They are named A through G, and can be found at IP addresses `192.168.0.201, 192.168.0.202, ..., 192.168.0.207`

The password to the pi is `edgecomp`

Run these two shell commands immediately:

`export ZOOKEEPER_SERVER=192.168.0.100:2181`

`export BOOTSTRAP_SERVER=192.168.0.100:9092`

The application will use these environemnt variables to locate the central Kafka instance.

### Part Two

The Pis are preloaded with an Edgent jar.

cd to `edgent`

Execute Edgent with `./run-tempSensorPubApp.sh`

### Part Three

Develop locally like before. (build a new jar.)

Do this step from inside `/lab/edgent` in your Docker container.

Then you can copy the generated `target/*-uber.jar` to the edge device and then run it with: `scp target/*-uber.jar pi@192.168.0.20X:~/edgent/target` where `X` is the last digit of your pi's IP address.


## Cleanup Docker Containers
Delete all containers: ```docker rm $(docker ps -a -q)```

Delete all images: ```docker rmi $(docker images -q)```


## Helpful Docker tips
Copy a file to docker with ```sudo docker cp <local file> <container id>:<container filepath>```
   
You may or may not need to run docker commands as root. 
