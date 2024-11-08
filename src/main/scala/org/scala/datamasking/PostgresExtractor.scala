package org.scala.datamasking
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

class PostgresExtractor extends DataExtractor {

  override def extract(spark: SparkSession, config: Properties, tableName: String): DataFrame = {
    val jdbcUrl = config.getProperty("jdbc.url")
    val connectionProperties = new Properties()
    connectionProperties.put("user",config.getProperty("jdbc.user"))
    connectionProperties.put("password",config.getProperty("jdbc.password"))
    connectionProperties.put("numPartitions",config.getProperty("jdbc.numPartitions"))
    connectionProperties.put("fetchsize",config.getProperty("jdbc.fetchsize"))
    val df = spark.read.jdbc(jdbcUrl,tableName,connectionProperties)
    df.show()
    df
  }
}
