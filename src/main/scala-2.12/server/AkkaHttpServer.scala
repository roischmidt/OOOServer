package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import utils.RedisClientImpl


/**
  * Created by rois on 10/02/2017.
  */
object AkkaHttpServer extends App with Router {
    
    override implicit val system = ActorSystem()
    override implicit val executor = system.dispatcher
    override implicit val materializer = ActorMaterializer()
    
    override val config = ConfigFactory.load()
    override val logger = Logging(system, getClass)
    
    Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
    
    logger.info(s"server is up and listening to port ${config.getInt("http.port")}")
    RedisClientImpl.init
}
