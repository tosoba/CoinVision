plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("com.android.library")
  id("org.jetbrains.compose")
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  androidTarget()

  jvm("desktop")

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

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(compose.runtime)

        implementation(libs.logback.classic)

        api(libs.koin.core)

        implementation(libs.kotlinx.serialization.json)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        implementation(libs.okio)

        implementation(libs.paging.compose.common)

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

        implementation(libs.koin.android)
        implementation(libs.koin.core)

        implementation(libs.kotlinx.coroutines.android)

        implementation(libs.ktor.client.android)
        implementation(libs.ktor.client.okhttp)
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

      dependencies { implementation(libs.ktor.client.darwin) }
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

    val desktopMain by getting { dependencies { implementation(compose.desktop.common) } }
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlin { jvmToolchain(11) }

  packagingOptions { resources { excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}" } }
}
