import sbt._

object Dependencies {

  object Versions {
    val circe       = "0.13.0"
    val neutron     = "0.0.0+1-7d777064-SNAPSHOT"

    val betterMonadicFor = "0.3.1"
    val contextApplied   = "0.1.2"
    val kindProjector    = "0.11.0"
  }

  object Libraries {
    def circe(artifact: String): ModuleID  = "io.circe"   %% artifact % Versions.circe

    val circeCore    = circe("circe-core")
    val circeGeneric = circe("circe-generic")
    val circeParser  = circe("circe-parser")

    val neutron  = "com.chatroulette" %% "neutron-core" % Versions.neutron
  }

  object CompilerPlugins {
    val betterMonadicFor = compilerPlugin("com.olegpy"     %% "better-monadic-for" % Versions.betterMonadicFor)
    val contextApplied   = compilerPlugin("org.augustjune" %% "context-applied"    % Versions.contextApplied)
    val kindProjector = compilerPlugin(
      "org.typelevel" %% "kind-projector" % Versions.kindProjector cross CrossVersion.full
    )
  }

}
