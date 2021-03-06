package com.cloudera.sdk

import org.apache.spark.{SparkConf, SparkContext }
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD

object SparkBatchExample {
    def main(args: Array[String]): Unit = {
        if (args.length < 1) {
            System.err.println("Usage: SparkBatchExample <path>")
            System.exit(1)
        }

        val path = args(0)
        val conf = new SparkConf()
            .setAppName("Spark Batch Example")
            //.setMaster("local[2]")
            //.set("spark.io.compression.codec", "lz4")
        val sc = new SparkContext(conf)
 
        conf.set("spark.cleaner.ttl", "120000")

        /* READ DATA FROM HDFS, SORT BY KEY, SAVE TO HDFS */
        
        val file=sc.textFile(path)
        val lines = file.map(s => s.split(",")).map(a => (a(0),a(1)))
        val sorted = lines.repartition(1).sortByKey()
        //sorted.saveAsTextFile("/home/landy/Documents/BigData/Bootcamp2018/output")
         sorted.saveAsTextFile("/user/landy/spark/output")
        
        
    }
}

