import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.sqlDelight)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  jvmToolchain(17)

  android {
    namespace = "com.trm.coinvision"
    compileSdk = 37
    minSdk = 24

    // AGP 9 KMP library plugin processes Android resources only when explicitly
    // enabled. Required so Compose Multiplatform resources (*.cvr under
    // composeResources/) are packaged into the AAR/APK.
    androidResources { enable = true }

    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }

    // Host (unit) tests are disabled by default by the Android-KMP library plugin.
    // Enabling them keeps the commonTest tests running on the Android target.
    withHostTest {}
  }

  listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "shared"
      isStatic = false
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.material.icons.extended)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.runtime)

      implementation(libs.bignum)

      implementation(libs.kamel)
      implementation(libs.kamel.decoder.image.bitmap)
      implementation(libs.kamel.decoder.image.vector)

      implementation(libs.koin.core)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)

      implementation(libs.kotlinx.datetime)
      implementation(libs.kotlinx.serialization.json)

      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.logging)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.kotlinx.json)

      implementation(libs.lifecycle.viewmodel)
      implementation(libs.lifecycle.viewmodel.compose)

      implementation(libs.logback.classic)

      implementation(libs.material3.windowSizeClass)

      implementation(libs.napier)

      implementation(libs.okio)

      implementation(libs.paging.compose)

      implementation(libs.shimmer)

      implementation(libs.sqldelight.coroutines.extensions)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(libs.kotlinx.coroutines.test)
    }

    androidMain.dependencies {
      api(libs.activity.compose)
      api(libs.appcompat)
      api(libs.core.ktx)

      implementation(libs.tradingview.charts)

      implementation(libs.koin.android)
      implementation(libs.koin.core)

      implementation(libs.kotlinx.coroutines.android)

      implementation(libs.ktor.client.android)
      implementation(libs.ktor.client.okhttp)

      implementation(libs.sqldelight.android.driver)
    }

    iosMain.dependencies {
      implementation(libs.stately.common)
      implementation(libs.ktor.client.darwin)
      implementation(libs.sqldelight.native.driver)
    }
  }
}

sqldelight {
  databases { create("CoinVisionDb") { packageName.set("com.trm.coinvision.db") } }
  linkSqlite = true
}
