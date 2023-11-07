# PureConfig

[![Build Status](https://github.com/pureconfig/pureconfig/workflows/CI/badge.svg?branch=master)](https://github.com/pureconfig/pureconfig/actions?query=workflow%3ACI+branch%3Amaster)
[![Coverage Status](https://coveralls.io/repos/github/pureconfig/pureconfig/badge.svg?branch=master)](https://coveralls.io/github/pureconfig/pureconfig?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.pureconfig/pureconfig_2.12/badge.svg)](https://search.maven.org/artifact/com.github.pureconfig/pureconfig_2.12)
[![Scaladoc](https://javadoc.io/badge/com.github.pureconfig/pureconfig-core_2.12.svg)](https://javadoc.io/page/com.github.pureconfig/pureconfig-core_2.12/latest/pureconfig/index.html)
[![Join the chat at https://gitter.im/melrief/pureconfig](https://badges.gitter.im/melrief/pureconfig.svg)](https://gitter.im/melrief/pureconfig?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Quick Start

To use PureConfig in an existing SBT project with Scala 2.13 or a later version, add the following dependency to your
`build.sbt`:

```scala
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.17.4"
```

`Refined types for pureconfig`

```scala
libraryDependencies += "eu.timepit" %% "refined" % "0.10.3"
libraryDependencies += "eu.timepit" %% "refined-pureconfig" % "0.10.3"
libraryDependencies += "eu.timepit" %% "refined-cats" % "0.10.3"
```

`Ciris`

```scala
libraryDependencies += "is.cir" %% "ciris" % "3.1.0"
libraryDependencies += "is.cir" %% "ciris-enumeratum" % "3.1.0"
libraryDependencies += "is.cir" %% "ciris-refined" % "3.1.0"
```

For a full example of `build.sbt` you can have a look at
this [build.sbt](https://github.com/pureconfig/pureconfig/blob/master/example/build.sbt).

Earlier versions of Scala had bugs which can cause subtle compile-time problems in PureConfig.
As a result we recommend only using the latest Scala versions within the minor series.

In your code, import `pureconfig.generic.auto` and define data types and a case class to hold the configuration:

```scala
import pureconfig._
import pureconfig.generic.auto._

case class Port(number: Int) extends AnyVal

sealed trait AuthMethod

case class Login(username: String, password: String) extends AuthMethod

case class Token(token: String) extends AuthMethod

case class PrivateKey(pkFile: java.io.File) extends AuthMethod

case class ServiceConf(
    host: String,
    port: Port,
    useHttps: Boolean,
    authMethods: List[AuthMethod]
)
```

Second, create an `application.conf` file and add it as a resource of your application (with SBT, they are usually
placed in `src/main/resources`):

```
// src/main/resources/application.conf
host = "example.com"
port = 8080
use-https = true
auth-methods = [
  { type = "private-key", pk-file = "/home/user/myauthkey" },
  { type = "login", username = "pureconfig", password = "12345678" }
]
```

Finally, load the configuration:

```scala
ConfigSource.default.load[ServiceConf]
// res4: ConfigReader.Result[ServiceConf] = Right(
//   ServiceConf(
//     "example.com",
//     Port(8080),
//     true,
//     List(PrivateKey(/home/user/myauthkey), Login("pureconfig", "12345678"))
//   )
// )
```

## Documentation

Please see the [full PureConfig documentation](https://pureconfig.github.io/docs) for more information.