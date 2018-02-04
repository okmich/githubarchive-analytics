
//spark-shell --master yarn --packages mysql:mysql-connector-java:5.1.24

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import org.apache.spark.graphx._

val properties = new java.util.Properties()
properties.put("user", "root")
properties.put("password", "cloudera")

val userDF = sqlContext.read.jdbc("jdbc:mysql://quickstart.cloudera:3306/github", "user", properties)

case class User(id: Long, login: String, name: String, location: String, company: String, blog: String, joined: String, ts: Long) extends java.io.Serializable

def rowToUser(r: Row) : User = User(r.getAs[Long](0), r.getAs[String](1), r.getAs[String](2), r.getAs[String](3), r.getAs[String](4), r.getAs[String](5), r.getAs[String](6), r.getAs[Long](7))

val userRDD : RDD[(VertexId, User)] = userDF.rdd.map(rowToUser).map((u:User) => (u.id, u))

val userNetworkDF = sqlContext.read.jdbc("jdbc:mysql://quickstart.cloudera:3306/github", "user_network", properties)

val userNetworkRDD : RDD[Edge[Int]] = userNetworkDF.rdd.map((r:Row) => new Edge(r.getAs[Long](1), r.getAs[Long](0), 1))

val graph = Graph(userRDD, userNetworkRDD).cache
//number of vertices
graph.numVertices
//number of edges
graph.numEdges

//indegress
val inDeg = graph.inDegrees
inDeg.filter(_._2 == 1).count
inDeg.sortBy(_._2 * -1).first

//outdegrees
val outDeg = graph.outDegrees
outDeg.filter(_._2 == 0)
res17.count
outDeg.filter(_._2 == 1).count
outDeg.sortBy(_._2 * -1).first

//user 4303 has the most follwers
userDF.where("id = 976175").show
//user 976175 has follows the most
userDF.where("id = 4303").show


