package org.scala.datamasking

import org.apache.log4j.Logger

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import java.util.Properties
import scala.io.Source

object DataMasking {

  def addPrefix(df: DataFrame,column: String): DataFrame = {
    //df.schema.map(x => println(x))
    df.schema(column).dataType match {
      case StringType => df.withColumn(column, when(length(col(column)) < 6, concat(expr("repeat('X', 6 - length(" + column + "))"), col(column))).when(length(col(column)) > 28,substring(col(column),0,28)).otherwise(col(column)))
      case IntegerType => df.withColumn(column, when(length(col(column).cast(StringType)) < 6, lpad(col(column).cast(StringType), 6, "0")).when(length(col(column).cast(StringType)) > 28,substring(col(column),0,28)).otherwise(column))
      case ShortType => df.withColumn(column, when(length(col(column).cast(StringType)) < 6, rpad(col(column).cast(StringType), 6, "0")).when(length(col(column).cast(StringType)) > 28,substring(col(column),0,28)).otherwise(column))
      case _ => df
    }
  }

  def main(args: Array[String]): Unit = {

    val alphabet: Map[String, String] = Map(
      "DIGITS"-> "0123456789",
      "LETTERS"-> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ",
      "STRING"-> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~",
      "EMAIL"->"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@._"
    )

    val logger = Logger.getLogger(this.getClass)
    val spark = SparkSession.builder().appName("DataRefresh")
      .master("local").getOrCreate()

    val properties: Properties = new Properties()
      val source = Source.fromFile("src/main/resources/application.properties")
    properties.load(source.bufferedReader())
    val sourceDbType = properties.getProperty("source.db.type")
    val targetDbType = properties.getProperty("target.db.type")
    val maskingKey = "2DE79D232DF5585068CE47882AE256D6" //
    //sys.env.getOrElse("FPE_KEY", throw new IllegalArgumentException("Missing environment variable FPF_KEY")
    val tweak = "CBD09280979564" //properties.getProperty("fpe.tweak")
    logger.info(s"Source DB is $sourceDbType")
    logger.info(s"Target DB is $targetDbType")
    println(properties.getProperty("source.tables"))
    println(properties.getProperty("jdbc.url"))
    println(properties.getProperty("jdbc.user"))
    println(properties.getProperty("jdbc.password"))
    logger.info(s"Trying to connect to source db")
    try{
      val extractor = DataExtractorFactory.getExtractor(sourceDbType, properties)  //extractor.extract(spark, properties, "input_modal")
      val table = properties.getProperty("source.tables").split(",")
      table.foreach{ tableName =>
        logger.info(s"Processing table: $tableName")
        val extractedDf = extractor.extract(spark, properties, tableName)

        val columnType = extractedDf.schema.map{ x => x.name-> x.dataType }.toMap

        println(columnType)
        // Apply masking based on manifest configuration
        val maskedDf = extractedDf.columns.foldLeft(extractedDf) {
          (df, columnName) =>
            // Apply masking if required based on column metadata
          val isPii = properties.getProperty( s"column.$columnName.pii", "N") == "Y"
            if (isPii) {
              logger.info(s"Masking column $columnName")
              val dfWithColumnLengthFixed = addPrefix(df,columnName)
              dfWithColumnLengthFixed.show()
              var customAlphabet = Option(properties.getProperty(s"column.$columnName.custom_alphabet", null))
              println(s"===================${customAlphabet}")
              if (customAlphabet != null) {
                customAlphabet = columnType.get(columnName) match {
                  case Some(IntegerType) => Some(alphabet.getOrElse("DIGITS",""))
                  case Some(StringType) => Some(alphabet.getOrElse("LETTERS",""))
                  case Some(ShortType) => Some(alphabet.getOrElse("DIGITS",""))
                  case Some(TimestampType) => Some(alphabet.getOrElse("DIGITS",""))
                  case _ => Some(alphabet.getOrElse("STRING",""))
                }
              }

              dfWithColumnLengthFixed.withColumn(
                columnName,
                udf((value: String) => MaskingUtils.applyFF3Masking(maskingKey, tweak, value, customAlphabet))
                .apply(col(columnName))
              )
            }else df
        }
        maskedDf.show(false);
        logger.info(s"Table $tableName processed succesfully")
      }
    }catch {
      case e:Exception =>
        logger.error("Error occured during ETL job ",e)
    }

  }
}

