package functionalConfigs.utils.pureconfig

import scala.concurrent.duration.FiniteDuration

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
      secretKey: SecretKey,
      claim: NonEmptyString,
      adminToken: NonEmptyString,
    )

  final case class UserJwt(secretKey: SecretKey)
  final case class PasswordSalt(value: NonEmptyString)
  final case class TokenExpiration(value: FiniteDuration)
  final case class Checkout(retriesLimit: PosInt, retriesBackoff: FiniteDuration)
  final case class Payment(uri: RefinedUrl)
  final case class HttpClient(connectionTimeout: FiniteDuration, requestTimeout: FiniteDuration)
  final case class HttpServer(host: RefinedUrl, port: UserPortNumber)

  final case class Postgres(
      host: RefinedUri,
      port: UserPortNumber,
      user: NonEmptyString,
      database: NonEmptyString,
      password: NonEmptyString,
      max: PosInt,
    )

  final case class Redis(uri: RefinedUri)
}
