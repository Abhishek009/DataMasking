package org.scala.datamasking

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

abstract class DataExtractor {
  def extract(spark: SparkSession,config:Properties,tableName:String):DataFrame
}

