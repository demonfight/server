dependencies {
  compileOnlyApi(libs.lombok)
  compileOnlyApi(libs.annotations)

  compileOnly(libs.terminable)
  compileOnly(libs.redis)
  compileOnly(libs.guava)
  compileOnly(libs.caffeine)
  compileOnly(libs.kubernetes)
  compileOnly(libs.agones4j)
  implementation(libs.guice)
}
