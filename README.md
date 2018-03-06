# Tutorial: Edgent with Raspberry Pi

## Get started with Kafka + Edgent
Download: `sudo docker pull hausss/edgent-demo`

Run: `sudo docker run --net=host -it hausss/edgent-demo bash`

      note: `--net=host` lets the docker container access the computer's network (so make sure you aren't already running Zookeeper or Kafka)

Leave this terminal running and open two more terminals with ```docker exec -it <container id> bash```.

You can find the container id with ```sudo docker ps``` 

To start Zookeeper: `./kafka/bin/zookeeper-server-start.sh ./kafka/config/zookeeper.properties`

To start Kafka: `./kafka/bin/kafka-server-start.sh ./kafka/config/server.properties`


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

Then to build the jar file run:
```sh
./mvnw clean package  # add -Pplatform-java7 or -Pplatform-android as needed
```

## Running Spark Streaming

Open a terminal with ```docker exec -it <container id> bash```.

To start Spark Streaming: `./spark/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.3.0 ./spark/tempSummary.py`


## Running the application on Raspberry Pi's
### Part One

`export ZOOKEEPER_SERVER=192.168.0.100:2181`

`export BOOTSTRAP_SERVER=192.168.0.100:9092`

### Part Two
Develop locally like before. (build a new jar.)

Then you can copy the generated `target/*-uber.jar` to the edge device and then run it with: `scp target/*-uber.jar pi@192.168.0.20X:~/edgent/target` where `X` is the last digit of your pi's IP address.


## Cleanup Docker Containers
Delete all containers: ```docker rm $(docker ps -a -q)```

Delete all images: ```docker rmi $(docker images -q)```


## Helpful Docker tips
Copy a file to docker with ```sudo docker cp <local file> <container id>:<container filepath>```
   
You may or may not need to run docker commands as root. 
