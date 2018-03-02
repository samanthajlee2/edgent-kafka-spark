UBER_JAR=target/*-uber.jar

echo $UBER_JAR

SAMPLE_PACKAGE_BASE=com.mycompany.app
CLASS_NAME=${SAMPLE_PACKAGE_BASE}.TempSensorApp

java -cp ${UBER_JAR} "${CLASS_NAME}" "$@"
