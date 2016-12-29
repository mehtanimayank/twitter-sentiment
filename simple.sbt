name := "Simple Project"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(

        "org.apache.spark" %% "spark-core" % "2.0.1",
        "org.apache.spark" %% "spark-streaming" % "2.0.1",
	"databricks" % "spark-corenlp" % "0.2.0-s_2.10",
	"org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1",
	"org.twitter4j" % "twitter4j-core" % "4.0.4",
	"org.twitter4j" % "twitter4j-stream" % "4.0.4",
	"org.apache.spark" %% "spark-sql" % "2.0.1",
	"com.google.code.gson" % "gson" % "2.7",
        "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
        "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"

)


