package cupper.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object User {
  val ROOT = "/v1/users"

  val login = exec(http("login")
    .post(s"$ROOT/login")
    .body(StringBody(
      """{
        |"login_id":"${login_id}",
        |"password":"${password}"
        |}""".stripMargin
    ))
    .check(status.is(200))
    .check(jsonPath("$..token").saveAs("token"))
  )

  val logout = exec(http("logout")
    .post(s"$ROOT/logout")
    .header(HttpHeaderNames.Authorization, "Bearer ${token}")
  )
}

object Tweet {
  val ROOT = "/v1/tweet"

  val create = exec(http("post message")
    .post(s"${ROOT}")
  )

  val findOwn = exec(http("find own tweets")
    .post(s"${ROOT}")
  )

  val search = exec(http("search tweets")
    .post(s"${ROOT}")
  )
}
