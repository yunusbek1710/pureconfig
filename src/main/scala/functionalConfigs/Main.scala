package functionalConfigs

//import cats.effect.IO
//import cats.effect.unsafe.implicits.global
//import functionalConfigs.utils.ciris.Configurable
import functionalConfigs.utils.pureconfig.Configurable

// pureconfig with refined types

object Main extends App with Configurable {
  println(s"$config")
}

// This is environment variable config loader with ciris

//object Main extends App with Configurable {
//  println(s"${config.load[IO].unsafeRunSync()}")
//}
