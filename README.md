There are 3 examples in this project: 

1. HelloEdgent.java run with ./run-helloEdgent.sh
2. TempSensorApp.java run with ./run-tempSensorApp.sh
3. TempSensorPubApp.java run with ./run-tempSensorPubApp.sh

1. is a simple Hello Edgent program
2. is an application that polls from a simulated temperature generator
3. takes 2. and publishes the temperatures to Kafka

This project includes a maven wrapper script to eliminate the need to
manually download and install maven.

# Building the project
```sh
./mvnw clean package  # add -Pplatform-java7 or -Pplatform-android as needed
```

Note: Do NOT override the value via
`./mvnw ... -Dedgent.runtime.version=<the-version>`.
The build will not behave as desired.

# Running the application
You can copy `app-run.sh` and the generated `target/*-uber.jar` to the 
edge device and then run it
```sh
./app-run.sh
```