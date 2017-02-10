package api

import play.api.libs.json.Json

/**
  * Created by rois on 10/02/2017.
  */

case class LoginRequest (
    nickName: String
)

object LoginRequest{
    implicit val fmtJson = Json.format[LoginRequest]
}

case class LoginResponse (
    success: Boolean, // if nickname already exists false. else true
    token: Option[String] // filled only if nickname not exists
)

object LoginResponse {
    implicit val fmtJson = Json.format[LoginResponse]
}
