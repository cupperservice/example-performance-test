package cupper.scenario

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import cupper.config.Config
import cupper.api.{User, Tweet}

import scala.util.Random
import scala.concurrent.duration._

class Scenario extends Simulation with Config {
  Random.setSeed(System.currentTimeMillis())
  val httpProtocol = http
    .baseUrl(TARGET)
    .headers(Map(
      HttpHeaderNames.ContentType -> HttpHeaderValues.ApplicationJson,
      HttpHeaderNames.UserAgent -> "gatling"
    ))

  // 性能測定で使用するユーザーのリスト
  val feeder = csv("users.csv").eager.circular

  /**
   * 1. ログイン
   * 2. 自身のメッセージを取得する
   * 3. 以下を95: 5の割合で実行する
   * 3.1 メッセージを検索 * 10回
   * 3.2 メッセージを送信
   * 4. ログアウト
   */
  val user = scenario("user1")
    .feed(feeder)
    .exec(User.login).exitHereIfFailed

  setUp(
//    user1.inject(atOnceUsers(1)),
    user.inject(incrementUsersPerSec(5).times(10).eachLevelLasting(30.seconds).startingFrom(1)),
  ).protocols(httpProtocol)
}
