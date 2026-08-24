import tgx.gradle.task.GenerateTdlibFunctionsTask

plugins {
  id("java-toolchain-convention")
  id(libs.plugins.android.library.get().pluginId)
  id("tgx-module")
}

val generateTdlibFunctions = tasks.register<GenerateTdlibFunctionsTask>("generateTdlibFunctions") {
  group = "Setup"
  description = "Generates TDLib utility functions based on current TdApi.java"
  tdApi.set(layout.projectDirectory.file(
    "../../tdlib/src/main/java/org/drinkless/tdlib/TdApi.java"
  ))
  kotlinOutputDir.set(layout.projectDirectory.dir(
    "src/generated/kotlin"
  ))
}

dependencies {
  implementation(project(":tdlib"))
  implementation(project(":vkryl:core"))
}

android {
  namespace = "tgx.td.unused"

  androidComponents {
    onVariants { variant ->
      variant.sources.kotlin?.addGeneratedSourceDirectory(
        generateTdlibFunctions, GenerateTdlibFunctionsTask::kotlinOutputDir
      )
    }
  }
}