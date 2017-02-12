package utils

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.util.Timeout
import com.redis.{RedisClient, RedisClientPool}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Failure

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
    var clients = new RedisClientPool(config.getString("redis.interface"), config.getInt("redis.port"))
    
    def asyncRedis[T](body: RedisClient => T): Future[T] =
        Future {
            clients.withClient {
                client => {
                    client.setConfig("notify-keyspace-events", "KEx")
                    body(client)
                }
            }
        }.andThen {
            case Failure(e) => logger.error("redis request failed", e)
            case ignore =>
        }
    
    
    def init: Future[Boolean] = {
        logger.info("pinging redis server")
        asyncRedis {
            _.ping.exists {
                case res =>
                    logger.info(s"got pong response from redis: $res")
                    true
            }
        }
    }
    
    
    // clear all DB
    def clearAll(): Future[Boolean] = {
        asyncRedis {
            _.flushdb
        }
    }

    // store CacheData object
    def store(key: String, value: String): Future[Boolean] =
        storeEx(key, expired, value)

    // store CacheData object with provided expiration
    private def storeEx(key: String, expire: Int, value: String): Future[Boolean] =
        asyncRedis {
            _.setex(key, expire, value)
        }

    // renew expiration for a specific key
    def resume(key: String, expiration: Int = expired): Future[Boolean] =
        asyncRedis {
            _.expire(key, expiration)
        }

    // check if key exists
    def exists(key: String): Future[Boolean] =
        asyncRedis {
            _.exists(key)
        }

    // remove key
    def remove(key: String): Future[Boolean] =
        asyncRedis {
            _.del(key).exists(_ > 0)
        }

    // add to set
    def addToSet(key: String, value: String): Future[Boolean] =
        asyncRedis {
            _.sadd(key, value) match {
                case Some(len) =>
                    len > 0
                case None =>
                    logger.info(s"$key is not a set")
                    false
            }
        }

    // remove from set
    def removeFromList(key: String, value: String): Future[Boolean] =
        asyncRedis {
            _.srem(key, value) match {
                case Some(len) =>
                    len > 0
                case None =>
                    logger.info(s"$key is not a set")
                    false
            }
        }

}
