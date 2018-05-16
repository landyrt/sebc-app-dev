#!/bin/sh

export SPARK_DIST_CLASSPATH=$(hadoop classpath)

SPARK_CLASS=com.cloudera.sdk.SparkStreamExample
SPARK_BUILD_DIR=/home/landy/sparkstreaming
SPARK_JAR=sparkstreamexample_2.11-1.0.jar
HOST=localhost
PORT=41415

DEPLOY_MODE=client
MASTER=yarn

spark2-submit --deploy-mode ${DEPLOY_MODE} --master ${MASTER} --executor-memory 1G --num-executors 4 --executor-cores 2 --class ${SPARK_CLASS} ${SPARK_BUILD_DIR}/${SPARK_JAR} ${HOST} ${PORT}

