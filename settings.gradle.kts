pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "server"

enableFeaturePreview("VERSION_CATALOGS")

include(":core")
include(":queue")
include(":texture")
include(":main-lobby")

include(":dungeons:core")
include(":housing:core")
include(":pvp-battlegrounds:core")
include(":pvp-resource-world:core")
include(":pvp-survival:core")
include(":pvp-resource-world:core")
include(":safe-resource-world:core")
include(":safe-survival:core")

include(":dungeons:lobby")
include(":housing:lobby")
include(":pvp-battlegrounds:lobby")
include(":pvp-resource-world:lobby")
include(":pvp-survival:lobby")
include(":pvp-resource-world:lobby")
include(":safe-resource-world:lobby")
include(":safe-survival:lobby")

include(":dungeons:gameplay")
include(":housing:gameplay")
include(":pvp-battlegrounds:gameplay")
include(":pvp-resource-world:gameplay")
include(":pvp-survival:gameplay")
include(":pvp-resource-world:gameplay")
include(":safe-resource-world:gameplay")
include(":safe-survival:gameplay")
