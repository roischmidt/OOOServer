package service

import api.LoginResponse
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json.{JsError, JsSuccess, Json}
import rest.Controller
import utils.TokenManager

/**
  * Created by rois on 11/02/2017.
  */
class LoginServiceSpec extends FunSpec with Matchers with ScalaFutures{
    
    describe("login request") {
        it("valid token"){
            val loginRequest = """{"nickName":"test_user"}"""
            whenReady(Controller.handleLogin(loginRequest)) { res =>
                LoginResponse.fmtJson.reads(Json.parse(res)) match {
                    case JsSuccess(loginResponse, _) =>
                        loginResponse.success shouldBe true
                        loginResponse.token shouldBe defined
                        val opt = TokenManager.parseTokenClaimUsername(loginResponse.token.get)
                        opt shouldBe defined
                        opt.get shouldBe "test_user"
                    case JsError(e) =>
                        fail("bad response")
        
                }
            }
        }
    }
}
