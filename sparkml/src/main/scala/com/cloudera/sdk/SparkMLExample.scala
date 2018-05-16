package com.cloudera.sdk

import org.apache.spark.{SparkConf, SparkContext }
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession



object SparkMLExample {
    def main(args: Array[String]): Unit = {
        if (args.length < 2) {
            System.err.println("Usage: SparkMLExample <input> <iterations>")
            System.exit(1)
        }

        val path = args(0)
        val iters = args(1).toInt
        val conf = new SparkConf()
            .setAppName("Spark ML Example")
            //.setMaster("local[2]")
            //.set("spark.io.compression.codec", "lz4")
        val sc = new SparkContext(conf)

        conf.set("spark.cleaner.ttl", "120000")
//      conf.set("spark.driver.allowMultipleContexts", "true")

        val spark = SparkSession.builder()
            .config(conf)
            .getOrCreate()

        Logger.getRootLogger.setLevel(Level.WARN)

        val training = spark.read.format("libsvm").load(path)

        /* ADD LINEAR REGRESSION FROM SPARKML AND PRINT RESULTS */
        
        val lr = new LinearRegression()
            .setMaxIter(iters)
            .setRegParam(0.3)
            .setElasticNetParam(0.8)
            
        val lrModel=lr.fit(training)
        println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
        
        println(s"predicted_house_price = ${lrModel.intercept} + ${lrModel.coefficients(0)} x num_of_rooms + ${lrModel.coefficients(1)} x num_of_baths + ${lrModel.coefficients(2)} x size_of_house + ${lrModel.coefficients(3)} x one_if_pool_zero_otherwise" )

        
        
    }
}
