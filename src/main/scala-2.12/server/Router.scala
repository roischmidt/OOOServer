package server

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.typesafe.config.Config
import rest.Controller

import scala.concurrent.ExecutionContextExecutor
/**
  * Created by rois on 10/02/2017.
  */
trait Router {
    
    implicit val system: ActorSystem
    
    implicit def executor: ExecutionContextExecutor
    
    implicit val materializer: Materializer
    
    def config: Config
    
    val logger: LoggingAdapter
    
    
    val routes = {
        logRequestResult("http-helthCheck") {
            path("healthCheck") {
                get {
                    complete {
                        Controller.handleHealthCheck
                    }
                }
            }
        }
    
        logRequestResult("http-login") {
            path("login") {
                (post & entity(as[String])) { loginRequest => {
                    complete {
                       Controller.handleLogin(loginRequest)
                    }
                }
                }
            }
        }
    
    }
}
