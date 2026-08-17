plugins {
  id("java-toolchain-convention")
  id(libs.plugins.android.library.get().pluginId)
  id("tgx-module")
}

dependencies {
  implementation(project(":tdlib"))
  implementation(project(":vkryl:core"))
}

android {
  namespace = "tgx.td.unused"
}