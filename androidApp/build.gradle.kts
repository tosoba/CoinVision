plugins {
  kotlin("multiplatform")
  id("com.android.application")
  id("org.jetbrains.compose")
}

kotlin {
  androidTarget()
  sourceSets {
    val androidMain by getting { dependencies { implementation(project(":shared")) } }
  }
}

android {
  namespace = "com.trm.coinvision.android"
  compileSdk = 33

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

  defaultConfig {
    applicationId = "com.trm.coinvision.android"
    minSdk = (findProperty("android.minSdk") as String).toInt()
    targetSdk = (findProperty("android.targetSdk") as String).toInt()
    versionCode = 1
    versionName = "1.0"
  }

  packagingOptions { resources { excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}" } }

  buildTypes { getByName("release") { isMinifyEnabled = false } }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlin { jvmToolchain(11) }
}
