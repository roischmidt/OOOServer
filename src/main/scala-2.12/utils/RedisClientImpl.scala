package utils

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.util.Timeout
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import service.LoginService.getClass

import scala.concurrent.duration._
import scala.language.postfixOps
/**
  * Created by rois on 10/02/2017.
  */
object RedisClientImpl {
    
    val logger = LoggerFactory.getLogger(getClass)
    
    final val expired = TimeUnit.SECONDS.convert(5, TimeUnit.MINUTES).toInt // session expires after 5 minutes of inactivity
    
    // Akka setup
    implicit val system = ActorSystem("redis-client")
    implicit val executionContext = system.dispatcher
    implicit val timeout = Timeout(5 seconds)
    
    // Redis client setup
    val config = ConfigFactory.load()
    var client  = new RedisClient(config.getString("redis.interface"), config.getInt("redis.port"))
    
    def init : Boolean = {
        logger.info("pinging redis server")
        client.ping.exists {
            case res =>
                logger.info(s"got pong response from redis: $res")
                true
        }
    }
    
    // clear all DB
    def clearAll() : Boolean = {
        client.flushdb
    }
    
    // store CacheData object
    def store(key: String, value: String): Boolean =
        storeEx(key, expired, value)
    
    // store CacheData object with provided expiration
    def storeEx(key: String, expire: Int, value: String): Boolean =
        client.setex(key, expire, value)
    
    // renew expiration for a specific key
    def resume(key: String, expiration: Int = expired): Boolean =
        client.expire(key, expiration)
    
    // check if key exists
    def exists(key: String): Boolean =
        client.exists(key)
    
    // remove key
    def remove(key: String): Boolean =
        client.del(key).exists(_ > 0)
}
