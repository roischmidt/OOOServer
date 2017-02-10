package server

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor
import com.typesafe.config.Config
import akka.http.scaladsl.server.Directives._
/**
  * Created by rois on 10/02/2017.
  */
trait Controller {
    
    implicit val system: ActorSystem
    
    implicit def executor: ExecutionContextExecutor
    
    implicit val materializer: Materializer
    
    def config: Config
    
    val logger: LoggingAdapter
    
    val routes = {
        logRequestResult("http-helthCheck") {
            pathPrefix("healthCheck") {
                get {
                    complete {
                        "alive"
                    }
                }
            }
        }
    
    }
}
