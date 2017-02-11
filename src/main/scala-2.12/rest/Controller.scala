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
    
    private def process[REQ,RES](service : Service[REQ,RES] ,json: String) : Future[HttpResponse] = {
        service.fromJson(json).map{
            request => service.handle(request).map{
                response => toHttpResponse(service.toJson(response))
            }.recoverWith{
                case e: Exception =>
                    Future.successful(new Error(500,s"request failed with error ${e.getMessage}").asHttpResponse)
            }
        }.getOrElse(Future.successful(new Error(StatusCodes.BadRequest,"").asHttpResponse))
    }
    
    def toHttpResponse(response: String) : HttpResponse =
        HttpResponse(200,entity = HttpEntity(ContentType(MediaTypes.`application/json`),response))
    
    def handleHealthCheck: String = "alive"
    
    def handleLogin(request: String): Future[HttpResponse] =
        process(LoginService,request)
    
    
    
    
}
