package api

import play.api.libs.json.Json

/**
  * Created by rois on 10/02/2017.
  */
case class Error(
    reason: String
)

object Error{
    implicit val fmtJson = Json.format[Error]
}
