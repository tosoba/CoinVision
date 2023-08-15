plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("com.android.library")
  id("org.jetbrains.compose")
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
    extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.logback.classic)

        implementation(libs.coingecko)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
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
  }
}

android {
  namespace = "com.trm.coinvision"
  compileSdk = 33
  defaultConfig { minSdk = 21 }

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlin { jvmToolchain(11) }
}
