package service

import akka.http.scaladsl.model._
import api.Error

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by rois on 10/02/2017.
  */
trait Service [REQ,RES]{
    
    def process(json: String) : Future[HttpResponse] = {
        fromJson(json).map{
            request => handle(request).map{
                response => toHttpResponse(response)
            }.recoverWith{
                case e: Exception =>
                    Future.successful(new Error(500,s"request failed with error ${e.getMessage}").asHttpResponse)
            }
        }.getOrElse(Future.successful(new Error(StatusCodes.BadRequest,"").asHttpResponse))
    }
    
   
    def handle(request: REQ) : Future[RES]
    
    def fromJson(request : String) : Option[REQ]
    
    def toJson(response: RES) : String
    
    def toHttpResponse(response: RES) : HttpResponse =
        HttpResponse(200,entity = HttpEntity(ContentType(MediaTypes.`application/json`),toJson(response)))
}
