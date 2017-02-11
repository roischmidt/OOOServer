package service

import scala.concurrent.Future

/**
  * Created by rois on 10/02/2017.
  */
trait Service [REQ,RES]{
    
    def handle(request: REQ) : Future[RES]
    
    def fromJson(request : String) : Option[REQ]
    
    def toJson(response: RES) : String
    
}
