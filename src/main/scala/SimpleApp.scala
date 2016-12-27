/* SimpleApp.scala */

import com.databricks.spark.corenlp.functions._
import org.apache.spark.sql.functions._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object SimpleApp {
	
        def main(args: Array[String]) {

		val sc = new SparkContext()

		val sqlContext= new org.apache.spark.sql.SQLContext(sc)
		import sqlContext.implicits._

		val warehouseLocation = "/apps/hive/warehouse/spark-warehouse"
		val spark = SparkSession
		   .builder()
		   .appName("SimpleApp")
		   .config("spark.sql.warehouse.dir", warehouseLocation)
		   .getOrCreate()
		
		val input = Seq((1, "<xml>Stanford University is located in California; It is a great university.</xml>")).toDF("id", "text")
		val output = input.select(cleanxml('text).as('doc)).select(explode(ssplit('doc)).as('sen)).select('sen, tokenize('sen).as('words), ner('sen).as('nerTags), sentiment('sen).as('sentiment))
		output.show(truncate = false)
	

	}

}




/*
import org.apache.spark.SparkContext
import org.apache.spark.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.streaming
import org.apache.spark.sql.functions._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.twitter
import org.apache.spark.streaming.twitter.TwitterUtils






*/
