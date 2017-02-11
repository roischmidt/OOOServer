package rest

import akka.http.scaladsl.model.{HttpEntity, _}
import api.Error
import service.{LoginService, Service}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by rois on 10/02/2017.
  */
object Controller {
    
    private def process[REQ,RES](service : Service[REQ,RES] ,json: String) : Future[String] = {
        service.fromJson(json).map{
            request => service.handle(request).map{
                response => service.toJson(response)
            }.recoverWith{
                case e: Exception =>
                    Future.successful(Error.fmtJson.writes(Error(s"request failed with error ${e.getMessage}")).toString)
            }
        }.getOrElse(Future.successful(Error.fmtJson.writes(Error("")).toString))
    }
    
    def handleHealthCheck: String = "alive"
    
    def handleLogin(request: String): Future[String] =
        process(LoginService,request)
    
    
    
    
}
