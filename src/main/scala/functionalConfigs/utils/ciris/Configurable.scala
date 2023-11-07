package functionalConfigs.utils.ciris

import cats.implicits._
import ciris._
import ciris.refined._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import functionalConfigs.utils.ciris.Configurable.serviceConfig
import functionalConfigs.utils.ciris.Configuration._
import functionalConfigs.utils.ciris.Configuration.AppEnvironment._

import scala.concurrent.duration.DurationInt

trait Configurable {
  val config: ConfigValue[Effect, ServiceConfig] = serviceConfig
}

object Configurable {
  private val serviceConfig: ConfigValue[Effect, ServiceConfig] = {
    env("APP_ENV").as[AppEnvironment].option.flatMap {
      appEnv =>
      (
        env("JWT_SECRET_KEY").as[SecretKey].secret,
        env("CLAIM").as[NonEmptyString].secret,
        env("ADMIN_TOKEN").as[NonEmptyString].secret,
        env("USER_SECRET_KEY").as[SecretKey].secret,
        env("PASSWORD_SALT").as[NonEmptyString].secret,
        env("PAYMENT_URI").as[RefinedUrl].secret,
        env("SERVER_HOST").as[RefinedUrl].secret,
        env("SERVER_PORT").as[UserPortNumber],
        env("POSTGRES_HOST").as[RefinedUri],
        env("POSTGRES_PORT").as[UserPortNumber],
        env("POSTGRES_USER").as[NonEmptyString],
        env("POSTGRES_DATABASE").as[NonEmptyString],
        env("POSTGRES_PASSWORD").as[NonEmptyString].secret,
        env("POSTGRES_MAX").as[PosInt],
        env("REDIS_URI").as[RefinedUri],
      ).parMapN{
        (adminSecret, claim, adminToken, userSecretKey, salt, paymentUri, serverHost, serverPort, pgHost, pgPort, pgUser, pgDatabase, pgPassword, pgMax, redisUri) =>
        ServiceConfig(
          adminJwt = AdminJwt(adminSecret, claim, adminToken),
          userJwt = UserJwt(userSecretKey),
          passwordSalt = PasswordSalt(salt),
          tokenExpiration =
            appEnv match {
              case Some(Production) => TokenExpiration(30.minutes)
              case Some(Stage) => TokenExpiration(2.hours)
              case _ => TokenExpiration(24.hours)
            },
          checkout = Checkout(
            retriesLimit = PosInt.unsafeFrom(3),
            retriesBackoff = 10.milliseconds
          ),
          payment = Payment(paymentUri),
          httpClient = HttpClient(2.seconds, 2.seconds),
          httpServer = HttpServer(serverHost, serverPort),
          postgres = Postgres(pgHost, pgPort, pgUser, pgDatabase, pgPassword, pgMax),
          redis = Redis(uri = redisUri)
        )
      }
    }
}
}
