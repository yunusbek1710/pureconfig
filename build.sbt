name := "pureconfig"

scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig"         % "0.17.4",
      "eu.timepit"            %% "refined"            % "0.10.3",
      "eu.timepit"            %% "refined-pureconfig" % "0.10.3",
      "eu.timepit"            %% "refined-cats"       % "0.10.3",
      "is.cir"                %% "ciris"              % "3.1.0",
      "is.cir"                %% "ciris-enumeratum"   % "3.1.0",
      "is.cir"                %% "ciris-refined"      % "3.1.0",
      "org.typelevel"         %% "cats-core"          % "2.9.0",
      "org.typelevel"         %% "cats-effect"        % "3.4.8",
    )
  )

Global / onChangedBuildSource := ReloadOnSourceChanges
