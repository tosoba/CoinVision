plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.trm.coinvision.android"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.trm.coinvision.android"
    minSdk = (findProperty("android.minSdk") as String).toInt()
    targetSdk = (findProperty("android.targetSdk") as String).toInt()
    versionCode = 1
    versionName = "1.0"
  }

  packaging {
    resources {
      excludes += "/META-INF/AL2.0"
      excludes += "/META-INF/LGPL2.1"
      excludes += "/META-INF/INDEX.LIST"
    }
  }

  buildTypes { getByName("release") { isMinifyEnabled = false } }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  implementation(project(":shared"))

  implementation(libs.koin.android)
  implementation(libs.koin.core)
}
