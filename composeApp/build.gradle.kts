import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kspCompose)
    alias(libs.plugins.androidxRoom)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            //Ktor
            implementation(libs.ktor.client.okhttp)

            //Splash
            implementation(libs.core.splashscreen)

            //room
            implementation(libs.room.runtime.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(compose.materialIconsExtended)

            //Navigation Compose
            implementation(libs.navigation.compose)

            //Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.kotlin.serialization)

            //Viewmodel
            implementation(libs.viewmodel.compose)

            //Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            //Date
            implementation(libs.kotlinx.datetime)

            //Room
            implementation(libs.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            //Logger
            implementation(libs.logger)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            //Ktor
            implementation(libs.ktor.client.cio)
            //Apis Windows
            //implementation(libs.jna.core)
            //implementation(libs.jna.platform)
        }

        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata")
        }
    }
}

android {
    namespace = "com.herargos.herargosadmistrativo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.herargos.herargosadmistrativo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    ksp(libs.room.compiler)
}

compose.desktop { // <-- Primer nivel: compose.desktop
    application { // <-- Segundo nivel: application
        mainClass = "com.herargos.herargosadmistrativo.MainKt"

        nativeDistributions { // <-- TERCER NIVEL: nativeDistributions
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PanOregano"
            packageVersion = "1.0.0"

            // *** ESTAS LÍNEAS DEBEN ESTAR AQUÍ, DIRECTAMENTE DENTRO DE nativeDistributions { ... } ***
            //jvmVendor("oracle") // <--- Aquí
            //jvmVersion("17")    // <--- Y aquí
            // ***********************************************************************************

            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/icon.ico"))
                shortcut = true
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

