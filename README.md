# Tutorial: Edgent with Raspberry Pi

## Running Edgent + Kafka
Download: `sudo docker pull hausss/edgent-demo`

Run: `sudo docker run --net=host -it hausss/edgent-demo bash`

      note: `--net=host` lets the docker container access the computer's network (so make sure you aren't already running Zookeeper or Kafka)

check: `pwd` should show that you are in `/lab`

You can find the container id with ```sudo docker ps``` 

Leave this terminal running and open more terminals as needed with ```docker exec -it <container id> bash```.

To start Zookeeper and Kafka in the background: `./start-zookeeper-kafka.sh`

(Alt) To start Zookeeper: `./kafka/bin/zookeeper-server-start.sh ./kafka/config/zookeeper.properties`

(Alt) To start Kafka: `./kafka/bin/kafka-server-start.sh ./kafka/config/server.properties`


## Codes and scripts to run them 
`cd edgent`

1. `HelloEdgent.java` run with `./run-helloEdgent.sh`
2. `TempSensorApp.java` run with `./run-tempSensorApp.sh`
3. `TempSensorPubApp.java` run with `./run-tempSensorPubApp.sh`


### Details:
1. is a simple Hello Edgent program
2. is an application that polls from a simulated temperature generator
3. takes 2. and publishes the temperatures to Kafka

This project includes a maven wrapper script to eliminate the need to
manually download and install maven.


## Running Spark Streaming

Open a terminal with ```docker exec -it <container id> bash```.

To start Spark Streaming: `./spark/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.3.0 tempSummary.py`

#### Building the project
```sh
./mvnw clean package  # add -Pplatform-java7 or -Pplatform-android as needed
```

Note: Do NOT override the value via
`./mvnw ... -Dedgent.runtime.version=<the-version>`.
The build will not behave as desired.

#### Running the application
You can copy `<script>.sh` and the generated `target/*-uber.jar` to the 
edge device and then run it
```sh
./<script>.sh
```


### Cleanup
Delete all containers: ```docker rm $(docker ps -a -q)```

Delete all images: ```docker rmi $(docker images -q)```


## Helpful Docker tips
Copy a file to docker with ```sudo docker cp <local file> <container id>:<container filepath>```
   
You may or may not need to run docker commands as root. 
