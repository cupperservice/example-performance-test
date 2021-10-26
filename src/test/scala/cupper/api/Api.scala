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
    .post(s"${ROOT}/")
    .header(HttpHeaderNames.Authorization, "Bearer ${token}")
    .body(StringBody(
      """{
        |"content": "静岡駅 Now! #shizuoka"
        |}
        |""".stripMargin))
    .check(status.is(200))
  )

  val findOwn = exec(http("find own tweets")
    .get(s"${ROOT}/")
    .header(HttpHeaderNames.Authorization, "Bearer ${token}")
    .check(status.in(200))
  )

  val search = exec(http("search tweets")
    .post(s"${ROOT}/find_by_hash_tag")
    .header(HttpHeaderNames.Authorization, "Bearer ${token}")
    .body(StringBody(
      """{
        |"tag":"shizuoka"
        |}
        |""".stripMargin))
    .check(status.in(200))
  )
}
