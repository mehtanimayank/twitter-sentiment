/* SimpleApp.scala */
import com.databricks.spark.corenlp.functions._
import org.apache.spark.sql.functions._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession 
import org.apache.spark.SparkConf
import org.apache.spark.streaming
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.twitter
import org.apache.spark.streaming.twitter.TwitterUtils


/* these imports are required to run the mysenti method  */
import java.util.Properties
import scala.collection.JavaConverters._
import edu.stanford.nlp.ling.{CoreAnnotations, CoreLabel}
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{CleanXmlAnnotator, StanfordCoreNLP}
import edu.stanford.nlp.pipeline.CoreNLPProtos.Sentiment
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.simple.{Document, Sentence}
import edu.stanford.nlp.util.Quadruple
import org.apache.spark.sql.functions.udf


object SimpleApp {

	var sentimentPipeline: StanfordCoreNLP = _
	def mysenti(sentence: String): (String, Int) = {

        	if (sentimentPipeline == null) {

                	val props = new Properties()
                	props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
                	sentimentPipeline = new StanfordCoreNLP(props)
              	}

                val annotation = sentimentPipeline.process(sentence)
                val tree = annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).asScala.head.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
                (sentence,RNNCoreAnnotations.getPredictedClass(tree))
	}
	
        def main(args: Array[String]) {

		val config = new SparkConf().setAppName("twitter-stream-sentiment")
                val sc = new SparkContext(config)
		sc.setLogLevel("WARN")
                val ssc = new StreamingContext(sc, Seconds(5))

                System.setProperty("twitter4j.oauth.consumerKey","po2YFywYNEkt0T02dB0Y7FLmK")
                System.setProperty("twitter4j.oauth.consumerSecret","sl6iBv3Jcl59QXlpqXHdAw37BLagK2uFWoAe2WzbN3AH0dt0tS")
                System.setProperty("twitter4j.oauth.accessToken","811565823127003137-oUMnVQOxJ0QreE4Gg4VDpCmMRDUYujh")
                System.setProperty("twitter4j.oauth.accessTokenSecret","bufSJzBvgm1hfmOrcKocU7CNWjORzpped12RhudfNdq0c")
		
		val sqlContext = new org.apache.spark.sql.SQLContext(sc) // this is needed for the imports
		import sqlContext.implicits._
		
		val warehouseLocation = "/apps/hive/warehouse/spark-warehouse"
		val spark = SparkSession
		   .builder()
		   .appName("SimpleApp")
		   .config("spark.sql.warehouse.dir", warehouseLocation)
	   .getOrCreate()
		
		val stream = TwitterUtils.createStream(ssc, None)
		val text_tweet = stream.map(status => status.getText()) //DStream returned
		val text_tweet_senti = text_tweet.map(mysenti)

		text_tweet_senti.print()		

		ssc.start()
		ssc.awaitTermination()
			
      }

}



