name := "SparkStreamExample"
version := "1.0"
scalaVersion := "2.11.0"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
// ADD CLOUDERA REPO AND DEPENDENCIES FOR SPARK APPS
// ALSO ADD A MERGE STRATEGY & BUILD A FAT JAR HERE


resolvers +=  "cloudera-repos" at "http://repository.cloudera.com/artifactory/cloudera-repos/"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.3.0.cloudera2" % "provided")

libraryDependencies ++= Seq("org.apache.spark" %% "spark-sql" % "2.3.0.cloudera2" % "provided")

libraryDependencies ++= Seq("org.apache.spark" %% "spark-streaming-flume" % "2.3.0.cloudera2" % "provided")

libraryDependencies ++= Seq("org.apache.spark" %% "spark-streaming" % "2.3.0.cloudera2" % "provided")
