package org.scala.datamasking

import java.util.Properties

object DataExtractorFactory {

  def getExtractor(dbType:String, properties:Properties):DataExtractor = dbType.toLowerCase match {
    case "postgres" => new PostgresExtractor()
    case "hive" => new HiveExtractor()
    case _ => throw new IllegalArgumentException(s"Unsupported DB Type: $dbType" )
  }

}
