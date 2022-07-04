dependencies {
  compileOnly(project(":common"))

  compileOnly(libs.terminable)

  compileOnly(rootProject.libs.minestom) {
    exclude("com.google.code.gson", "gson")
    exclude("it.unimi.dsi", "fastutil")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains", "annotations")
  }
}
