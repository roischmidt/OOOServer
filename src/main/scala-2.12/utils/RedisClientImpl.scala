package utils

import org.slf4j.LoggerFactory
import redis.RedisClient

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by rois on 10/02/2017.
  */
object RedisClientImpl {
    
    implicit val akkaSystem = akka.actor.ActorSystem()
    
    val logger = LoggerFactory.getLogger(getClass)
    
    val redis = RedisClient()
    
    def init = {
        val futurePong = redis.ping()
        logger.info("Ping sent!")
        futurePong.map(pong => {
            logger.info(s"Redis replied with a $pong")
        })
        Await.result(futurePong, Duration("5 seconds"))
    }
}
