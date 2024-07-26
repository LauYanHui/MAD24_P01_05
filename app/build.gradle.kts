plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "sg.edu.np.mad.cookbuddy"
    compileSdk = 34


    defaultConfig {
        applicationId = "sg.edu.np.mad.cookbuddy"

        minSdk = 33
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            getByName("release") {
                isShrinkResources = true
                isMinifyEnabled = true
            }
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            // You can also exclude other conflicting files if needed
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("META-INF/INDEX.LIST")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.cardview)
    implementation (libs.viewpager2)
    implementation (libs.glide)
    implementation(libs.recyclerview.animators)
    implementation("com.airbnb.android:lottie:3.4.0")

    // don't specify version for firebase library dependencies if using bom
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("nu.aaro.gustav:passwordstrengthmeter:0.4")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor (libs.glide.compiler)

    implementation(libs.firebase.storage)
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("com.google.android.material:material:1.9.0")
    annotationProcessor (libs.glide.compiler)
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation ("com.google.cloud:google-cloud-vision:2.0.0")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("androidx.camera:camera-core:1.0.2")
    implementation ("androidx.camera:camera-camera2:1.0.2")
    implementation ("androidx.camera:camera-lifecycle:1.0.2")
    implementation ("androidx.camera:camera-view:1.0.0-alpha24")
    implementation ("androidx.camera:camera-extensions:1.0.0-alpha24")
    implementation ("androidx.activity:activity-ktx:1.3.0")
    implementation ("androidx.fragment:fragment-ktx:1.4.0")
    implementation ("io.grpc:grpc-okhttp:1.40.1")
    implementation ("io.grpc:grpc-protobuf:1.40.1")
    implementation ("io.grpc:grpc-stub:1.40.1")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.github.rubensousa:gravitysnaphelper:2.2.2")

}