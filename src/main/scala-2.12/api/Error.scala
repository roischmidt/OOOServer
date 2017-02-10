package api

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCode}

/**
  * Created by rois on 10/02/2017.
  */
class Error(statusCode: StatusCode,reason: String) {
    
    def asHttpResponse : HttpResponse = HttpResponse(statusCode,entity = HttpEntity(reason))

}
