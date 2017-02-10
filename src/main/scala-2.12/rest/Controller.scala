package rest

import akka.http.scaladsl.model._
import service.LoginService

import scala.concurrent.Future
/**
  * Created by rois on 10/02/2017.
  */
object Controller {
    
    def handleHealthCheck: String = "alive"
    
    def handleLogin(request: String): Future[HttpResponse] =
        LoginService.process(request)
    
    
    
    
}
