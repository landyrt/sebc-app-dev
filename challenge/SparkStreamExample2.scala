package com.cloudera.sdk

import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.log4j.{Level, Logger}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.io.Text
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.HTable



object SparkStreamExample2 {
    lazy val logger = Logger.getLogger(this.getClass.getName)

    def main(args: Array[String]): Unit = {
        if (args.length < 2) {
            System.err.println("Usage: SparkStreamExample <host> <port>")
            System.exit(1)
        }

        val host = args(0)
        val port = args(1).toInt
        val stopWords = List("a", "is", "the", "this")
        
        /** Hbase connection **/
        val tableName = "wordcount_landy"
        val hbaseMaster = "http://35.171.159.169:60010"
        val columnFamilyName = "wordcount"
        val columnNameWord = "word"
        val columnNameCount = "count"

        /** For generate a random number between [0,r] **/
        val r = scala.util.Random;


        val conf = new SparkConf()
            .setAppName("Spark Stream Example")
//          .setMaster("local[2]")
            .set("spark.io.compression.codec", "lz4")

        val sc = new SparkContext(conf)

        conf.set("spark.cleaner.ttl", "120000")
        conf.set("spark.driver.allowMultipleContexts", "true")

        val spark = SparkSession
            .builder()
            .config(conf)
            .getOrCreate()

        Logger.getRootLogger.setLevel(Level.WARN)

        val ssc = new StreamingContext(sc, Seconds(5))

        /* READ STREAM FROM FLUME AND PRINT COUNTS */
        
      val flumeStream = FlumeUtils.createStream(ssc,host,port)
      val lines = flumeStream.map {record => {(new String(record.event.getBody().array()))}}
      val words = lines.flatMap(line => line.split(" "))
      val counts = words.map(word => (word, 1)).reduceByKey(_+_) .filter { case (w, n) => !stopWords.exists(w.equals) == true }
 
      counts.foreachRDD {
      rdd =>
        /** Save word count result in hbase **/
        rdd.foreachPartition {
        
          conn =>
          
               
            
          val hbaseConf = HBaseConfiguration.create()
               hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
          val table = new HTable(hbaseConf, tableName)


        conn.foreach {
          case (word, count) =>
            var key = word + "-" + count + "-" + r.nextInt(10)
            println(">>> key:" + key)
            
            
            val put = new Put(key.getBytes)
            put.addColumn(columnFamilyName.getBytes, columnNameWord.getBytes, word.getBytes)
            put.addColumn(columnFamilyName.getBytes, columnNameCount.getBytes, Bytes.toBytes(count))

            
            table.put(put);
            table.flushCommits()
        }
        }
    }

        
      counts.print()
      // Start Spark Streaming Session
        ssc.start
        ssc.awaitTermination()

        /* DON'T FORGET TO START THE STREAM SESSION */
    }
}
