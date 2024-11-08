package org.scala.datamasking

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

class HiveExtractor extends DataExtractor {
  override def extract(spark: SparkSession, config: Properties, tableName: String): DataFrame = ???
}