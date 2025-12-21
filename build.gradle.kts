plugins {
  id(libs.plugins.android.library.get().pluginId)
  alias(libs.plugins.kotlin.android)
  id("tgx-module")
}

dependencies {
  implementation(project(":tdlib"))
  implementation(project(":vkryl:core"))
}

android {
  namespace = "tgx.td.unused"
}