pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "server"

enableFeaturePreview("VERSION_CATALOGS")

include(":common")
include(":minestom-common")

include(":proxy")
include(":queue")
include(":texture")
include(":main-lobby")

include(":dungeons:core")
include(":dungeons:lobby")
include(":dungeons:gameplay")

include(":housing:core")
include(":housing:lobby")
include(":housing:gameplay")

include(":pvp-battlegrounds:core")
include(":pvp-battlegrounds:lobby")
include(":pvp-battlegrounds:gameplay")

include(":pvp-resource-world:core")
include(":pvp-resource-world:lobby")
include(":pvp-resource-world:gameplay")

include(":pvp-survival:core")
include(":pvp-survival:lobby")
include(":pvp-survival:gameplay")

include(":pvp-resource-world:core")
include(":pvp-resource-world:lobby")
include(":pvp-resource-world:gameplay")

include(":safe-resource-world:core")
include(":safe-resource-world:lobby")
include(":safe-resource-world:gameplay")

include(":safe-survival:core")
include(":safe-survival:lobby")
include(":safe-survival:gameplay")
