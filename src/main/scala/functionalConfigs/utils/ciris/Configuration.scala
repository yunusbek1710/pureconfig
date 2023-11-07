package functionalConfigs.utils.ciris

import scala.concurrent.duration.FiniteDuration

import ciris.Secret
import enumeratum.CirisEnum
import enumeratum.EnumEntry
import enumeratum._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.string.Uri
import eu.timepit.refined.string.Url
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

object Configuration {
  type SecretKey = String Refined MatchesRegex["[a-zA-Z0-9]{20,45}"]
  type RefinedUrl = String Refined Url
  type RefinedUri = String Refined Uri

  sealed trait AppEnvironment extends EnumEntry
  object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
    case object Local extends AppEnvironment
    case object Stage extends AppEnvironment
    case object Production extends AppEnvironment

    val values = findValues
  }

  final case class ServiceConfig(
      adminJwt: AdminJwt,
      userJwt: UserJwt,
      passwordSalt: PasswordSalt,
      tokenExpiration: TokenExpiration,
      checkout: Checkout,
      payment: Payment,
      httpClient: HttpClient,
      httpServer: HttpServer,
      postgres: Postgres,
      redis: Redis,
    )

  final case class AdminJwt(
      secretKey: Secret[SecretKey],
      claim: Secret[NonEmptyString],
      adminToken: Secret[NonEmptyString],
    )

  final case class UserJwt(secretKey: Secret[SecretKey])
  final case class PasswordSalt(value: Secret[NonEmptyString])
  final case class TokenExpiration(value: FiniteDuration)
  final case class Checkout(retriesLimit: PosInt, retriesBackoff: FiniteDuration)
  final case class Payment(uri: Secret[RefinedUrl])
  final case class HttpClient(connectionTimeout: FiniteDuration, requestTimeout: FiniteDuration)
  final case class HttpServer(host: Secret[RefinedUrl], port: UserPortNumber)

  final case class Postgres(
      host: RefinedUri,
      port: UserPortNumber,
      user: NonEmptyString,
      database: NonEmptyString,
      password: Secret[NonEmptyString],
      max: PosInt,
    )

  final case class Redis(uri: RefinedUri)
}
