# A container for deploying an Apache Edgent+Kafka+Spark container on the edge


## Running Edgent + Kafka + Spark Streaming
Download: `sudo docker pull hausss/edgent-demo`

Run: `sudo docker run -it hausss/edgent-demo bash`

check: `pwd` should show that you are in `/lab`

Run: `./start-stuff.sh` to start up Zookeeper and Kafka in the background via screen.


#### TODO: Leave this terminal running and open two more terminals side by side with ```docker exec -it <container id> bash```.

## Helpful Docker tips
You can find the container id with ```sudo docker ps```   
Copy a file to docker with ```sudo docker cp <local file> <container id>:<container filepath>```

Open another terminal into container: ```sudo docker exec -it <container id> bash```     
You may or may not need to run these commands as root. 

### Cleanup
Delete all containers: ```docker rm $(docker ps -a -q)```

Delete all images: ```docker rmi $(docker images -q)```

## Codes and scripts to run them 

1. `HelloEdgent.java` run with `./run-helloEdgent.sh`
2. `TempSensorApp.java` run with `./run-tempSensorApp.sh`
3. `TempSensorPubApp.java` run with `./run-tempSensorPubApp.sh`

### Details:
1. is a simple Hello Edgent program
2. is an application that polls from a simulated temperature generator
3. takes 2. and publishes the temperatures to Kafka

This project includes a maven wrapper script to eliminate the need to
manually download and install maven.

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
