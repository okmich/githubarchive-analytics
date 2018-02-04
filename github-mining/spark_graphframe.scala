//spark-shell --packages graphframes:graphframes:0.5.0-spark1.6-s_2.10,mysql:mysql-connector-java:5.1.24

import org.graphframes._

import org.apache.spark.sql.types.IntegerType

val properties = new java.util.Properties()
properties.put("user", "root")
properties.put("password", "cloudera")

val userDF = sqlContext.read.jdbc("jdbc:mysql://quickstart.cloudera:3306/github", "user", properties).cache

val userNetworkDF = sqlContext.read.jdbc("jdbc:mysql://quickstart.cloudera:3306/github", "user_network", properties).select($"follower_id".as("src"), $"user_id".as("dst"), lit(1).cast(IntegerType).as("unit")).cache

val graph = GraphFrame(userDF, userNetworkDF)

//in inDegrees
graph.inDegrees.orderBy(desc("inDegree")).show

//out degree
graph.outDegrees.orderBy(desc("outDegree")).show

//to run iterative algorithm ingraphframe, checkpoint directory sshould be set on the sparkContext
sc.setCheckpointDir("/user/cloudera/spark_chckpoint_dir")

val ccDF = graph.connectedComponents.run
ccDF.write.saveAsTable("community")

//stronglyConnectedComponents

val sccDF =  graph.stronglyConnectedComponents.maxIter(20).run