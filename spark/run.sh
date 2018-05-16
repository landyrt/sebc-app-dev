#!/bin/sh

SPARK_CLASS=com.cloudera.sdk.SparkBatchExample
SPARK_BUILD_DIR=/home/landy/spark
SPARK_JAR=sparkbatchexample_2.11-1.0.jar
SPARK_BASE=/user/landy/spark/input

DEPLOY_MODE=cluster
MASTER=yarn

spark-submit --deploy-mode ${DEPLOY_MODE} --master ${MASTER} --executor-memory 128M --num-executors 2 --executor-cores 1 --class ${SPARK_CLASS} ${SPARK_BUILD_DIR}/${SPARK_JAR} ${SPARK_BASE}
