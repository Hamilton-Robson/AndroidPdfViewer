import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.JavaVersion

plugins {
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

val javaVersion: JvmTarget by rootProject.extra

android {
    compileSdk = 34

    defaultConfig {
        namespace = "com.github.barteksc.pdfviewer"
        minSdk = 23
        lint.targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
  jvmToolchain(17)               // ★ new — forces Kotlin to 17
  compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
  }
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            val libraryVersion: String by rootProject.extra

            groupId = "com.github.zacharee"
            artifactId = "AndroidPdfViewer"
            version = libraryVersion

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    api(libs.pdfiumandroid)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
