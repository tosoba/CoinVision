plugins {
  alias(libs.plugins.kotlinMultiplatform)
  kotlin("native.cocoapods")
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.sqlDelight)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  androidTarget()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    version = "1.0.0"
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    ios.deploymentTarget = "14.1"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      isStatic = true
    }
  }

  targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach {
    it.binaries.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().forEach { lib
      ->
      lib.isStatic = false
      lib.linkerOpts.add("-lsqlite3")
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(compose.runtime)

        implementation(libs.bignum)

        implementation(libs.kamel)

        api(libs.koin.core)

        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        implementation(libs.logback.classic)

        implementation(libs.material3.windowSizeClass)

        implementation(libs.napier)

        implementation(libs.okio)

        implementation(libs.paging.compose.common)

        implementation(libs.shimmer)

        implementation(libs.sqldelight.coroutines.extensions)

        implementation(libs.voyager.koin)
        implementation(libs.voyager.navigator)
        implementation(libs.voyager.tab.navigator)
        implementation(libs.voyager.transitions)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(libs.kotlinx.coroutines.test)
      }
    }

    val androidMain by getting {
      dependencies {
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
    }
    val androidUnitTest by getting

    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)

      dependencies {
        implementation(libs.stately.common)
        implementation(libs.ktor.client.darwin)
        implementation(libs.sqldelight.native.driver)
      }
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

android {
  namespace = "com.trm.coinvision"
  compileSdk = 34
  defaultConfig { minSdk = 21 }

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  // hack for problem:
  // "Expected object 'MR' has no actual declaration in module <mpp-library_debug> for JVM"
  // Issue: https://github.com/icerockdev/moko-resources/issues/510
  // Remove after fix this
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlin { jvmToolchain(17) }

  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}" } }
}

sqldelight {
  databases { create("CoinVisionDb") { packageName.set("com.trm.coinvision.db") } }
  linkSqlite = true
}
