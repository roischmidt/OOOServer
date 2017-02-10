package api

import play.api.libs.json.Json
/**
  * Created by rois on 10/02/2017.
  * every message (expect login) should inherit Session. stores auth data
  */
class Session(token: String) {
    val TOKEN = token
}

object Session {
    def apply(token: String) = new Session(token)
    def unapply(msg: Session) = Some(msg.TOKEN)
    
    implicit val fmtJson = Json.format[Session]
}
