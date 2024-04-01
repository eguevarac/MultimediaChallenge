plugins {
    kotlin("kapt")
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.multimediachallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.multimediachallenge"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val navVersion = "2.7.7"


    //Para poder hacer crop a la imagen
    implementation("com.vanniktech:android-image-cropper:4.5.0")

    //para google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //para poder usar supportFragmentmanager
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //Para poder acceder a la localización
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")

    //navComponent
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")


    //las que vienen de serie
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
kapt {
    correctErrorTypes = true
}