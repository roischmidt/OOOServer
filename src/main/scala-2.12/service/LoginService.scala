package service

import api.{LoginRequest, LoginResponse}
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsError, JsSuccess, Json}
import utils.TokenManager

import scala.concurrent.Future

/**
  * Created by rois on 10/02/2017.
  */
object LoginService extends Service[LoginRequest, LoginResponse] {
    
    val logger = LoggerFactory.getLogger(getClass)
    
     override def handle(request: LoginRequest): Future[LoginResponse] = {
        isNickNameAlreadyExists(request.nickName) match {
            case true => Future.successful(LoginResponse(false,None))
            case false =>
                val token = TokenManager.createToken(request.nickName)
                //TODO: store in redis (key = nickName, value = token). add nickName to list of users
                Future.successful(LoginResponse(true,Some(token)))
        }
    }
        
    
    override def fromJson(request: String): Option[LoginRequest] =
        LoginRequest.fmtJson.reads(Json.parse(request)) match {
            case JsSuccess(loginRequest, _) =>
                Some(loginRequest)
            case JsError(e) =>
                logger.info(s"error parsing LoginRequest : $e")
                None
        }
    
    override def toJson(response: LoginResponse): String =
        LoginResponse.fmtJson.writes(response).toString()
    
    def isNickNameAlreadyExists(nickName: String) : Boolean = {
        //TODO: check in redis
        false
    }
        
}
