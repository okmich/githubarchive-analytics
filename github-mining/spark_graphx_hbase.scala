// spark-shell --packages it.nerdammer.bigdata:spark-hbase-connector_2.10:1.0.3 --exclude-packages org.slf4j:slf4j-api,javax.servlet:javax.servlet-api,org.mortbay.jetty:jetty,org.mortbay.jetty:servlet-api-2.5,org.codehaus.jackson:jackson-core-asl,commons-codec:commons-codec --conf spark.hbase.host=quickstart.cloudera --conf hbase.zookeeper.quorum=quickstart.cloudera --conf hbase.zookeeper.property.clientPort=2181

import it.nerdammer.spark.hbase._

import org.apache.spark.rdd.RDD
import org.apache.spark.graphx._


val userRDD = sc.hbaseTable[(Long, String, String, String, String, String, String, Long)]("github:user").
    select("id", "login", "name", "location", "company", "blog", "joined", "ts").
    inColumnFamily("u").cache

val userNetworkRDD = sc.hbaseTable[(Long, Long, Long)]("github:user_network").
    select("p", "f", "ts").
    inColumnFamily("main").
    cache

case class User(id: Long, login: String, name: String, location: String, company: String, blog: String, joined: String, ts: Long) extends java.io.Serializable

def rowToUser(r: (Long, String, String, String, String, String, String, Long)) : User = User(r._1, r._2, r._3, r._4, r._5, r._6, r._7, r._8)

val userVertexRDD : RDD[(VertexId, User)] = userRDD.map(rowToUser).map((u:User) => (u.id, u))


val userEdgeRDD :RDD[Edge[Int]] = userNetworkRDD.map((r: (Long, Long, Long)) => new Edge(r._2, r._1, 1))

val graph = Graph(userVertexRDD, userEdgeRDD).cache