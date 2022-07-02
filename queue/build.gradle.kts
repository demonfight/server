import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.graalvm.buildtools.gradle.NativeImagePlugin
import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask

apply<ShadowPlugin>()
//apply<NativeImagePlugin>()

dependencies {
  implementation(rootProject.libs.gson)
  implementation(rootProject.libs.fastutil)
  implementation(rootProject.libs.kotlin) {
    exclude("org.jetbrains", "annotations")
  }
  implementation(rootProject.libs.minestom) {
    exclude("com.google.code.gson", "gson")
    exclude("it.unimi.dsi", "fastutil")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains", "annotations")
  }
}

tasks {
  withType<ShadowJar> {
    archiveClassifier.set(null as String?)
  }

  //withType<BuildNativeImageTask> {
  //  classpathJar.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
  //}
}

//configure<GraalVMExtension> {
//  useArgFile.set(false)
//  testSupport.set(false)
//  binaries {
//    named("main") {
//      javaLauncher.set(javaToolchains.launcherFor {
//        languageVersion.set(JavaLanguageVersion.of(17))
//        vendor.set(JvmVendorSpec.matching("GraalVM Community"))
//      })
//      imageName.set("server")
//      mainClass.set("tr.com.infumia.server.queue.Server")
//      useFatJar.set(true)
//    }
//  }
//}
