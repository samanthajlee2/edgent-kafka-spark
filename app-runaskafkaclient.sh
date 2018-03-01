#!/bin/bash


DIR=.

UBER_JAR=`echo ${DIR}/target/my-app-*-uber.jar`

# Runs the Kafka Publisher or Subscriber Client
#
# ./runkafkaclient.sh pub
# ./runkafkaclient.sh sub
# ./runkafkaclient.sh -h

export CLASSPATH=${UBER_JAR}

java com.mycompany.app.KafkaClient $@
