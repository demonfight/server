dependencies {
  compileOnly(project(":common"))

  compileOnly(libs.terminable)

  compileOnly(rootProject.libs.gson)
  compileOnly(rootProject.libs.fastutil)
  compileOnly(rootProject.libs.kotlin) {
    exclude("org.jetbrains", "annotations")
  }
  compileOnly(rootProject.libs.minestom) {
    exclude("com.google.code.gson", "gson")
    exclude("it.unimi.dsi", "fastutil")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains", "annotations")
  }
}
